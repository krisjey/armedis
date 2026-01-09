package com.github.armedis.redis.info;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;

public class ReflectionManipulator {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionManipulator.class);

    private static final ConcurrentHashMap<String, Optional<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Optional<Method>> METHOD_CACHE = new ConcurrentHashMap<>();

    public static void setFieldValue(Object target, String fieldName, String value) {
        Optional<Field> optionalField = getFieldFromCache(target.getClass(), fieldName);

        if (optionalField.isEmpty()) {
            logger.debug("Field not found: {}.{}", target.getClass().getSimpleName(), fieldName);
            return;
        }

        try {
            Field field = optionalField.get();
            Class<?> type = field.getType();

            if (type == int.class) {
                field.setInt(target, NumberUtils.toInt(value));
            }
            else if (type == long.class) {
                field.setLong(target, NumberUtils.toLong(value));
            }
            else if (type == double.class) {
                field.setDouble(target, NumberUtils.toDouble(value));
            }
            else if (type == float.class) {
                field.setFloat(target, NumberUtils.toFloat(value));
            }
            else {
                field.set(target, value);
            }
        }
        catch (IllegalAccessException e) {
            logger.error("Failed to set field: {}.{}", target.getClass().getSimpleName(), fieldName, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object target, String fieldName) {
        Optional<Field> optionalField = getFieldFromCache(target.getClass(), fieldName);

        if (optionalField.isEmpty()) {
            logger.debug("Field not found: {}.{}", target.getClass().getSimpleName(), fieldName);
            return null;
        }

        try {
            return (T) optionalField.get().get(target);
        }
        catch (IllegalAccessException e) {
            logger.error("Failed to get field: {}.{}", target.getClass().getSimpleName(), fieldName, e);
            return null;
        }
    }

    public static Object getMethodInvokeResult(Object target, String fieldName) {
        Class<?> clazz = target.getClass();
        String cacheKey = clazz.getName() + "#get" + fieldName;

        Optional<Method> optionalMethod = METHOD_CACHE.computeIfAbsent(cacheKey, k -> {
            String methodName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
            try {
                Method m = clazz.getDeclaredMethod(methodName);
                m.setAccessible(true);
                return Optional.of(m);
            }
            catch (NoSuchMethodException e) {
                return Optional.empty();
            }
        });

        if (optionalMethod.isEmpty()) {
            logger.debug("Method not found: {}.get{}", clazz.getSimpleName(), fieldName);
            return null;
        }

        try {
            return optionalMethod.get().invoke(target);
        }
        catch (Exception e) {
            logger.error("Failed to invoke method: {}.get{}", clazz.getSimpleName(), fieldName, e);
            return null;
        }
    }

    private static Optional<Field> getFieldFromCache(Class<?> clazz, String fieldName) {
        String cacheKey = clazz.getName() + "#" + fieldName;

        return FIELD_CACHE.computeIfAbsent(cacheKey, k -> {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return Optional.of(f);
            }
            catch (NoSuchFieldException e) {
                return Optional.empty();
            }
        });
    }
}
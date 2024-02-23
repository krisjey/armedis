/**
 * 
 */
package com.github.armedis.redis.info;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;

/**
 * 
 */
public class ReflectionManipulator {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionManipulator.class);

    /**
     * Private method to set field values using reflection
     * 
     * @param objects
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object objects, String fieldName, String value) {
        try {
            // FIXME Errorstats # Errorstats errorstat_ERR:count=3602
            if (objects.getClass().getSimpleName().equals("Errorstats")) {
                value = StringUtils.remove(value, "count=");
            }

            Field field = objects.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if (field.getType() == int.class) {
                field.setInt(objects, NumberUtils.toInt(value));
            }
            else if (field.getType() == long.class) {
                field.setLong(objects, NumberUtils.toLong(value));
            }
            else if (field.getType() == double.class) {
                field.setDouble(objects, NumberUtils.toDouble(value));
            }
            else if (field.getType() == float.class) {
                field.setFloat(objects, NumberUtils.toFloat(value));
            }
            else {
                field.set(objects, value);
            }
        }
        catch (NoSuchFieldException | IllegalAccessException | NumberFormatException | SecurityException e) {
            logger.error("Can not found decleared field in " + objects.getClass().getSimpleName() + " "
                    + fieldName + " " + value, e);
        }
    }

    /**
     * Private method to set field values using reflection
     * 
     * @param objects
     * @param fieldName
     * @param value
     */
    public static <T> T getFieldValue(Object objects, String fieldName) {
        T result = null;
        try {
            Field field = objects.getClass().getDeclaredField(fieldName);
            result = (T) field.get(objects);

        }
        catch (NoSuchFieldException | IllegalAccessException | NumberFormatException e) {
            logger.error("Can not found decleared field in " + objects.getClass().getSimpleName() + " "
                    + fieldName, e);
        }

        return result;
    }

    /**
     * Private method to set field values using reflection
     * 
     * @param objects
     * @param fieldName
     * @param value
     */
    public static Object getMethodInvokeResult(Object objects, String fieldName) {
        String methodName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        Object result = null;
        try {
            Method method = objects.getClass().getDeclaredMethod(methodName);
            result = method.invoke(objects);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            logger.error("Can not found decleared field in " + objects.getClass().getSimpleName() + " "
                    + fieldName, e);
        }

        return result;
    }
}

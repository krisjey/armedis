/**
 * 
 */
package com.github.armedis.redis.info;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.CaseFormat;

/**
 * 
 */
public abstract class StatsBaseVo {
    /**
     * Should know type for compare - long, int, float, double
     */
    public static final String DIFF = "diff";

    /**
     * Only numeric value - long, int, float, double
     */
    public static final String SUM = "sum";

    /**
     * Only numeric value - long, int, float, double
     */
    public static final String AVG = "avg";

    /**
     * replace Just dash
     */
    public static final String EMPTY = "empty";

    /**
     * Concat string values with comma.
     */
    public static final String CONCAT = "concat";

    /**
     * Only numeric value - long, int, float, double
     */
    public static final String MAX = "max";

    /**
     * Only numeric value - long, int, float, double
     */
    public static final String MIN = "min";

    protected String sctionContent;
    final Map<String, String> operationKeyList;

    public StatsBaseVo() {
        operationKeyList = initOperationKeyList();
    }

    /**
     * Comparison or sum operation target key/type list.
     * <pre>
     *  - sum(long, int, float) : sum of values
     *  - diff(* long, int, float)
     *  - empty(just dash)
     *  - concat(with comma)
     *  - max
     *  - min
     *  </pre>
     * @return key and operation type kv map.
     */
    public abstract Map<String, String> initOperationKeyList();

    /**
     * Comparison or sum operation target key/type list.
     * <pre>
     *  - sum(long, int, float) : sum of values
     *  - diff(* long, int, float)
     *  - empty(just dash)
     *  - concat(with comma)
     *  - max
     *  - min
     *  </pre>
     * @return key and operation type kv map.
     */
    public Map<String, String> operationKeyList() {
        return operationKeyList;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    /**
     * TODO Generic으로 처리.
     * 
     * @param <T>
     * 
     * @param content
     * @return T
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T fromString(Class<T> clazz, String content, boolean addContentSection)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        T instance = clazz.getDeclaredConstructor().newInstance();

        if (content == null) {
            return instance;
        }

        String[] lines = content.split("\r\n");

        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);

//                if(clazz == Replication.class && key.startsWith("masterReplid"))    {
//                    System.out.println(value);
//                }
                
                ReflectionManipulator.setFieldValue(instance, key, value);
            }
        }

        if (addContentSection) {
            ReflectionManipulator.setFieldValue(instance, "sctionContent", content);
        }

        return instance;
    }

    /**
     * @return the sctionContent
     */
    public String getSctionContent() {
        return sctionContent;
    }

    /**
     * @param sctionContent the sctionContent to set
     */
    @SuppressWarnings("unchecked")
    protected <T> T setSctionContent(String sctionContent) {
        this.sctionContent = sctionContent;
        return (T) this;
    }
}

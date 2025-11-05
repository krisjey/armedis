package com.github.armedis.redis.info;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RedisInfoVo 집계를 위한 Aggregator (하이브리드 방식)
 * 기존 구조를 유지하면서 리플렉션 캐싱과 타입별 최적화로 성능 개선
 */
public class RedisInfoAggregator {
    private static final Logger logger = LoggerFactory.getLogger(RedisInfoAggregator.class);

    // 메서드 캐시 (클래스명.메서드명 -> Method)
    private static final Map<String, Method> GETTER_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Method> SETTER_CACHE = new ConcurrentHashMap<>();

    /**
     * 여러 RedisInfoVo를 하나의 집계된 RedisInfoVo로 변환
     * 
     * @param redisInfos 집계할 RedisInfoVo 컬렉션
     * @return 집계된 RedisInfoVo
     */
    public static RedisInfoVo aggregate(Collection<RedisInfoVo> redisInfos) {
        if (redisInfos == null || redisInfos.isEmpty()) {
            return RedisInfoVo.emptyObject();
        }

        List<RedisInfoVo> infoList = redisInfos instanceof List
                ? (List<RedisInfoVo>) redisInfos
                : redisInfos.stream().collect(Collectors.toList());

        // 결과 객체 생성
        RedisInfoVo result = RedisInfoVo.emptyObject();

        // 각 서브 VO 집계
        aggregateSubVo(result.getServer(), infoList, RedisInfoVo::getServer);
        aggregateSubVo(result.getClients(), infoList, RedisInfoVo::getClients);
        aggregateSubVo(result.getMemory(), infoList, RedisInfoVo::getMemory);
        aggregateSubVo(result.getPersistence(), infoList, RedisInfoVo::getPersistence);
        aggregateSubVo(result.getStats(), infoList, RedisInfoVo::getStats);
        aggregateSubVo(result.getReplication(), infoList, RedisInfoVo::getReplication);
        aggregateSubVo(result.getCpu(), infoList, RedisInfoVo::getCpu);
        aggregateSubVo(result.getModules(), infoList, RedisInfoVo::getModules);
        aggregateSubVo(result.getErrorstats(), infoList, RedisInfoVo::getErrorstats);
        aggregateSubVo(result.getCluster(), infoList, RedisInfoVo::getCluster);
        result.setKeyspace(aggregateKeyspace(infoList));

        return result;
    }

    /**
     * 서브 VO 집계 (타입 안전한 인터페이스)
     */
    @FunctionalInterface
    private interface SubVoExtractor<T> {
        T extract(RedisInfoVo info);
    }

    /**
     * 서브 VO 집계 메인 로직
     */
    private static <T extends StatsBaseVo> void aggregateSubVo(
            T resultVo,
            List<RedisInfoVo> infoList,
            SubVoExtractor<T> extractor) {

        // 모든 노드의 해당 서브 VO 추출
        List<T> subVos = infoList.stream()
                .map(extractor::extract)
                .collect(Collectors.toList());

        // 연산 대상 필드 목록 가져오기
        Map<String, String> operationMap = resultVo.operationKeyList();

        // 각 필드에 대해 집계 수행 (예외는 aggregateField 내부에서 처리)
        for (Map.Entry<String, String> entry : operationMap.entrySet()) {
            String fieldName = entry.getKey();
            String operation = entry.getValue();
            aggregateField(resultVo, subVos, fieldName, operation);
        }
    }

    /**
     * 단일 필드 집계
     */
    private static <T extends StatsBaseVo> void aggregateField(
            T resultVo,
            List<T> subVos,
            String fieldName,
            String operation) {

        try {
            Class<?> voClass = resultVo.getClass();

            // Getter 메서드 가져오기 (캐시 활용)
            Method getter = getGetterMethod(voClass, fieldName);
            if (getter == null) {
                logger.debug("No getter found for field: {} in class: {}", fieldName, voClass.getSimpleName());
                return;
            }

            // 필드 타입 확인
            Class<?> returnType = getter.getReturnType();

            // 연산 수행
            Object aggregatedValue = performOperation(subVos, getter, returnType, operation);

            // Setter 메서드로 결과 설정
            if (aggregatedValue != null) {
                Method setter = getSetterMethod(voClass, fieldName, returnType);
                if (setter != null) {
                    try {
                        setter.invoke(resultVo, aggregatedValue);
                    }
                    catch (IllegalArgumentException e) {
                        // 타입 불일치 시 String으로 변환 시도
                        if (returnType == String.class && !(aggregatedValue instanceof String)) {
                            setter.invoke(resultVo, String.valueOf(aggregatedValue));
                        }
                        else if (returnType != String.class && aggregatedValue instanceof String) {
                            // String을 숫자로 변환 시도
                            Object converted = convertStringToType((String) aggregatedValue, returnType);
                            if (converted != null) {
                                setter.invoke(resultVo, converted);
                            }
                            else {
                                throw e;
                            }
                        }
                        else {
                            throw e;
                        }
                    }
                }
                else {
                    logger.debug("No setter found for field: {} in class: {}", fieldName, voClass.getSimpleName());
                }
            }
        }
        catch (Exception e) {
            logger.error("Failed to aggregate field: {} with operation: {} - {}",
                    fieldName, operation, e.getMessage(), e);
        }
    }

    /**
     * String을 특정 타입으로 변환
     */
    private static Object convertStringToType(String value, Class<?> targetType) {
        try {
            if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            }
            else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(value);
            }
            else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            }
            else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(value);
            }
        }
        catch (NumberFormatException e) {
            logger.debug("Cannot convert '{}' to {}", value, targetType.getSimpleName());
        }
        return null;
    }

    /**
     * 연산 타입에 따라 집계 수행
     */
    private static <T> Object performOperation(
            List<T> subVos,
            Method getter,
            Class<?> returnType,
            String operation) throws Exception {

        switch (operation) {
            case StatsBaseVo.SUM:
                return performSum(subVos, getter, returnType);

            case StatsBaseVo.AVG:
                return performAvg(subVos, getter, returnType);

            case StatsBaseVo.MAX:
                return performMax(subVos, getter, returnType);

            case StatsBaseVo.MIN:
                return performMin(subVos, getter, returnType);

            case StatsBaseVo.DIFF:
                return performDiff(subVos, getter, returnType);

            case StatsBaseVo.CONCAT:
                return performConcat(subVos, getter);

            case StatsBaseVo.EMPTY:
                return "-";

            default:
                logger.warn("Unknown operation: {}", operation);
                return null;
        }
    }

    /**
     * SUM 연산
     */
    private static <T> Object performSum(List<T> subVos, Method getter, Class<?> returnType) throws Exception {
        if (returnType == int.class || returnType == Integer.class) {
            long sum = 0;
            for (T vo : subVos) {
                Integer value = (Integer) getter.invoke(vo);
                if (value != null) {
                    sum += value;
                }
            }
            return (int) sum;
        }
        else if (returnType == long.class || returnType == Long.class) {
            long sum = 0;
            for (T vo : subVos) {
                Long value = (Long) getter.invoke(vo);
                if (value != null) {
                    sum += value;
                }
            }
            return sum;
        }
        else if (returnType == double.class || returnType == Double.class) {
            double sum = 0.0;
            for (T vo : subVos) {
                Double value = (Double) getter.invoke(vo);
                if (value != null) {
                    sum += value;
                }
            }
            return sum;
        }
        else if (returnType == float.class || returnType == Float.class) {
            float sum = 0.0f;
            for (T vo : subVos) {
                Float value = (Float) getter.invoke(vo);
                if (value != null) {
                    sum += value;
                }
            }
            return sum;
        }
        return null;
    }

    /**
     * AVG 연산
     */
    private static <T> Object performAvg(List<T> subVos, Method getter, Class<?> returnType) throws Exception {
        Object sum = performSum(subVos, getter, returnType);
        if (sum == null || subVos.isEmpty()) {
            return null;
        }

        int count = subVos.size();
        if (sum instanceof Integer) {
            return ((Integer) sum) / count;
        }
        else if (sum instanceof Long) {
            return ((Long) sum) / count;
        }
        else if (sum instanceof Double) {
            return ((Double) sum) / count;
        }
        else if (sum instanceof Float) {
            return ((Float) sum) / count;
        }
        return null;
    }

    /**
     * MAX 연산
     */
    private static <T> Object performMax(List<T> subVos, Method getter, Class<?> returnType) throws Exception {
        if (returnType == int.class || returnType == Integer.class) {
            int max = Integer.MIN_VALUE;
            boolean hasValue = false;
            for (T vo : subVos) {
                Integer value = (Integer) getter.invoke(vo);
                if (value != null) {
                    max = Math.max(max, value);
                    hasValue = true;
                }
            }
            return hasValue ? max : 0;
        }
        else if (returnType == long.class || returnType == Long.class) {
            long max = Long.MIN_VALUE;
            boolean hasValue = false;
            for (T vo : subVos) {
                Long value = (Long) getter.invoke(vo);
                if (value != null) {
                    max = Math.max(max, value);
                    hasValue = true;
                }
            }
            return hasValue ? max : 0L;
        }
        else if (returnType == double.class || returnType == Double.class) {
            double max = Double.MIN_VALUE;
            boolean hasValue = false;
            for (T vo : subVos) {
                Double value = (Double) getter.invoke(vo);
                if (value != null) {
                    max = Math.max(max, value);
                    hasValue = true;
                }
            }
            return hasValue ? max : 0.0;
        }
        return null;
    }

    /**
     * MIN 연산
     */
    private static <T> Object performMin(List<T> subVos, Method getter, Class<?> returnType) throws Exception {
        if (returnType == int.class || returnType == Integer.class) {
            int min = Integer.MAX_VALUE;
            boolean hasValue = false;
            for (T vo : subVos) {
                Integer value = (Integer) getter.invoke(vo);
                if (value != null) {
                    min = Math.min(min, value);
                    hasValue = true;
                }
            }
            return hasValue ? min : 0;
        }
        else if (returnType == long.class || returnType == Long.class) {
            long min = Long.MAX_VALUE;
            boolean hasValue = false;
            for (T vo : subVos) {
                Long value = (Long) getter.invoke(vo);
                if (value != null) {
                    min = Math.min(min, value);
                    hasValue = true;
                }
            }
            return hasValue ? min : 0L;
        }
        else if (returnType == double.class || returnType == Double.class) {
            double min = Double.MAX_VALUE;
            boolean hasValue = false;
            for (T vo : subVos) {
                Double value = (Double) getter.invoke(vo);
                if (value != null) {
                    min = Math.min(min, value);
                    hasValue = true;
                }
            }
            return hasValue ? min : 0.0;
        }
        return null;
    }

    /**
     * DIFF 연산 (값이 다르면 표시)
     */
    private static <T> Object performDiff(List<T> subVos, Method getter, Class<?> returnType) throws Exception {
        if (subVos.isEmpty()) {
            return returnType == String.class ? "" : 0;
        }

        Object first = getter.invoke(subVos.get(0));
        boolean allSame = true;

        for (T vo : subVos) {
            Object value = getter.invoke(vo);
            if (value == null && first == null) {
                continue;
            }
            if (value == null || !value.equals(first)) {
                allSame = false;
                break;
            }
        }

        if (allSame) {
            return first;
        }

        // 값이 다른 경우 처리
        Object last = getter.invoke(subVos.get(subVos.size() - 1));

        if (returnType == String.class) {
            return last != null ? last.toString() + "(*)" : "(*)";
        }
        else if (returnType == int.class || returnType == Integer.class) {
            return last != null ? -(Integer) last : 0;
        }
        else if (returnType == long.class || returnType == Long.class) {
            return last != null ? -(Long) last : 0L;
        }
        else if (returnType == double.class || returnType == Double.class) {
            return last != null ? -(Double) last : 0.0;
        }

        return last;
    }

    /**
     * CONCAT 연산 (쉼표로 연결)
     */
    private static <T> String performConcat(List<T> subVos, Method getter) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (T vo : subVos) {
            try {
                Object value = getter.invoke(vo);
                if (value != null) {
                    String strValue = String.valueOf(value);
                    if (!strValue.isEmpty() && !strValue.equals("0")) {
                        if (!first) {
                            sb.append(", ");
                        }
                        sb.append(strValue);
                        first = false;
                    }
                }
            }
            catch (Exception e) {
                logger.debug("Error invoking getter in CONCAT operation: {}", e.getMessage());
            }
        }

        return sb.toString();
    }

    /**
     * Getter 메서드 가져오기 (캐시 활용)
     */
    private static Method getGetterMethod(Class<?> clazz, String fieldName) {
        String cacheKey = clazz.getName() + ".get" + capitalize(fieldName);

        return GETTER_CACHE.computeIfAbsent(cacheKey, key -> {
            try {
                String methodName = "get" + capitalize(fieldName);
                return clazz.getMethod(methodName);
            }
            catch (NoSuchMethodException e) {
                logger.debug("Getter not found: {}", key);
                return null;
            }
        });
    }

    /**
     * Setter 메서드 가져오기 (캐시 활용)
     */
    private static Method getSetterMethod(Class<?> clazz, String fieldName, Class<?> paramType) {
        String cacheKey = clazz.getName() + ".set" + capitalize(fieldName) + "." + paramType.getName();

        return SETTER_CACHE.computeIfAbsent(cacheKey, key -> {
            try {
                String methodName = "set" + capitalize(fieldName);
                return clazz.getMethod(methodName, paramType);
            }
            catch (NoSuchMethodException e) {
                logger.debug("Setter not found: {}", key);
                return null;
            }
        });
    }

    /**
     * Keyspace 정보 집계
     */
    private static Map<Integer, Keyspace> aggregateKeyspace(List<RedisInfoVo> infos) {
        Map<Integer, Keyspace> result = new java.util.TreeMap<>();

        // 모든 노드에서 사용 중인 DB 번호 수집
        Set<Integer> allDbNos = new java.util.HashSet<>();
        for (RedisInfoVo info : infos) {
            if (info.getKeyspace() != null) {
                allDbNos.addAll(info.getKeyspace().keySet());
            }
        }

        // 각 DB 번호별로 집계
        for (Integer dbNo : allDbNos) {
            List<Keyspace> dbKeyspaces = new ArrayList<>();

            // 해당 DB를 가진 노드들의 Keyspace 수집
            for (RedisInfoVo info : infos) {
                if (info.getKeyspace() != null && info.getKeyspace().containsKey(dbNo)) {
                    dbKeyspaces.add(info.getKeyspace().get(dbNo));
                }
            }

            if (!dbKeyspaces.isEmpty()) {
                Keyspace aggregated = aggregateKeyspaceForDb(dbNo, dbKeyspaces);
                result.put(dbNo, aggregated);
            }
        }

        return result;
    }

    /**
     * 특정 DB의 Keyspace 집계
     */
    private static Keyspace aggregateKeyspaceForDb(int dbNo, List<Keyspace> keyspaces) {
        // Keyspace는 private 생성자를 가질 수 있으므로 리플렉션 사용
        try {
            Keyspace result = new Keyspace();

            // DB 번호 설정
            result.setNo(dbNo);

            // keys 합계
            int totalKeys = 0;
            for (Keyspace ks : keyspaces) {
                totalKeys += ks.getKeys();
            }
            result.setKeys(totalKeys);

            // expires 합계
            int totalExpires = 0;
            for (Keyspace ks : keyspaces) {
                totalExpires += ks.getExpires();
            }
            result.setExpires(totalExpires);

            // avgTtl 평균
            int totalAvgTtl = 0;
            int count = 0;
            for (Keyspace ks : keyspaces) {
                if (ks.getAvgTtl() > 0) {
                    totalAvgTtl += ks.getAvgTtl();
                    count++;
                }
            }
            result.setAvgTtl(count > 0 ? totalAvgTtl / count : 0);

            return result;
        }
        catch (Exception e) {
            logger.error("Failed to aggregate keyspace for DB {}", dbNo, e);
            return null;
        }
    }

    /**
     * 첫 글자를 대문자로 변환
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
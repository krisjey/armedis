/**
 * 
 */
package com.github.armedis.http.service.management.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class AllowedConfigCommands {

    // TODO add HyperLogLog command
//    PFADD("pfadd"),
//    PFCOUNT("pfcount"),
//    PFMERGE("pfmerge"),

    // TODO add GEO command
//    GEOADD("geoadd"),
//    GEODIST("geodist"),
//    GEOHASH("geohash"),
//    GEOPOS("geopos"),
//    GEORADIUS("georadius"),
//    GEORADIUSBYMEMBER("georadiusbymember"),

    // TODO PUB/SUB command by GRPC/Thrift
    // TODO Streams command by GRPC/Thrift

    // TODO Should be added support version of every Redis commands. and then check current redis version.

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static boolean initialized;

    private AllowedConfigCommands() {
    }

    // ==== DataType & Category ====
    public enum DataType {
        STRING,
        INTEGER,
        BOOLEAN,
        FLOAT,
        MEMORYUNIT,
        DURATION,
        DROPDOWN
    }

    public enum Category {
        Memory,
        Network,
        Persistence,
        Client,
        Logging,
        Performance,
        Security,
        Replication,
        Cluster,
        Scripting,
        Modules,
        General
    }

    public static final class ConfigCommand {
        private final String key;
        private final Category category;
        private final String description;
        private final DataType dataType;
        private final List<String> options;
        private final String parseKey;
        @JsonIgnore
        private final Predicate<String> validator;
        @JsonIgnore
        private final Function<String, String> normalizer;
        private volatile String currentValue;

        private ConfigCommand(Builder builder) {
            this.key = Objects.requireNonNull(builder.key);
            this.category = Objects.requireNonNull(builder.category);
            this.description = Objects.requireNonNull(builder.description);
            this.dataType = Objects.requireNonNull(builder.dataType);
            this.options = List.copyOf(builder.options);
            this.parseKey = Objects.requireNonNull(builder.parseKey);
            this.validator = builder.validator != null ? builder.validator : (v -> true);
            this.normalizer = builder.normalizer != null ? builder.normalizer : Function.identity();
            this.currentValue = builder.currentValue;
        }

        @JsonProperty("key")
        public String getKey() {
            return key;
        }

        @JsonProperty("currentValue")
        public String getCurrentValue() {
            return currentValue;
        }

        @JsonProperty("category")
        public String getCategory() {
            return category.name();
        }

        @JsonProperty("description")
        public String getDescription() {
            return description;
        }

        @JsonProperty("dataType")
        public String getDataType() {
            return dataType.name().toLowerCase();
        }

        @JsonProperty("options")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public List<String> getOptions() {
            return options.isEmpty() ? null : options;
        }

        @JsonProperty("parseKey")
        public String getParseKey() {
            return parseKey;
        }

        public void setCurrentValueFromDB(String value) {
            if (value == null) {
                throw new IllegalArgumentException("value cannot be null");
            }

            this.currentValue = normalizer.apply(value);
        }

        public void setCurrentValue(String value) {
            if (value == null) {
                throw new IllegalArgumentException("value cannot be null");
            }

            if (!validator.test(value)) {
                String base = "Invalid value for '" + key + "': " + value;
                if (dataType == DataType.DROPDOWN && !options.isEmpty()) {
                    throw new IllegalArgumentException(base + " (allowed: " + options + ")");
                }
                throw new IllegalArgumentException(base);
            }
            this.currentValue = normalizer.apply(value);
        }

        public boolean isValid(String value) {
            return value != null && validator.test(value);
        }

        public String parseValue(JsonNode jsonNode) {
            if (StringUtils.trimToNull(parseKey) == null) {
                return jsonNode.asText("");
            }
            else {
                return jsonNode.get(parseKey).asText("");
            }
        }

        public static Builder builder(String key) {
            return new Builder(key);
        }

        public static final class Builder {
            private final String key;
            private Category category = Category.General;
            private String description = "";
            private DataType dataType = DataType.STRING;
            private List<String> options = new ArrayList<>();
            private String parseKey = "";
            private Predicate<String> validator;
            private Function<String, String> normalizer;
            private String currentValue = "";

            private Builder(String key) {
                this.key = key;
            }

            public Builder category(Category c) {
                this.category = c;
                return this;
            }

            public Builder description(String d) {
                this.description = d;
                return this;
            }

            public Builder dataType(DataType t) {
                this.dataType = t;
                return this;
            }

            public Builder options(List<String> opts) {
                this.options = new ArrayList<>(opts);
                return this;
            }

            public Builder parseKey(String parseKey) {
                this.parseKey = parseKey;
                return this;
            }

            public Builder validator(Predicate<String> v) {
                this.validator = v;
                return this;
            }

            public Builder normalizer(Function<String, String> n) {
                this.normalizer = n;
                return this;
            }

            public Builder currentValue(String v) {
                this.currentValue = v;
                return this;
            }

            public ConfigCommand build() {
                return new ConfigCommand(this);
            }
        }
    }

    // ==== JSON 출력용 래퍼 클래스 ====
    public static class ConfigCommandsJson {
        @JsonProperty("key-description")
        private final String keyDescription = "key, currentValue, category, description, dataType, options";

        @JsonProperty("dataType-description")
        private final String dataTypeDescription = "number, text, dropdown, memoryunit";

        @JsonProperty("configKeys")
        private final List<ConfigCommand> configKeys;

        public ConfigCommandsJson(Collection<ConfigCommand> commands) {
            this.configKeys = new ArrayList<>(commands);
        }

        public String getKeyDescription() {
            return keyDescription;
        }

        public String getDataTypeDescription() {
            return dataTypeDescription;
        }

        public List<ConfigCommand> getConfigKeys() {
            return configKeys;
        }
    }

    // ==== 표준 Validator / Normalizer 유틸 ====
    private static Predicate<String> memoryUnitValidator() {
        return v -> v != null && v.trim().matches("(?i)^[0-9]+(b|k|kb|m|mb|g|gb)?$");
    }

    private static Function<String, String> memoryUnitNormalizer() {
        return v -> v.trim().toLowerCase(Locale.ROOT);
    }

    private static Predicate<String> durationValidator() {
        return v -> v != null && v.trim().matches("(?i)^[0-9]+(ms|s|m|h|d)?$");
    }

    private static Function<String, String> lowerNormalizer() {
        return v -> v.trim().toLowerCase(Locale.ROOT);
    }

    private static Predicate<String> integerValidator() {
        return v -> v != null && v.matches("^-?\\d+$");
    }

    private static Predicate<String> floatValidator() {
        return v -> v != null && v.matches("^-?\\d+(\\.\\d+)?$");
    }

    private static Predicate<String> booleanValidator() {
        return v -> v != null && v.matches("(?i)^(true|false|yes|no|1|0)$");
    }

    private static Predicate<String> dropdownValidator(List<String> options) {
        Set<String> s = new HashSet<>();
        for (String o : options)
            s.add(o.toLowerCase(Locale.ROOT));
        return v -> v != null && s.contains(v.trim().toLowerCase(Locale.ROOT));
    }

    public static boolean contains(String key) {
        return COMMANDS.containsKey(norm(key));
    }

    private static String norm(String key) {
        return Objects.requireNonNull(key, "key").trim().toLowerCase(Locale.ROOT);
    }

    // ==== 사전 정의된 허용 명령들 ====
    private static final Map<String, ConfigCommand> COMMANDS = initAllowedCommands();

    private static Map<String, ConfigCommand> initAllowedCommands() {
        Map<String, ConfigCommand> m = new LinkedHashMap<>();

        m.put("maxmemory",
                ConfigCommand.builder("maxmemory")
                        .category(Category.Memory)
                        .description("최대 메모리 사용량 (바이트)")
                        .dataType(DataType.MEMORYUNIT)
                        .parseKey("maxmemory")
                        .validator(memoryUnitValidator())
                        .normalizer(memoryUnitNormalizer())
                        .build());

        List<String> policyOpts = List.of("noeviction", "allkeys-lru", "volatile-lru", "volatile-random", "allkeys-random", "volatile-ttl");
        m.put("maxmemory-policy",
                ConfigCommand.builder("maxmemory-policy")
                        .category(Category.Memory)
                        .description("메모리 초과 시 키 삭제 정책")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("maxmemory-policy")
                        .options(policyOpts)
                        .validator(dropdownValidator(policyOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        List<String> memorySampleOpts = List.of("3", "5", "7", "10");
        m.put("maxmemory-samples",
                ConfigCommand.builder("maxmemory-samples")
                        .category(Category.Memory)
                        .description("메모리 초과 시 키 삭제 정책(대상 조회 수)")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("maxmemory-samples")
                        .options(memorySampleOpts)
                        .validator(dropdownValidator(memorySampleOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        // TODO allocator_frag_ratio값, allocator_frag_bytes 값 출력 필요.
        List<String> defragOpts = List.of("yes", "no");
        m.put("activedefrag",
                ConfigCommand.builder("activedefrag")
                        .category(Category.Memory)
                        .description("동적 메모리 파편화 제거")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("activedefrag")
                        .options(defragOpts)
                        .validator(dropdownValidator(defragOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        // active-defrag-threshold-upper best efforts
        List<String> thresholdUpperOpts = List.of("100", "80", "70");
        m.put("active-defrag-threshold-upper",
                ConfigCommand.builder("active-defrag-threshold-upper")
                        .category(Category.Memory)
                        .description("동적 메모리 파편화 제거-최대성능시작 threshold")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("active-defrag-threshold-upper")
                        .options(thresholdUpperOpts)
                        .validator(dropdownValidator(thresholdUpperOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        // active-defrag-threshold-lower stop
        List<String> thresholdLowerOpts = List.of("100", "80", "70");
        m.put("active-defrag-threshold-lower",
                ConfigCommand.builder("active-defrag-threshold-lower")
                        .category(Category.Memory)
                        .description("동적 메모리 파편화 제거-중지 threshold")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("active-defrag-threshold-lower")
                        .options(thresholdLowerOpts)
                        .validator(dropdownValidator(thresholdLowerOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        // active-defrag-ignore-bytes flag
        m.put("active-defrag-ignore-bytes",
                ConfigCommand.builder("active-defrag-ignore-bytes")
                        .category(Category.Memory)
                        .description("동적 메모리 파편화 제거-시작 최소값")
                        .dataType(DataType.MEMORYUNIT)
                        .parseKey("active-defrag-ignore-bytes")
                        .validator(memoryUnitValidator())
                        .normalizer(memoryUnitNormalizer())
                        .build());

        List<String> hzOpts = List.of("10", "20", "30", "50");
        m.put("hz",
                ConfigCommand.builder("hz")
                        .category(Category.Performance)
                        .description("Background job frequency(expire, activedefrag, client close...)")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("hz")
                        .options(hzOpts)
                        .validator(dropdownValidator(hzOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        m.put("maxclients",
                ConfigCommand.builder("maxclients")
                        .category(Category.Client)
                        .description("동시 접속 가능한 최대 클라이언트 수")
                        .dataType(DataType.INTEGER)
                        .parseKey("maxclients")
                        .validator(integerValidator())
                        .build());

        m.put("timeout",
                ConfigCommand.builder("timeout")
                        .category(Category.Client)
                        .description("유휴 클라이언트 연결 종료 시간(초)")
                        .dataType(DataType.INTEGER)
                        .parseKey("timeout")
                        .validator(integerValidator())
                        .build());

        m.put("save",
                ConfigCommand.builder("save")
                        .category(Category.Persistence)
                        .description("RDB 스냅샷 주기(동기)")
                        .dataType(DataType.STRING)
                        .parseKey("save")
                        .build());

        List<String> fsyncOpts = List.of("always", "everysec", "no");
        m.put("appendfsync",
                ConfigCommand.builder("appendfsync")
                        .category(Category.Persistence)
                        .description("AOF 동기화 정책")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("appendfsync")
                        .options(fsyncOpts)
                        .validator(dropdownValidator(fsyncOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        m.put("tcp-keepalive",
                ConfigCommand.builder("tcp-keepalive")
                        .category(Category.Network)
                        .description("TCP keepalive 시간(초)")
                        .dataType(DataType.INTEGER)
                        .parseKey("tcp-keepalive")
                        .validator(integerValidator())
                        .build());

        List<String> levelOpts = List.of("debug", "verbose", "notice", "warning", "nothing");
        m.put("loglevel",
                ConfigCommand.builder("loglevel")
                        .category(Category.Logging)
                        .description("로그 레벨")
                        .dataType(DataType.DROPDOWN)
                        .parseKey("loglevel")
                        .options(levelOpts)
                        .validator(dropdownValidator(levelOpts))
                        .normalizer(lowerNormalizer())
                        .build());

        m.put("slowlog-log-slower-than",
                ConfigCommand.builder("slowlog-log-slower-than")
                        .category(Category.Logging)
                        .description("slow log detect threshold")
                        .dataType(DataType.INTEGER)
                        .parseKey("slowlog-log-slower-than")
                        .validator(integerValidator())
                        .build());

        m.put("cluster-node-timeout",
                ConfigCommand.builder("cluster-node-timeout")
                        .category(Category.Cluster)
                        .description("클러스터 노드 타임아웃(밀리초)")
                        .dataType(DataType.INTEGER)
                        .parseKey("cluster-node-timeout")
                        .validator(integerValidator())
                        .build());

        return Collections.unmodifiableMap(m);
    }

    public static ConfigCommand get(String key) {
        String normalizedKey = norm(key);
        ConfigCommand cmd = COMMANDS.get(normalizedKey);
        if (cmd == null) {
            throw new NoSuchElementException("Unknown config: " + key);
        }

        return cmd;
    }

    public static Collection<ConfigCommand> all() {
        return COMMANDS.values();
    }

    public static Set<String> keys() {
        return COMMANDS.keySet();
    }

    /**
     * 전체 config commands를 JSON 문자열로 반환
     */
    public static String toJsonString() throws JsonProcessingException {
        ConfigCommandsJson wrapper = new ConfigCommandsJson(COMMANDS.values());
        return OBJECT_MAPPER.writeValueAsString(wrapper);
    }

    /**
     * 특정 config command를 JSON 문자열로 반환
     */
    public static String toJson(String key) throws JsonProcessingException {
        ConfigCommand cmd = get(key);
        return OBJECT_MAPPER.writeValueAsString(cmd);
    }

    /**
     * 
     */
    public static void initialized() {
        initialized = true;
    }

    /**
     * 
     */
    public static boolean isInitialized() {
        return initialized;
    }
}

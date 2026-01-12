package com.github.armedis.config;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.connection.RedisServerDetector;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;

@Component
public class RedisConfigManager {

    // TODO Multi node commander
    private final RedisServerDetector redisServerDetector;
    private final Map<String, RedisTemplate<String, String>> redisTemplateByNodes = new ConcurrentHashMap<>();

    public RedisConfigManager(RedisServerDetector redisServerDetector) {
        this.redisServerDetector = redisServerDetector;
    }

    /**
     * GET 명령 수행시 노드들의 값이 다르면 (+) 기호 표시.
     * @param configKey
     * @return
     */
    public String getConfigValue(String configKey) {
        Set<RedisNode> currentNodes = redisServerDetector.getAllNodes();

        // 1. 현재 노드 목록과 캐시된 Pool 동기화
        syncNodeTemplates(currentNodes);

        Set<String> values = new HashSet<>();
        for (RedisNode node : currentNodes) {
            String nodeKey = toKey(node);
            RedisTemplate<String, String> template = redisTemplateByNodes.get(nodeKey);
            try {
                String value = template.execute((RedisConnection conn) -> {
                    Properties prop = conn.serverCommands().getConfig(configKey);
                    return (String) prop.get(configKey);
                });
                values.add(value);
            }
            catch (RedisConnectionFailureException e) {
                // 연결 실패 시 해당 Pool 제거 → 다음 호출 시 재생성
                removeTemplate(nodeKey);
            }
        }

        if (values.isEmpty()) {
            throw new RedisConnectionFailureException("All nodes unreachable");
        }

        String result = values.iterator().next();
        return values.size() > 1 ? result + "(+)" : result;
    }

    /**
     *  SET 명령수행시 롤백 처리 : 롤백을 위해 이전 값 저장
     * @param configKey
     * @param configValue
     * @return redis command success flag
     */
    public boolean setConfigValue(String configKey, String configValue) {
        Set<RedisNode> currentNodes = redisServerDetector.getAllNodes();
        syncNodeTemplates(currentNodes);

        // 1. 백업: 각 노드의 현재 값 저장
        Map<String, String> backupValues = new ConcurrentHashMap<>();
        for (RedisNode node : currentNodes) {
            String nodeKey = toKey(node);
            RedisTemplate<String, String> template = redisTemplateByNodes.get(nodeKey);
            try {
                String currentValue = template.execute((RedisConnection conn) -> {
                    Properties prop = conn.serverCommands().getConfig(configKey);
                    return (String) prop.get(configKey);
                });
                backupValues.put(nodeKey, currentValue);
            }
            catch (Exception e) {
                // 백업 실패 시 즉시 중단
                e.printStackTrace();
                return false;
            }
        }

        // 2. 설정 변경 시도
        Set<String> successNodes = new HashSet<>();
        for (RedisNode node : currentNodes) {
            String nodeKey = toKey(node);
            RedisTemplate<String, String> template = redisTemplateByNodes.get(nodeKey);
            try {
                template.execute((RedisConnection conn) -> {
                    conn.serverCommands().setConfig(configKey, configValue);
                    return null;
                });
                successNodes.add(nodeKey);
            }
            catch (Exception e) {
                e.printStackTrace();
                // 3. 실패 시 롤백
                rollback(configKey, backupValues, successNodes);
                return false;
            }
        }

        // 4. 모든 노드 성공
        return true;
    }

    private void rollback(String configKey, Map<String, String> backupValues, Set<String> successNodes) {
        for (String nodeKey : successNodes) {
            RedisTemplate<String, String> template = redisTemplateByNodes.get(nodeKey);
            String originalValue = backupValues.get(nodeKey);
            try {
                template.execute((RedisConnection conn) -> {
                    conn.serverCommands().setConfig(configKey, originalValue);
                    return null;
                });
            }
            catch (Exception e) {
                // 롤백 실패 시 로깅만 (이미 실패 상태)
                System.err.println("Rollback failed for node: " + nodeKey);
            }
        }
    }

    private void syncNodeTemplates(Set<RedisNode> currentNodes) {
        Set<String> currentKeys = currentNodes.stream()
                .map(this::toKey)
                .collect(Collectors.toSet());

        // 제거된 노드의 Pool 정리
        redisTemplateByNodes.keySet().removeIf(key -> {
            if (!currentKeys.contains(key)) {
                destroyTemplate(redisTemplateByNodes.get(key));
                return true;
            }
            return false;
        });

        // 신규 노드의 Pool 생성
        for (RedisNode node : currentNodes) {
            redisTemplateByNodes.computeIfAbsent(toKey(node), k -> createTemplate(node));
        }
    }

    private String toKey(RedisNode node) {
        return node.getHost() + ":" + node.getPort();
    }

    private void removeTemplate(String key) {
        RedisTemplate<String, String> template = redisTemplateByNodes.remove(key);
        if (template != null) {
            destroyTemplate(template);
        }
    }

    private void destroyTemplate(RedisTemplate<String, String> template) {
        try {
            ((LettuceConnectionFactory) template.getConnectionFactory()).destroy();
        }
        catch (Exception ignored) {
        }
    }

    /**
     * Connection Pool 설정 생성
     */
    @SuppressWarnings("rawtypes")
    private GenericObjectPoolConfig buildPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(1);
        poolConfig.setMaxIdle(1);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxWait(Duration.ofMillis(1500));
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);

        return poolConfig;
    }

    @SuppressWarnings("unchecked")
    private RedisTemplate<String, String> createTemplate(RedisNode node) {
     // 1) Lettuce reconnect/timeout 동작 정의
        ClientOptions clientOptions = ClientOptions.builder()
                .autoReconnect(true) // 핵심: 자동 재연결
                // Admin 성격이면 보통 "즉시 실패"가 운영상 예측 가능
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                // 커넥션 타임아웃/커맨드 타임아웃을 명확히 (무한 대기 방지)
                .timeoutOptions(TimeoutOptions.enabled())
                .socketOptions(SocketOptions.builder()
                        .connectTimeout(Duration.ofMillis(200))
                        .build())
                .build();
        
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(buildPoolConfig())
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofMillis(200))   // 필요 시 조정
                .shutdownTimeout(Duration.ofMillis(500))   // Admin이면 짧게
                .build();

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(node.getHost(), node.getPort());
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
        factory.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    @PreDestroy
    public void cleanup() {
        redisTemplateByNodes.values().forEach(this::destroyTemplate);
        redisTemplateByNodes.clear();
    }
}
package com.github.armedis.config;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.github.armedis.redis.RedisNode;
import com.github.armedis.redis.connection.RedisServerDetector;

@Component
public class NodeConfigChecker {

    private final RedisServerDetector redisServerDetector;
    private final Map<String, RedisTemplate<String, String>> nodeTemplates = new ConcurrentHashMap<>();

    public NodeConfigChecker(RedisServerDetector redisServerDetector) {
        this.redisServerDetector = redisServerDetector;
    }

    public String getConfigValue(String configKey) {
        Set<RedisNode> currentNodes = redisServerDetector.getAllNodes();

        // 1. 현재 노드 목록과 캐시된 Pool 동기화
        syncNodeTemplates(currentNodes);

        Set<String> values = new HashSet<>();
        for (RedisNode node : currentNodes) {
            String nodeKey = toKey(node);
            RedisTemplate<String, String> template = nodeTemplates.get(nodeKey);
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

    private void syncNodeTemplates(Set<RedisNode> currentNodes) {
        Set<String> currentKeys = currentNodes.stream()
                .map(this::toKey)
                .collect(Collectors.toSet());

        // 제거된 노드의 Pool 정리
        nodeTemplates.keySet().removeIf(key -> {
            if (!currentKeys.contains(key)) {
                destroyTemplate(nodeTemplates.get(key));
                return true;
            }
            return false;
        });

        // 신규 노드의 Pool 생성
        for (RedisNode node : currentNodes) {
            nodeTemplates.computeIfAbsent(toKey(node), k -> createTemplate(node));
        }
    }

    private String toKey(RedisNode node) {
        return node.getHost() + ":" + node.getPort();
    }

    private void removeTemplate(String key) {
        RedisTemplate<String, String> template = nodeTemplates.remove(key);
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

    private RedisTemplate<String, String> createTemplate(RedisNode node) {
        String key = node.getHost() + ":" + node.getPort();
        return nodeTemplates.computeIfAbsent(key, k -> {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(node.getHost(), node.getPort());
            LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
            factory.afterPropertiesSet();
            
            RedisTemplate<String, String> template = new RedisTemplate<>();
            template.setConnectionFactory(factory);
            template.afterPropertiesSet();
            return template;
        });
    }

    @PreDestroy
    public void cleanup() {
        nodeTemplates.values().forEach(this::destroyTemplate);
        nodeTemplates.clear();
    }
}
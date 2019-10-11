
package com.github.armedis.redis.key;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.StringUtils;

public class RedisKeyMaker implements KeyMaker {
    private String key;

    private String namespace;

    /**
     * Constructor of RedisKeyMaker with namespace and actual key
     * @param namespace
     * @param key
     */
    public RedisKeyMaker(String namespace, String key) {
        this.namespace = requireNonNull(namespace, "namespace");
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            StringBuilder builder = new StringBuilder(64);

            // README /v1/namespace/get/key
            builder.append(namespace);
            builder.append(KeyMaker.DELIMITER);
            builder.append(key);

            this.key = builder.toString();
        }

        return this.key;
    }

    /**
     *
     * If made key is <b>cache::hello</b> then will return <b>hello</b>
     *
     * <pre>
     * RedisKeyMaker.parseRequestKeyWithNamespace("storage::serarch::hello-event") = "serarch::hello-event"
     * </pre>
     *
     * @param madeKey
     * @return
     */
    public static final String extractKey(String madeKey) {
        return StringUtils.substringAfter(madeKey, KeyMaker.DELIMITER);
    }
}
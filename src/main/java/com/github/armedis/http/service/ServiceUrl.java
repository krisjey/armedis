
package com.github.armedis.http.service;

/**
     * EndPoint of API URL Constants.
     * 
     * @author krisjey
     *
     */
public final class ServiceUrl {
    /**
     * dummy api endpoint
     */
    public static final String HELLO_WORLD_GET = "/v1/hello/world/{userId}";

    /**
     * Server active check.
     */
    public static final String RUOK = "/v1/ruok";

    /**
     * Redis status check
     */
    public static final String REDIS_STATS = "/v1/redis/stats";

    /**
     * Current memory status
     */
    public static final String FREE_MEMORY_GET = "/v1/free";

    /**
     * request url for version 1.0 
     */
    public static final String TEST_COMMAND = "/v1/{command}";
}
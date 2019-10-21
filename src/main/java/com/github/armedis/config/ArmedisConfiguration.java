
package com.github.armedis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * config.redis.seed=192.168.56.104:7003 server.service.port=8088
 * 
 * @author krisjey
 *
 */
@Component
@ConfigurationProperties
//@ConfigurationProperties("")	// prefix 지정.
public class ArmedisConfiguration {
    @Value("${config.redis.seed}")
    private String redisSeedAddress;

    @Value("${server.service.port}")
    private int servicePort;

    @Value("${server.service.instanceCount}")
    private int instanceCount;

    public String getRedisSeedAddress() {
        return redisSeedAddress;
    }

    public void setRedisSeedAddress(String redisAddressSeed) {
        this.redisSeedAddress = redisAddressSeed;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

}

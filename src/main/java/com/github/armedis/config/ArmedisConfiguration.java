
package com.github.armedis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class of Armedis, This class using application.properties file.
 * 
 * @author krisjey
 *
 */
@Component
@ConfigurationProperties
public class ArmedisConfiguration {
    @Value("${config.redis.seed}")
    private String redisSeedAddress;

    @Value("${server.service.port}")
    private int servicePort;

    @Value("${server.service.instanceCount}")
    private int instanceCount;

    @Value("${server.config.stat.addContentSection}")
    private boolean addContentSection;

    @Value("${server.config.stat.enabled}")
    private boolean statEnabled;

    @Value("${server.config.stat.logging.enableed}")
    private boolean loggingEnabled;

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

    /**
     * @return the addContentSection
     */
    public boolean isAddContentSection() {
        return addContentSection;
    }

    /**
     * @param addContentSection the addContentSection to set
     */
    public void setAddContentSection(boolean addContentSection) {
        this.addContentSection = addContentSection;
    }


    /**
     * @return the statEnabled
     */
    public boolean isStatEnabled() {
        return statEnabled;
    }

    /**
     * @param statEnabled the statEnabled to set
     */
    public void setStatEnabled(boolean statEnabled) {
        this.statEnabled = statEnabled;
    }

    /**
     * @return the loggingEnabled
     */
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * @param loggingEnabled the loggingEnabled to set
     */
    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }
}

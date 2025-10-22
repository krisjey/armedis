
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

    @Value("${server.management.loginId}")
    private String loginId;

    @Value("${server.management.loginPassword}")
    private String loginPassword;

    public String getRedisSeedAddress() {
        return redisSeedAddress;
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

    /**
     * @return the addContentSection
     */
    public boolean isAddContentSection() {
        return addContentSection;
    }

    /**
     * @return the statEnabled
     */
    public boolean isStatEnabled() {
        return statEnabled;
    }

    /**
     * @return the loggingEnabled
     */
    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @return the loginPassword
     */
    public String getLoginPassword() {
        return loginPassword;
    }
}

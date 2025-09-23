
package com.github.armedis;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.armedis.config.ApplicationPropertiesLoader;
import com.linecorp.armeria.spring.ArmeriaSettings;

/**
 * Armidis is a Redis Rest/gRPC API server based on Armeria F/W.
 * 
 * API Server Main class
 * 
 * @TODO add prometheus exportor - prometheus exporter armeria example
 *       https://github.com/heowc/armeria-example/blob/master/prometheus-metrics/src/main/java/com/example/PrometheusMetricsApplication.java
 *       https://jupiny.com/2021/01/03/armeria-metric-monitoring-by-prometheus/
 * @TODO asdf
 * @FIXME Speed up Spring Boot startup time!! EnableAutoConfiguration(exclude =
 *        {ActiveMQAutoConfiguration.class, xxx.class,})
 * @author krisjey
 */
@SpringBootApplication
public class ArmedisServer implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ArmeriaSettings settings;

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        SpringApplication application = new SpringApplication(ArmedisServer.class);

        // load application properties
        application.setDefaultProperties(ApplicationPropertiesLoader.getProperties());
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);

        System.out.println(
                "Spring framework is loaded! (main) " + (System.currentTimeMillis() - startTime) + "ms elapsed");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String loadedMessage = "Spring application loaded!(ArmedisServer.run())";
        logger.info(loadedMessage + settings.getPorts());
        logger.info("Stats url is /index.htm");

        // TODO print connected redis config.
        
        System.out.println(loadedMessage);
    }
}

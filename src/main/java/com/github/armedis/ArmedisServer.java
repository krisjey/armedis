
package com.github.armedis;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.armedis.config.ApplicationPropertiesLoader;

/**
 * @FIXME Speed up Spring Boot startup time!!
 * EnableAutoConfiguration(exclude = {ActiveMQAutoConfiguration.class, xxx.class,})
 * @author krisjey
 */
@SpringBootApplication
public class ArmedisServer implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(ArmedisServer.class);

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        SpringApplication application = new SpringApplication(ArmedisServer.class);

        // load application properties
        application.setDefaultProperties(ApplicationPropertiesLoader.getProperties());
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);

        System.out.println("Spring framework is loaded! (main) " + (System.currentTimeMillis() - startTime) + "ms elapsed");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String loadedMessage = "Spring application loaded!(ArmedisServer.run())";
        logger.info(loadedMessage);
        
        // TODO print connected redis config.
        
        // TODO start redis info generator. 

        System.out.println(loadedMessage);
    }
}

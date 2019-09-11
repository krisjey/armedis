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

@SpringBootApplication
public class ArmedisServer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ArmedisServer.class);

    @Autowired
    ArmedisServerConfiguration armeriaHttpServerConfiguration;

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
        String loadedMessage = "Spring application loaded!(run)";
        logger.info(loadedMessage);
        System.out.println(loadedMessage);
    }
}

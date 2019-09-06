package com.github.armedis.config;

import java.io.IOException;
import java.util.Properties;

public class ApplicationPropertiesLoader {
    /**
     * FIXME add application.properties + zookeeper config
     * @return application.properties + zookeeper config
     * @throws IOException
     */
    public static Properties getProperties() throws IOException {
//        File file = ConfigReader.getInstance().getConfigFile("application.properties");
        Properties configProperty = new Properties();
//        configProperty.load(new FileInputStream(file));

//        configProperty.putAll(ConfigReader.getInstance().getAll());

        return configProperty;
    }
}
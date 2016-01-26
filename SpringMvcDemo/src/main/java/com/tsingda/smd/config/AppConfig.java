package com.tsingda.smd.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:jdbc.properties", "classpath:app.properties" })
public class AppConfig {

    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value(value = "${db.name}")
    private String dbName;

    @Value(value = "${app.name}")
    private String appName;

    @Autowired
    Environment env;

    public AppConfig() {
        logger.debug("==========AppConfig init==========");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer loadPropertySource() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
    
    @PostConstruct
    public void printConfigInfo() {
        logger.debug("db.name:{}", env.getProperty("db.name"));
        logger.debug("db.name:{}", dbName);
        logger.debug("app.name:{}", appName);
    }

}

package com.tsingda.smd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);
    
    public AppConfig(){
        logger.debug("==========AppConfig init==========");
    }

}

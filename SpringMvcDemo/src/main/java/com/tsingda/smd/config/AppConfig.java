package com.tsingda.smd.config;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@PropertySource(value = { "classpath:jdbc.properties" })
public class AppConfig {

    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value(value = "${db.name}")
    private String dbName;

    @Value(value = "#{appProperties['app.upload.file.temp']}")
    private String uploadFileTemp;

    @Autowired
    Environment env;

    public AppConfig() {
        logger.debug("==========AppConfig init==========");
    }

    @PostConstruct
    public void printConfigInfo() {
        logger.debug("配置信息：{}", env);
    }

    @Bean(name = "appProperties")
    public static PropertiesFactoryBean propertiesFactoryBean() throws IOException {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        Resource location = new ClassPathResource("app.properties");
        bean.setLocations(location);
        bean.setSingleton(true);
        return bean;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer loadPropertySource() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }

    @Bean
    public MultipartResolver multipartResolver(ServletContext context) throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setResolveLazily(true);
//        resolver.setMaxUploadSizePerFile(5 * 10 * 1024 * 1024);
        resolver.setMaxUploadSize(100 * 1024 * 1024);
        File uploadTempDir = new File(context.getRealPath("/") + uploadFileTemp);
        resolver.setUploadTempDir(new FileSystemResource(uploadTempDir));
        return resolver;
    }

}

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
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tsingda.smd.config.interceptor.FileUploadInterceptor;

@Configuration
@Import(value = { DataSourceConfig.class, MybatisConfig.class })
public class AppConfig {

    private final static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /**
     * 文件上传的临时文件夹地址
     */
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

    /**
     * 加载基本配置
     * 
     * 使用方法：在类型属性上或者set方法上注解 @Value(value = "#{appProperties['配置的变量名']}")
     * 
     * @return
     * @throws IOException
     */
    @Bean(name = "appProperties")
    public static PropertiesFactoryBean propertiesFactoryBean() throws IOException {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        Resource location = new ClassPathResource("app.properties");
        bean.setLocations(location);
        bean.setSingleton(true);
        return bean;
    }

    /**
     * propert sources 加载，实现在@Configuration 配置类中，可以用@Value来加载@PropertySource中的配置
     * 用法：在类型属性或者setter方法上面加注解 @Value("${db.driver}")
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer loadPropertySource() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }

    /**
     * multipart resolver 配置，用来上传文件。文件大小限制的设置应该大一些， 相当于不起作用，因为tomcat在抛出 异常
     * {@link MaxUploadSizeExceededException}
     * 的时候有问题，所以上传文件大小限制用Interceptor的形式来实现
     * 
     * @see MvcConfig#addInterceptors {@link MvcConfig#addInterceptors}
     * @see FileUploadInterceptor#preHandle
     * @param context
     *            servlet context
     * @return multipart resolver
     * @throws IOException
     *             IO Exception
     */
    @Bean
    public MultipartResolver multipartResolver(ServletContext context) throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setResolveLazily(true);
        // resolver.setMaxUploadSizePerFile(5 * 10 * 1024 * 1024);

        resolver.setMaxUploadSize(100 * 1024 * 1024);
        File uploadTempDir = new File(context.getRealPath("/") + uploadFileTemp);
        resolver.setUploadTempDir(new FileSystemResource(uploadTempDir));
        return resolver;
    }

}

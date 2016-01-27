package com.tsingda.smd.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tsingda.smd.util.JsonUtil;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.tsingda.smd", excludeFilters = @Filter(type = FilterType.REGEX, pattern = "com.tsingda.smd.config.*"))
public class MvcConfig extends WebMvcConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    private final static MediaType TEXT_PLAIN_UTF8 = new MediaType("text", "plain", Charset.forName("UTF-8"));
    private final static MediaType TEXT_HTML_UTF8 = new MediaType("text", "html", Charset.forName("UTF-8"));
    private final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter(
            JsonUtil.objectMapper);

    public MvcConfig() {
        super();
        logger.debug("==========MvcConfig init=================");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        logger.debug("==========configureViewResolvers==========");
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.debug("==========addResourceHandlers=============");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        // StringHttpMessageConverter 配置
        // 设置Stringodgar返回Content-Type:text/plain;charset=UTF-8、Content-Type:text/html;charset=UTF-8
        StringHttpMessageConverter stringMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringMessageConverter.setWriteAcceptCharset(false);
        List<MediaType> types = new ArrayList<MediaType>();
        types.add(TEXT_PLAIN_UTF8);
        types.add(MediaType.APPLICATION_JSON_UTF8);
        types.add(TEXT_HTML_UTF8);
        stringMessageConverter.setSupportedMediaTypes(types);
        converters.add(stringMessageConverter);
        converters.add(this.jsonMessageConverter);
    }

    /**
     * 配置异常处理
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        super.configureHandlerExceptionResolvers(exceptionResolvers);
        MyExceptionResolver exceptionResolver = new MyExceptionResolver();
        exceptionResolver.setJsonMessageConverter(this.jsonMessageConverter);
        // 设置默认错误码
        exceptionResolver.setDefaultStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        // 设置默认错误页面
        exceptionResolver.setDefaultErrorView("error/500");
        // 添加特定异常对应的错误页面 e.g.
        // java.sql.SQLException、java.io.IOException、java.lang.Exception、java.lang.Throwable
        Properties mappings = new Properties();
        mappings.put("org.springframework.web.servlet.NoHandlerFoundException", "error/404");
        mappings.put("java.sql.SQLException", "error/sqlException");
        mappings.put("java.lang.Exception", "error/500");
        mappings.put("java.lang.Throwable", "error/500");
        exceptionResolver.setExceptionMappings(mappings);

        Properties exceptionStatusMappings = new Properties();
        exceptionStatusMappings.put("org.springframework.web.servlet.NoHandlerFoundException",
                HttpServletResponse.SC_NOT_FOUND);
        exceptionStatusMappings.put("java.lang.Exception", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionResolver.setExceptionCodeMapping(exceptionStatusMappings);

        exceptionResolvers.add(exceptionResolver);
    }

    @Bean
    public Validator initValidatorFactoryBean() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        return validator;
    }
}

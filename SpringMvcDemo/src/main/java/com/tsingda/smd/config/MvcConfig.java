package com.tsingda.smd.config;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tsingda.smd.config.interceptor.FileUploadInterceptor;
import com.tsingda.smd.util.JsonUtil;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.tsingda.smd")
public class MvcConfig extends WebMvcConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    private final static String DEFAULT_CHARSET_VALUE = "UTF-8";
    private final static Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_VALUE);

    private final static MediaType TEXT_PLAIN_UTF8 = new MediaType("text", "plain", DEFAULT_CHARSET);
    private final static MediaType TEXT_HTML_UTF8 = new MediaType("text", "html", DEFAULT_CHARSET);

    /**
     * Bean Validation Messages 缓存时间
     */
    private final static int VALIDATION_MESSAGES_CACHE_SECONS = 60;

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

    @SuppressWarnings("rawtypes")
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        // StringHttpMessageConverter 配置
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter());
        converters.add(new AtomFeedHttpMessageConverter());
        converters.add(new RssChannelHttpMessageConverter());
        // 设置Stringodgar返回Content-Type:text/plain;charset=UTF-8、Content-Type:text/html;charset=UTF-8
        StringHttpMessageConverter stringMessageConverter = new StringHttpMessageConverter(DEFAULT_CHARSET);
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
        // 设置默认错误码
        exceptionResolver.setDefaultStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        // 设置默认错误页面
        exceptionResolver.setDefaultErrorView("error/500");
        // 添加特定异常对应的错误页面 e.g.
        // java.sql.SQLException、java.io.IOException、java.lang.Exception、java.lang.Throwable
        Properties mappings = new Properties();
        mappings.put(NoHandlerFoundException.class.getName(), "error/404");
        mappings.put(SQLException.class.getName(), "error/sqlException");
        mappings.put(Exception.class.getName(), "error/500");
        mappings.put(Throwable.class.getName(), "error/500");
        exceptionResolver.setExceptionMappings(mappings);
        Properties exceptionStatusMappings = new Properties();
        exceptionStatusMappings.put("org.springframework.web.servlet.NoHandlerFoundException",
                HttpServletResponse.SC_NOT_FOUND);
        exceptionStatusMappings.put("java.lang.Exception", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        exceptionResolver.setExceptionCodeMapping(exceptionStatusMappings);

        exceptionResolvers.add(exceptionResolver);
    }

    @Override
    public Validator getValidator() {
        OptionalValidatorFactoryBean validator = new OptionalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(DEFAULT_CHARSET_VALUE);
        messageSource.setCacheSeconds(VALIDATION_MESSAGES_CACHE_SECONS);

        String userValidationMessages = "UserValidationMessages";
        String orderValidationMessages = "OrderValidationMessages";
        String commonValidationMessages = "CommonValidationMessages";
        messageSource.setBasenames(userValidationMessages, orderValidationMessages, commonValidationMessages);
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        FileUploadInterceptor fileUploadInterceptor = new FileUploadInterceptor();
        fileUploadInterceptor.setMaxSize(10 * 1024 * 1024);
        registry.addInterceptor(fileUploadInterceptor);
    }

}

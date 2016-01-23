package com.tsingda.smd.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
        MediaType textPlainType = new MediaType("text", "plain", Charset.forName("UTF-8"));
        MediaType textHtmlType = new MediaType("text", "html", Charset.forName("UTF-8"));
        List<MediaType> types = new ArrayList<MediaType>();
        types.add(textPlainType);
        types.add(textHtmlType);
        stringMessageConverter.setSupportedMediaTypes(types);
        converters.add(stringMessageConverter);

        HttpMessageConverter<Object> jsonMessageConverter = new MappingJackson2HttpMessageConverter(
                JsonUtil.objectMapper);
        converters.add(jsonMessageConverter);
    }

}

package com.tsingda.smd.config;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

/**
 * MyBatis相关配置
 * @ClassName: MyBatisConfig 
 * @Description: 配置MyBatis
 * @author Administrator
 * @date 2016年2月15日 下午1:53:33 
 *
 */
@Configuration
public class MyBatisConfig {

    private static final String MAPPER_BASE_PACKAGE = "com.tsingda.smd.dao.mapper";

    private static final String SQL_SESSION_FACTORY_BEAN_NAME = "sqlSessionFactory";

    @Bean(name = SQL_SESSION_FACTORY_BEAN_NAME)
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, ServletContext servletContext)
            throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        Resource configLocation = new ClassPathResource("mybatis.xml");
        sqlSessionFactoryBean.setConfigLocation(configLocation);

        ServletContextResourcePatternResolver resourceResolver = new ServletContextResourcePatternResolver(
                servletContext);
        Resource[] mapperLocations = resourceResolver.getResources("classpath*:com/tsingda/smd/sql/*.xml");
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);

        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scanner = new MapperScannerConfigurer();
        scanner.setBasePackage(MAPPER_BASE_PACKAGE);
        scanner.setSqlSessionFactoryBeanName(SQL_SESSION_FACTORY_BEAN_NAME);
        return scanner;
    }

}

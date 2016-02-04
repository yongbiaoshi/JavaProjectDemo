package com.tsingda.smd.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.tsingda.smd.dao.UserMapper;

@Configuration
public class MybatisConfig {

    @Bean(name = "sessionFactory")
    public SqlSessionFactoryBean sessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        Resource configLocation = new ClassPathResource("mybatis.xml");
        sessionFactoryBean.setConfigLocation(configLocation);
        return sessionFactoryBean;
    }

    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSession(SqlSessionFactoryBean sessionFactory) throws Exception {
        return new SqlSessionTemplate(sessionFactory.getObject());
    }

    @Bean(name = "userMapper")
    public UserMapper userMapper(SqlSessionFactoryBean sessionFactory) throws Exception {
        MapperFactoryBean<UserMapper> mapperFactory = new MapperFactoryBean<UserMapper>();
        mapperFactory.setMapperInterface(UserMapper.class);
        mapperFactory.setSqlSessionFactory(sessionFactory.getObject());
        return mapperFactory.getObject();
    }
}

package com.tsingda.smd.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.tsingda.smd.dao.UserMapper;

@Configuration
public class MybatisConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        Resource configLocation = new ClassPathResource("mybatis.xml");
        sqlSessionFactoryBean.setConfigLocation(configLocation);
        SpringManagedTransactionFactory factory = new SpringManagedTransactionFactory();
        sqlSessionFactoryBean.setTransactionFactory(factory);
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "userMapper")
    public UserMapper userMapper(SqlSessionFactory sqlSessionFactory) throws Exception {
        return getMapper(sqlSessionFactory, UserMapper.class);
    }

    private <T> T getMapper(SqlSessionFactory sqlSessionFactory, Class<T> clazz) throws Exception {
        MapperFactoryBean<T> mapperFactory = new MapperFactoryBean<T>();
        mapperFactory.setMapperInterface(clazz);
        mapperFactory.setSqlSessionFactory(sqlSessionFactory);
        return mapperFactory.getObject();
    }
}

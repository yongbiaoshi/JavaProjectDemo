package com.tsingda.smd.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;

@Configuration
@PropertySource(value = { "classpath:jdbc.properties" })
public class DataSourceConfig {

    @Value("${db.driver}")
    private String dbDriver;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUsername;
    @Value("${db.password}")
    private String dbPassword;
    @Value("${db.initialSize}")
    private String dbInitialSize;
    @Value("${db.maxActive}")
    private String dbMaxActive;

    @Bean(initMethod = "init", destroyMethod = "close", name = "dataSource")
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, dbDriver);
        properties.put(DruidDataSourceFactory.PROP_URL, dbUrl);
        properties.put(DruidDataSourceFactory.PROP_USERNAME, dbUsername);
        properties.put(DruidDataSourceFactory.PROP_PASSWORD, dbPassword);
        //配置初始化连接数和最大连接数
        properties.put(DruidDataSourceFactory.PROP_INITIALSIZE, dbInitialSize);
        properties.put(DruidDataSourceFactory.PROP_MAXACTIVE, dbMaxActive);
        //配置用于监控和统计的filters
        properties.put(DruidDataSourceFactory.PROP_FILTERS, "stat");
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }
    
}

package com.tsingda.smd.config;

import java.nio.charset.Charset;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.tsingda.smd.util.JsonUtil;

@Configuration
@PropertySource(value = { "classpath:${spring.profiles.active:dev}/jdbc.properties" })
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

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.maxWaitMillis}")
    private long redisMaxWaitMillis;

    @Bean(initMethod = "init", destroyMethod = "close", name = "dataSource")
    public DataSource dataSource() throws Exception {
        Properties properties = new Properties();
        properties.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, dbDriver);
        properties.put(DruidDataSourceFactory.PROP_URL, dbUrl);
        properties.put(DruidDataSourceFactory.PROP_USERNAME, dbUsername);
        properties.put(DruidDataSourceFactory.PROP_PASSWORD, dbPassword);
        // 配置初始化连接数和最大连接数
        properties.put(DruidDataSourceFactory.PROP_INITIALSIZE, dbInitialSize);
        properties.put(DruidDataSourceFactory.PROP_MAXACTIVE, dbMaxActive);
        // 配置用于监控和统计的filters
        properties.put(DruidDataSourceFactory.PROP_FILTERS, "stat");
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

    /**
     * redis connection factory config
     *
     * @return redis connection factory
     */
    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(redisPassword);
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxWaitMillis(redisMaxWaitMillis);
        jedisConnectionFactory.setPoolConfig(poolConfig);
        //生产环境去掉，测试只用一个DB就
        jedisConnectionFactory.setDatabase(1);
        return jedisConnectionFactory;
    }

    /**
     * redis template
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory,
            StringRedisSerializer stringRedisSerializer,
            GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        RedisTemplate<K, V> template = new RedisTemplate<K, V>();
        template.setConnectionFactory(connectionFactory);

        template.setDefaultSerializer(stringRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return template;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer(Charset.forName("UTF-8"));
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(JsonUtil.objectMapper);
    }
}

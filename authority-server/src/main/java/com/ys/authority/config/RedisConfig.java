package com.ys.authority.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${redis-cfg.verify-pool:2}")
    private int verifyDBId;

    @Value("${redis-cfg.access-token:3}")
    private int accessTokenDBId;

    @Value("${redis-cfg.host:127.0.0.1}")
    private String host;

    @Value("${redis-cfg.port:6379}")
    private int port;

    @Value("${redis-cfg.pool.min-idle:0}")
    private int minIdle;

    @Value("${redis-cfg.pool.max-active:10}")
    private int maxActive;

    @Value("${redis-cfg.pool.max-wait:-1}")
    private long maxWait;

    @Value("${redis-cfg.pool.max-idle:8}")
    private int maxIdle;

    @Value("${redis-cfg.password}")
    private String password;

    @Bean
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        config.setMaxTotal(maxActive);
        return config;
    }

    @Scope(scopeName = "prototype")
    public JedisConnectionFactory jedisConnectionFactory(int dbId) {
        JedisPoolConfig jedisPoolConfig = getRedisConfig();
        //单机版jedis
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(host);
        //设置默认使用的数据库
        redisStandaloneConfiguration.setDatabase(dbId);
        //设置密码
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        //设置redis的服务的端口号
        redisStandaloneConfiguration.setPort(port);
        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        //单机配置 + 客户端配置 = jedis连接工厂
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean(name = "verify-pool")
    public RedisTemplate<String, String> verifyMaskRedisTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        buildRedisTemplate(template, verifyDBId);
        return template;
    }

    @Bean(name = "access-token")
    public StringRedisTemplate accessRedisTemplate() {
        final StringRedisTemplate template = new StringRedisTemplate();
        buildRedisTemplate(template, accessTokenDBId);
        return template;
    }

    private void buildRedisTemplate(RedisTemplate<String, String> template, int dbId) {
        JedisConnectionFactory jedisConnectionFactory = jedisConnectionFactory(dbId);
        template.setConnectionFactory(jedisConnectionFactory);
        log.info("create RedisTemplate host:{}, port:{}, database:{}",
                jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(), jedisConnectionFactory.getDatabase());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringRedisSerializer);
    }
}

package com.redislabs.university.RU102J.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

    @Bean(destroyMethod = "close")
    public JedisPool jedisPool(RedisProperties redis) {
        if (redis.hasPassword()) {
            return new JedisPool(new JedisPoolConfig(), redis.host(), redis.port(),
                    2000, redis.password());
        }
        return new JedisPool(redis.host(), redis.port());
    }
}

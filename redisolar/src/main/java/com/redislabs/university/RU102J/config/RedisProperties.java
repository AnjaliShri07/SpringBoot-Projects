package com.redislabs.university.RU102J.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Connection settings for Redis, bound from the {@code redis.*} properties
 * (see application.yml).
 */
@ConfigurationProperties(prefix = "redis")
public record RedisProperties(
        @DefaultValue("localhost") String host,
        @DefaultValue("6379") int port,
        @DefaultValue("") String password) {

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }
}

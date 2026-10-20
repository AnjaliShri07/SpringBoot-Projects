package com.redislabs.university.RU102J.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/** Reported by Actuator as the "redisolar" component of /actuator/health. */
@Component("redisolar")
public class RediSolarHealthIndicator implements HealthIndicator {

    private final JedisPool jedisPool;

    public RediSolarHealthIndicator(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Health health() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
            return Health.up().build();
        } catch (JedisException e) {
            return Health.down(e).build();
        }
    }
}

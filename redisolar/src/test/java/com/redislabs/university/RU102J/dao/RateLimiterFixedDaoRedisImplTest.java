package com.redislabs.university.RU102J.dao;

import com.redislabs.university.RU102J.HostPort;
import com.redislabs.university.RU102J.TestKeyManager;
import org.junit.jupiter.api.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterFixedDaoRedisImplTest {

    private static JedisPool jedisPool;
    private static Jedis jedis;
    private static TestKeyManager keyManager;

    @BeforeAll
    public static void setUp() throws Exception {
        String password = HostPort.getRedisPassword();

        if (password.length() > 0) {
            jedisPool = new JedisPool(new JedisPoolConfig(), HostPort.getRedisHost(), HostPort.getRedisPort(), 2000, password);
        } else {
            jedisPool = new JedisPool(HostPort.getRedisHost(), HostPort.getRedisPort());
        }

        jedis = new Jedis(HostPort.getRedisHost(), HostPort.getRedisPort());

        if (password.length() > 0) {
            jedis.auth(password);
        }

        keyManager = new TestKeyManager("test");
    }

    @AfterAll
    public static void tearDown() {
        jedisPool.destroy();
        jedis.close();
    }

    @AfterEach
    public void flush() {
        keyManager.deleteKeys(jedis);
    }

    @Test
    public void hit() {
        int exceptionCount = 0;
        RateLimiter limiter = new RateLimiterFixedDaoRedisImpl(jedisPool,
                MinuteInterval.ONE, 10);
        for (int i=0; i<10; i++) {
            try {
                limiter.hit("foo");
            } catch (RateLimitExceededException e) {
                exceptionCount += 1;
            }
        }

        assertThat(exceptionCount, is(0));
    }

    @Test
    public void getMinuteOfDayBlock() {
    }
}
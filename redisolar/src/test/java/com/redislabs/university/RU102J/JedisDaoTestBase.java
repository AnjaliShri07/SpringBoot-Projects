package com.redislabs.university.RU102J;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class JedisDaoTestBase {

    protected static JedisPool jedisPool;
    protected static Jedis jedis;
    protected static TestKeyManager keyManager;

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

}

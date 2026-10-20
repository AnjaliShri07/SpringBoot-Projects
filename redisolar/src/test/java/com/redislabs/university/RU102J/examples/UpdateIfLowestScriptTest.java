package com.redislabs.university.RU102J.examples;

import com.redislabs.university.RU102J.HostPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateIfLowestScriptTest {

    private Jedis jedis;

    @BeforeEach
    public void setUp() {
        this.jedis = new Jedis(HostPort.getRedisHost(), HostPort.getRedisPort());

        if (HostPort.getRedisPassword().length() > 0) {
            this.jedis.auth(HostPort.getRedisPassword());
        }
    }

    @AfterEach
    public void tearDown() {
        jedis.del("testLua");
        jedis.close();
    }

    @Test
    public void updateIfLowest() {
        jedis.set("testLua", "100");
        UpdateIfLowestScript script = new UpdateIfLowestScript(jedis);

        boolean result = script.updateIfLowest("testLua", 50);
        assertThat(result, is(true));
        assertThat(jedis.get("testLua"), is("50"));
    }

    @Test
    public void updateIfLowestUnchanged() {
        jedis.set("testLua", "100");
        UpdateIfLowestScript script = new UpdateIfLowestScript(jedis);

        boolean result = script.updateIfLowest("testLua", 200);
        assertThat(result, is(false));
        assertThat(jedis.get("testLua"), is("100"));
    }

    @Test
    public void updateIfLowestWithNoKey() {
        jedis.del("testLua");
        UpdateIfLowestScript script = new UpdateIfLowestScript(jedis);

        boolean result = script.updateIfLowest("testLua", 200);
        assertThat(result, is(true));
        assertThat(jedis.get("testLua"), is("200"));
    }
}
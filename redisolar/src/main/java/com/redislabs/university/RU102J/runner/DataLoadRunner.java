package com.redislabs.university.RU102J.runner;

import com.redislabs.university.RU102J.core.DataLoader;
import com.redislabs.university.RU102J.core.SampleDataGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * Replaces the Dropwizard "load" command.
 *
 * <pre>
 * java -jar target/redisolar-1.0.jar --spring.profiles.active=load [--redisolar.load.flush=true]
 * </pre>
 * Redis connection settings come from the usual {@code --redis.host/port/password} properties.
 */
@Component
@Profile("load")
public class DataLoadRunner implements ApplicationRunner {

    private final JedisPool jedisPool;
    private final boolean flush;
    private final String filename;

    public DataLoadRunner(JedisPool jedisPool,
                          @Value("${redisolar.load.flush:false}") boolean flush,
                          @Value("${redisolar.load.filename:}") String filename) {
        this.jedisPool = jedisPool;
        this.flush = flush;
        this.filename = filename;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DataLoader loader = filename.isBlank()
                ? new DataLoader(jedisPool)
                : new DataLoader(jedisPool, filename);

        if (flush) {
            loader.flush();
        }
        loader.load();

        new SampleDataGenerator(jedisPool).generateHistorical(1);
        System.out.println("Data load complete!");
    }
}

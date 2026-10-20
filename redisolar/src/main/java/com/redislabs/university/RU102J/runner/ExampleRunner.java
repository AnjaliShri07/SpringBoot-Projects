package com.redislabs.university.RU102J.runner;

import com.redislabs.university.RU102J.config.RedisProperties;
import com.redislabs.university.RU102J.examples.Hello;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Replaces the Dropwizard "run --example" command.
 *
 * <pre>
 * java -jar target/redisolar-1.0.jar --spring.profiles.active=example --redisolar.example=hello
 * </pre>
 */
@Component
@Profile("example")
public class ExampleRunner implements ApplicationRunner {

    private final RedisProperties redis;
    private final String example;

    public ExampleRunner(RedisProperties redis,
                         @Value("${redisolar.example:}") String example) {
        this.redis = redis;
        this.example = example;
    }

    @Override
    public void run(ApplicationArguments args) {
        switch (example.toLowerCase()) {
            case "hello" -> new Hello(redis.host(), redis.port()).say();
            default -> System.out.println("Unknown example: '" + example
                    + "'. Use --redisolar.example=hello");
        }
    }
}

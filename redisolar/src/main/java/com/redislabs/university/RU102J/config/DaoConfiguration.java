package com.redislabs.university.RU102J.config;

import com.redislabs.university.RU102J.dao.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * Wires the Redis DAO implementations. The DAOs themselves stay plain Java
 * classes (they are what you edit while doing the course challenges).
 *
 * <p>Two switches replace the "comment/uncomment these lines" instructions of
 * the original Dropwizard application:
 * <ul>
 *   <li>{@code redisolar.site-dao}: {@code basic} (default) or {@code geo}</li>
 *   <li>{@code redisolar.metric-dao}: {@code zset} (default) or {@code timeseries}</li>
 * </ul>
 */
@Configuration(proxyBeanMethods = false)
public class DaoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "redisolar", name = "site-dao",
            havingValue = "basic", matchIfMissing = true)
    public SiteDao siteDao(JedisPool jedisPool) {
        return new SiteDaoRedisImpl(jedisPool);
    }

    @Bean
    @ConditionalOnProperty(prefix = "redisolar", name = "site-dao", havingValue = "geo")
    public SiteGeoDao siteGeoDao(JedisPool jedisPool) {
        return new SiteGeoDaoRedisImpl(jedisPool);
    }

    @Bean
    @ConditionalOnProperty(prefix = "redisolar", name = "metric-dao",
            havingValue = "zset", matchIfMissing = true)
    public MetricDao zsetMetricDao(JedisPool jedisPool) {
        return new MetricDaoRedisZsetImpl(jedisPool);
    }

    @Bean
    @ConditionalOnProperty(prefix = "redisolar", name = "metric-dao",
            havingValue = "timeseries")
    public MetricDao timeSeriesMetricDao(JedisPool jedisPool) {
        return new MetricDaoRedisTSImpl(jedisPool);
    }

    @Bean
    public CapacityDao capacityDao(JedisPool jedisPool) {
        return new CapacityDaoRedisImpl(jedisPool);
    }

    @Bean
    public FeedDao feedDao(JedisPool jedisPool) {
        return new FeedDaoRedisImpl(jedisPool);
    }

    @Bean
    public SiteStatsDao siteStatsDao(JedisPool jedisPool) {
        return new SiteStatsDaoRedisImpl(jedisPool);
    }
}

package com.redislabs.university.RU102J.service;

import com.redislabs.university.RU102J.api.MeterReading;
import com.redislabs.university.RU102J.dao.CapacityDao;
import com.redislabs.university.RU102J.dao.FeedDao;
import com.redislabs.university.RU102J.dao.MetricDao;
import com.redislabs.university.RU102J.dao.SiteStatsDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Fans a meter reading out to all of the DAOs that need to record it.
 * (Previously this logic lived inside the JAX-RS MeterReadingResource.)
 */
@Service
public class MeterReadingService {

    private final SiteStatsDao siteStatsDao;
    private final MetricDao metricDao;
    private final CapacityDao capacityDao;
    private final FeedDao feedDao;

    public MeterReadingService(SiteStatsDao siteStatsDao, MetricDao metricDao,
                               CapacityDao capacityDao, FeedDao feedDao) {
        this.siteStatsDao = siteStatsDao;
        this.metricDao = metricDao;
        this.capacityDao = capacityDao;
        this.feedDao = feedDao;
    }

    public void addAll(List<MeterReading> readings) {
        for (MeterReading reading : readings) {
            add(reading);
        }
    }

    public void add(MeterReading reading) {
        metricDao.insert(reading);
        siteStatsDao.update(reading);
        capacityDao.update(reading);
        feedDao.insert(reading);
    }
}

package com.redislabs.university.RU102J.dao;

import com.redislabs.university.RU102J.api.MeterReading;
import com.redislabs.university.RU102J.api.SiteStats;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface SiteStatsDao {
    SiteStats findById(long siteId);
    SiteStats findById(long siteId, ZonedDateTime day);
    void update(MeterReading reading);
}

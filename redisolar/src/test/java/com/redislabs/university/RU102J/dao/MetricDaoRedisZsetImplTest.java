package com.redislabs.university.RU102J.dao;

import com.redislabs.university.RU102J.JedisDaoTestBase;
import com.redislabs.university.RU102J.api.Measurement;
import com.redislabs.university.RU102J.api.MeterReading;
import com.redislabs.university.RU102J.api.MetricUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class MetricDaoRedisZsetImplTest extends JedisDaoTestBase {

    private List<MeterReading> readings;
    private Long siteId = 1L;
    private ZonedDateTime startingDate = ZonedDateTime.now(ZoneOffset.UTC);

    @AfterEach
    public void flush() {
        keyManager.deleteKeys(jedis);
    }

    /**
     * Generate 72 hours worth of data.
     */
    @BeforeEach
    public void generateData() {
        readings = new ArrayList<>();
        ZonedDateTime time = startingDate;
        for (int i=0; i <  72 * 60; i++) {
            MeterReading reading = new MeterReading();
            reading.setSiteId(siteId);
            reading.setTempC(i * 1.0);
            reading.setWhUsed(i * 1.0);
            reading.setWhGenerated(i * 1.0);
            reading.setDateTime(time);
            readings.add(reading);
            time = time.minusMinutes(1);
        }
    }

    // Challenge #2
    @Disabled
    @Test
    public void testSmall() {
        testInsertAndRetrieve(1);
    }

    // Challenge #2
    @Disabled
    @Test
    public void testOneDay() {
        testInsertAndRetrieve(60 * 24);
    }


    // Challenge #2
    @Disabled
    @Test
    public void testMultipleDays() {
        testInsertAndRetrieve(60 * 70);
    }

    private void testInsertAndRetrieve(int limit) {
        MetricDao metricDao = new MetricDaoRedisZsetImpl(jedisPool);
        for (MeterReading reading : readings) {
            metricDao.insert(reading);
        }

        List<Measurement> measurements = metricDao.getRecent(siteId, MetricUnit.WHGenerated,
         startingDate, limit);
        assertThat(measurements.size(), is(limit));
        int i = limit;
        for (Measurement measurement : measurements) {
            assertThat(measurement.getValue(), is((i - 1) * 1.0));
            i -= 1;
        }
    }
}
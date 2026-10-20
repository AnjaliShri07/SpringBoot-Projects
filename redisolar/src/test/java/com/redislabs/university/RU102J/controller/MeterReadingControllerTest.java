package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.MeterReading;
import com.redislabs.university.RU102J.dao.CapacityDao;
import com.redislabs.university.RU102J.dao.FeedDao;
import com.redislabs.university.RU102J.dao.MetricDao;
import com.redislabs.university.RU102J.dao.SiteStatsDao;
import com.redislabs.university.RU102J.service.MeterReadingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MeterReadingControllerTest {
    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    private final SiteStatsDao siteStatsDao = mock(SiteStatsDao.class);
    private final MetricDao metricDao = mock(MetricDao.class);
    private final CapacityDao capacityDao = mock(CapacityDao.class);
    private final FeedDao feedDao = mock(FeedDao.class);

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MeterReadingService service =
                new MeterReadingService(siteStatsDao, metricDao, capacityDao, feedDao);
        mvc = MockMvcBuilders
                .standaloneSetup(new MeterReadingController(service, feedDao))
                .build();
    }

    @AfterEach
    public void tearDown() {
        reset(siteStatsDao, metricDao, capacityDao, feedDao);
    }

    @Test
    public void testInsertMeterReading() throws Exception {
        List<MeterReading> readings = new ArrayList<>();
        MeterReading r1 = generateMeterReading(1);
        MeterReading r2 = generateMeterReading(2);
        readings.add(r1);
        readings.add(r2);

        mvc.perform(post("/api/meterReadings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(readings)))
                .andExpect(status().isAccepted());

        verify(metricDao).insert(r1);
        verify(metricDao).insert(r2);
        verify(capacityDao).update(r1);
        verify(capacityDao).update(r2);
        verify(siteStatsDao).update(r1);
        verify(siteStatsDao).update(r2);
        verify(feedDao).insert(r1);
        verify(feedDao).insert(r2);
    }

    private MeterReading generateMeterReading(long siteId) {
        ZonedDateTime now = ZonedDateTime.now().
                withZoneSameInstant(ZoneId.of("UTC"));
        MeterReading reading = new MeterReading();
        reading.setSiteId(siteId);
        reading.setDateTime(now);
        reading.setTempC(15.0);
        reading.setWhGenerated(0.025);
        reading.setWhUsed(0.015);
        return reading;
    }
}

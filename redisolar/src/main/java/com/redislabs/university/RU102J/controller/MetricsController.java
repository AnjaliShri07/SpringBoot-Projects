package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.Measurement;
import com.redislabs.university.RU102J.api.MetricUnit;
import com.redislabs.university.RU102J.api.Plot;
import com.redislabs.university.RU102J.dao.MetricDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private static final int DEFAULT_METRIC_COUNT = 120;

    private final MetricDao metricDao;

    public MetricsController(MetricDao metricDao) {
        this.metricDao = metricDao;
    }

    @GetMapping("/{siteId}")
    public List<Plot> getSiteMetrics(@PathVariable Long siteId,
                                     @RequestParam(required = false) Integer count) {
        int limit = count == null ? DEFAULT_METRIC_COUNT : count;
        List<Plot> plots = new ArrayList<>();

        List<Measurement> generated = metricDao.getRecent(siteId, MetricUnit.WHGenerated,
                ZonedDateTime.now(ZoneOffset.UTC), limit);
        plots.add(new Plot("Watt-Hours Generated", generated));

        List<Measurement> used = metricDao.getRecent(siteId, MetricUnit.WHUsed,
                ZonedDateTime.now(ZoneOffset.UTC), limit);
        plots.add(new Plot("Watt-Hours Used", used));

        return plots;
    }
}

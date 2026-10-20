package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.MeterReading;
import com.redislabs.university.RU102J.dao.FeedDao;
import com.redislabs.university.RU102J.service.MeterReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meterReadings")
public class MeterReadingController {

    private static final int MAX_RECENT_FEEDS = 1000;
    private static final int DEFAULT_RECENT_FEEDS = 100;

    private final MeterReadingService meterReadingService;
    private final FeedDao feedDao;

    public MeterReadingController(MeterReadingService meterReadingService, FeedDao feedDao) {
        this.meterReadingService = meterReadingService;
        this.feedDao = feedDao;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addAll(@RequestBody List<MeterReading> readings) {
        meterReadingService.addAll(readings);
    }

    @GetMapping({"", "/"})
    public List<MeterReading> getGlobal(@RequestParam(required = false) Integer count) {
        return feedDao.getRecentGlobal(getFeedCount(count));
    }

    @GetMapping("/{id}")
    public List<MeterReading> getForSite(@PathVariable Long id,
                                         @RequestParam(required = false) Integer count) {
        return feedDao.getRecentForSite(id, getFeedCount(count));
    }

    private int getFeedCount(Integer count) {
        if (count == null || count < 0) {
            return DEFAULT_RECENT_FEEDS;
        } else if (count > MAX_RECENT_FEEDS) {
            return MAX_RECENT_FEEDS;
        }
        return count;
    }
}

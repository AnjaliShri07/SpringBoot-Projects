package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.MeterReading;
import com.redislabs.university.RU102J.dao.FeedDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Not registered in the original Dropwizard app (and it was mapped to
 * "/capacity", clashing with CapacityResource). It is exposed here under
 * "/api/feed".
 */
@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private static final int DEFAULT_LIMIT = 20;

    private final FeedDao feedDao;

    public FeedController(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    @GetMapping({"", "/"})
    public List<MeterReading> getAllEntries(@RequestParam(required = false) Integer limit) {
        return feedDao.getRecentGlobal(limit == null ? DEFAULT_LIMIT : limit);
    }

    @GetMapping("/{id}")
    public List<MeterReading> getSingleFeed(@PathVariable("id") Long siteId,
                                            @RequestParam(required = false) Integer limit) {
        return feedDao.getRecentForSite(siteId, limit == null ? DEFAULT_LIMIT : limit);
    }
}

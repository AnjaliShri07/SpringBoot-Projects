package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.CapacityReport;
import com.redislabs.university.RU102J.dao.CapacityDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/capacity")
public class CapacityController {

    private static final int DEFAULT_LIMIT = 10;

    private final CapacityDao capacityDao;

    public CapacityController(CapacityDao capacityDao) {
        this.capacityDao = capacityDao;
    }

    // Spring 6+ no longer matches trailing slashes, and the dashboard calls "/api/capacity/".
    @GetMapping({"", "/"})
    public CapacityReport getCapacity(@RequestParam(required = false) Integer limit) {
        return capacityDao.getReport(limit == null ? DEFAULT_LIMIT : limit);
    }
}

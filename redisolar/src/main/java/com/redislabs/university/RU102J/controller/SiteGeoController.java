package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.Coordinate;
import com.redislabs.university.RU102J.api.GeoQuery;
import com.redislabs.university.RU102J.api.Site;
import com.redislabs.university.RU102J.dao.SiteGeoDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/** Sites endpoint with geospatial queries. Enable with redisolar.site-dao=geo. */
@RestController
@RequestMapping("/api/sites")
@ConditionalOnProperty(prefix = "redisolar", name = "site-dao", havingValue = "geo")
public class SiteGeoController {

    private static final double DEFAULT_RADIUS = 10.0;
    private static final String DEFAULT_GEO_UNIT = "KM";

    private final SiteGeoDao siteDao;

    public SiteGeoController(SiteGeoDao siteDao) {
        this.siteDao = siteDao;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Set<Site>> getSites(
            @RequestParam(required = false) String lng,
            @RequestParam(required = false) String lat,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) String radiusUnit,
            @RequestParam(required = false) Boolean onlyExcessCapacity) {

        if (lng == null && lat == null) {
            return ResponseEntity.ok(siteDao.findAll());
        } else if (lng != null && lat != null) {
            return ResponseEntity.ok(doGeoQuery(lng, lat, radius, radiusUnit, onlyExcessCapacity));
        }
        return ResponseEntity.notFound().build();
    }

    private Set<Site> doGeoQuery(String lng, String lat, Double radius,
                                 String radiusUnit, Boolean onlyExcessCapacity) {
        Coordinate coord = new Coordinate(lng, lat);
        double searchRadius = radius == null ? DEFAULT_RADIUS : radius;

        String unit = radiusUnit;
        if (unit == null || !(unit.equals("M") || unit.equals("KM")
                || unit.equals("FT") || unit.equals("MI"))) {
            unit = DEFAULT_GEO_UNIT;
        }

        boolean excessOnly = onlyExcessCapacity != null && onlyExcessCapacity;
        return siteDao.findByGeo(new GeoQuery(coord, searchRadius, unit, excessOnly));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        Site site = siteDao.findById(id);
        return site == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(site);
    }
}

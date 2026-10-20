package com.redislabs.university.RU102J.controller;

import com.redislabs.university.RU102J.api.Site;
import com.redislabs.university.RU102J.dao.SiteDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/** Default sites endpoint. Replaced by {@link SiteGeoController} when redisolar.site-dao=geo. */
@RestController
@RequestMapping("/api/sites")
@ConditionalOnProperty(prefix = "redisolar", name = "site-dao",
        havingValue = "basic", matchIfMissing = true)
public class SiteController {

    private final SiteDao siteDao;

    public SiteController(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

    @GetMapping({"", "/"})
    public Set<Site> getSites() {
        return siteDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        Site site = siteDao.findById(id);
        return site == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(site);
    }
}

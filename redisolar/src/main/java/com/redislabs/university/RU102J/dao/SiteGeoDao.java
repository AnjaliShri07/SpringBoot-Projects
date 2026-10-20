package com.redislabs.university.RU102J.dao;

import com.redislabs.university.RU102J.api.GeoQuery;
import com.redislabs.university.RU102J.api.Site;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SiteGeoDao extends SiteDao {
    Set<Site> findByGeo(GeoQuery query);
}

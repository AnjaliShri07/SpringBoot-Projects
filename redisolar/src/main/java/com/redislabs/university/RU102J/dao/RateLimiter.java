package com.redislabs.university.RU102J.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface RateLimiter {
    void hit(String name) throws RateLimitExceededException;
}

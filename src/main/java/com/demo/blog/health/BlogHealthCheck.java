package com.demo.blog.health;

import com.codahale.metrics.health.HealthCheck;
import com.demo.blog.service.PartsService;

public class BlogHealthCheck extends HealthCheck {
    private static final String HEALTHY = "The Dropwizard blog Service is healthy for read and write";
    private static final String UNHEALTHY = "The Dropwizard blog Service is not healthy. ";
    private static final String MESSAGE_PLACEHOLDER = "{}";

    private final PartsService partsService;

    public BlogHealthCheck(PartsService partsService) {
        this.partsService = partsService;
    }

    @Override
    public Result check() throws Exception {
        String mySqlHealthStatus = partsService.performHealthCheck();

        if (mySqlHealthStatus == null) {
            return Result.healthy(HEALTHY);
        }
        return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
    }
}
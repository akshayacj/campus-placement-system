package com.college.placement.dto;

/**
 * TECH TALK: Explicit DTO using Java 21 Record
 * Exposes active DataSource pool configuration for live inspection.
 */
public record DataSourceInfoResponse(
        String dataSourceClass,
        String poolName,
        String jdbcUrl,
        int maximumPoolSize
) {}


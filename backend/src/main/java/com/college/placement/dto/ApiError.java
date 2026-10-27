package com.college.placement.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized API Error record contract across all HTTP error responses.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String code,
        String message,
        String path,
        Map<String, String> fieldErrors
) {}


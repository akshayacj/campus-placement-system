package com.college.placement.dto;

/**
 * Immutable DTO record representing branch information returned by API endpoints.
 */
public record BranchResponse(
        Long id,
        String code,
        String name
) {}


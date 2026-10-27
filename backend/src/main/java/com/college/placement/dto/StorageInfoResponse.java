package com.college.placement.dto;

/**
 * Inspection DTO returning active storage service details for live tech talk switching.
 */
public record StorageInfoResponse(
        String activeImplementation,
        String storageType,
        boolean enabled
) {}


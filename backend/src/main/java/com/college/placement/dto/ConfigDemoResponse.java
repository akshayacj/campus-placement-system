package com.college.placement.dto;

import com.college.placement.config.PlacementProperties;
import java.util.List;

/**
 * Inspection DTO returning active profiles and masked placement properties.
 */
public record ConfigDemoResponse(
        List<String> activeProfiles,
        PlacementProperties properties
) {}


package com.college.placement.controller;

import com.college.placement.config.PlacementProperties;
import com.college.placement.dto.ConfigDemoResponse;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * TECH TALK: Externalized Configuration Inspection
 * Demonstrates @ConfigurationProperties binding and active profiles live.
 */
@RestController
@RequestMapping("/api/v1/demo/config")
public class ConfigDemoController {

    private final PlacementProperties placementProperties;
    private final Environment environment;

    public ConfigDemoController(PlacementProperties placementProperties, Environment environment) {
        this.placementProperties = placementProperties;
        this.environment = environment;
    }

    @GetMapping
    public ConfigDemoResponse getConfig() {
        List<String> activeProfiles = Arrays.asList(
                environment.getActiveProfiles().length > 0 ? environment.getActiveProfiles() : environment.getDefaultProfiles()
        );
        return new ConfigDemoResponse(activeProfiles, placementProperties.withMaskedSecret());
    }
}


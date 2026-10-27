package com.college.placement.demo.config;

import com.college.placement.config.PlacementProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TECH TALK: @Value vs @ConfigurationProperties
 *
 * | Feature                  | @Value ("SpEL / Flat")             | @ConfigurationProperties ("Structured") |
 * | :---                     | :---                               | :---                                    |
 * | Type Safety              | Weak (individual primitives/str)  | Strong (hierarchical Java records)      |
 * | Relaxed Binding          | Limited / manual                   | Full (kebab-case, camelCase, UPPERCASE) |
 * | JSR-380 Validation       | No built-in validation             | Yes (@Validated, @NotNull, @Min, etc.)  |
 * | IDE Auto-Completion      | None                               | Full via configuration metadata         |
 * | SpEL Expression Support  | Yes (#{...})                       | No                                      |
 */
@Component
public class ConfigComparisonDemo {

    // Approach 1: @Value - fine for one-off properties, but fragile & repetitive across large configurations
    @Value("${placement.policy.offer-response-window-hours:72}")
    private int offerWindowViaValue;

    // Approach 2: @ConfigurationProperties - recommended: type-safe, validated record
    private final PlacementProperties placementProperties;

    public ConfigComparisonDemo(PlacementProperties placementProperties) {
        this.placementProperties = placementProperties;
    }

    public int getOfferWindowViaValue() {
        return offerWindowViaValue;
    }

    public int getOfferWindowViaProperties() {
        return placementProperties.policy().offerResponseWindowHours();
    }
}


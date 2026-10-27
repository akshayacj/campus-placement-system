package com.college.placement.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TECH TALK: Relaxed Binding Demonstration
 *
 * <p>Spring Boot's "relaxed binding" allows environment variables written in
 * SCREAMING_SNAKE_CASE to map to camelCase or kebab-case property names.
 *
 * <p>Mapping table (all equivalent):
 * <pre>
 *   YAML key           : placement.policy.offer-response-window-hours
 *   Java property      : placement.policy.offerResponseWindowHours
 *   Env variable       : PLACEMENT_POLICY_OFFER_RESPONSE_WINDOW_HOURS
 * </pre>
 *
 * <p>This test uses {@link ApplicationContextRunner} to:
 * <ol>
 *   <li>Set a baseline YAML-style value of 72 via {@code withPropertyValues}.</li>
 *   <li>Add a {@link SystemEnvironmentPropertySource} with higher priority that
 *       overrides it using the env-var format (SCREAMING_SNAKE_CASE).</li>
 *   <li>Assert the bound value is 48, proving relaxed binding resolved it.</li>
 * </ol>
 *
 * <p>Note: the env-var key uses underscores as word separators for each segment
 * of the camelCase property name:
 *   offerResponseWindowHours → OFFER_RESPONSE_WINDOW_HOURS
 */
class RelaxedBindingTest {

    @EnableConfigurationProperties(PlacementProperties.class)
    static class TestConfig {}

    @Test
    void environmentVariableFormatOverridesYamlValueViaRelaxedBinding() {
        new ApplicationContextRunner()
                // Baseline properties (equivalent to application.yml values)
                .withPropertyValues(
                        "placement.email.allowed-domain=college.edu.in",
                        "placement.policy.offer-response-window-hours=72",   // baseline: 72
                        "placement.policy.dream-ctc-threshold-lpa=10.0",
                        "placement.jwt.secret=test-secret",
                        "placement.storage.base-dir=./test-dir"
                )
                // Simulate an environment variable override with higher priority.
                // Key insight: PLACEMENT_POLICY_OFFER_RESPONSE_WINDOW_HOURS uses
                // underscores between each word of the camelCase name segment.
                // Spring Boot's RelaxedPropertyResolver maps:
                //   PLACEMENT_POLICY_OFFER_RESPONSE_WINDOW_HOURS  →  placement.policy.offerResponseWindowHours
                .withInitializer(context -> {
                    Map<String, Object> envMap = Map.of(
                            // Correct SCREAMING_SNAKE_CASE: one underscore per camelCase word boundary
                            "PLACEMENT_POLICY_OFFER_RESPONSE_WINDOW_HOURS", "48"
                    );
                    context.getEnvironment().getPropertySources().addFirst(
                            new SystemEnvironmentPropertySource(
                                    StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                                    envMap
                            ));
                })
                .withUserConfiguration(TestConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(PlacementProperties.class);
                    PlacementProperties props = context.getBean(PlacementProperties.class);

                    // Must be 48 (from env var), NOT 72 (from withPropertyValues baseline).
                    // This proves relaxed binding resolved PLACEMENT_POLICY_OFFER_RESPONSE_WINDOW_HOURS
                    // to placement.policy.offerResponseWindowHours.
                    assertThat(props.policy().offerResponseWindowHours())
                            .as("Relaxed binding should map env var format to camelCase property")
                            .isEqualTo(48);
                });
    }
}

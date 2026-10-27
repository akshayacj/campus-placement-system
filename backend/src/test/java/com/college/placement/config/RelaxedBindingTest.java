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
 * Proves that environment variable naming format (UPPERCASE_UNDERSCORE):
 *   PLACEMENT_POLICY_OFFERRESPONSEWINDOWHOURS
 * seamlessly maps to record property:
 *   placement.policy.offerResponseWindowHours.
 */
class RelaxedBindingTest {

    @EnableConfigurationProperties(PlacementProperties.class)
    static class TestConfig {}

    @Test
    void environmentVariableFormatOverridesYamlValueViaRelaxedBinding() {
        new ApplicationContextRunner()
                .withPropertyValues(
                        "placement.email.allowed-domain=college.edu.in",
                        "placement.policy.offer-response-window-hours=72",
                        "placement.policy.dream-ctc-threshold-lpa=10.0",
                        "placement.jwt.secret=test-secret",
                        "placement.storage.base-dir=./test-dir"
                )
                .withInitializer(context -> {
                    Map<String, Object> envMap = Map.of(
                            "PLACEMENT_POLICY_OFFERRESPONSEWINDOWHOURS", "48"
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
                    assertThat(props.policy().offerResponseWindowHours()).isEqualTo(48);
                });
    }
}


package com.college.placement.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TECH TALK: Fail-Fast Configuration Validation
 * Proves that starting the application with invalid configuration constraints
 * (e.g., offerResponseWindowHours = -1 violating @Min(1) / @Positive)
 * fails immediately during container startup rather than failing silently or midway through business operations.
 */
class ConfigurationValidationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @EnableConfigurationProperties(PlacementProperties.class)
    static class TestConfig {}

    @Test
    void negativeOfferWindowFailsFastAtStartup() {
        runner.withPropertyValues(
                "placement.policy.offer-response-window-hours=-1",
                "placement.policy.dream-ctc-threshold-lpa=10.0",
                "placement.email.allowed-domain=college.edu.in",
                "placement.jwt.secret=some-test-secret",
                "placement.storage.base-dir=./test-storage"
        ).run(context -> {
            assertThat(context).hasFailed();
            assertThat(context.getStartupFailure())
                    .hasRootCauseInstanceOf(BindValidationException.class);
            assertThat(context.getStartupFailure())
                    .getRootCause()
                    .hasMessageContaining("offerResponseWindowHours");
        });
    }
}


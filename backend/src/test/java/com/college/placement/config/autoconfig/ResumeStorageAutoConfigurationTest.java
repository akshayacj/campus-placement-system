package com.college.placement.config.autoconfig;

import com.college.placement.demo.storage.InMemoryResumeStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TECH TALK: ApplicationContextRunner Test for Auto-Configuration
 * Verifies standard auto-configuration behavior in isolation:
 * 1. Default bean created
 * 2. Backs off on user bean presence
 * 3. Feature toggle disabled removes bean
 */
class ResumeStorageAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ResumeStorageAutoConfiguration.class));

    @Test
    void defaultBeanCreatedWhenNoUserBeanPresent() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ResumeStorageService.class);
            assertThat(context.getBean(ResumeStorageService.class))
                    .isInstanceOf(LocalDiskResumeStorageService.class);
        });
    }

    @Test
    void defaultBeanBacksOffWhenUserBeanIsRegistered() {
        contextRunner
                .withUserConfiguration(InMemoryResumeStorageService.class)
                .withPropertyValues("spring.profiles.active=test-storage")
                .run(context -> {
                    assertThat(context).hasSingleBean(ResumeStorageService.class);
                    assertThat(context.getBean(ResumeStorageService.class))
                            .isInstanceOf(InMemoryResumeStorageService.class);
                });
    }

    @Test
    void beanAbsentWhenStoragePropertyDisabled() {
        contextRunner
                .withPropertyValues("placement.storage.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(ResumeStorageService.class));
    }
}

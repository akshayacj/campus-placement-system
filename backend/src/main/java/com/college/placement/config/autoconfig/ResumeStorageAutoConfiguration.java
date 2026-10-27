package com.college.placement.config.autoconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * TECH TALK: Custom Auto-Configuration Pattern
 * Registered in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports.
 *
 * Demonstrates the 3 standard condition guards used across Spring Boot starters:
 * 1. @ConditionalOnClass: True only if java.nio.file.Files is present.
 * 2. @ConditionalOnProperty: Configurable feature toggle placement.storage.enabled.
 * 3. @ConditionalOnMissingBean: Yields if user provides their own ResumeStorageService bean.
 */
@AutoConfiguration
@ConditionalOnClass(java.nio.file.Files.class)
@ConditionalOnProperty(prefix = "placement.storage", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResumeStorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ResumeStorageService.class)
    public ResumeStorageService localDiskResumeStorageService(
            @Value("${placement.storage.base-dir:./target/storage-demo}") String baseDir) {
        return new LocalDiskResumeStorageService(baseDir);
    }
}


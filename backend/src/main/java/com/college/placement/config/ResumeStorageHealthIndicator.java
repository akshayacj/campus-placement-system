package com.college.placement.config;

import com.college.placement.config.autoconfig.LocalDiskResumeStorageService;
import com.college.placement.config.autoconfig.ResumeStorageService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TECH TALK: Custom HealthIndicator
 * Contributes to /actuator/health and readiness probe group.
 * Checks whether the resume storage directory is present and writable on disk.
 */
@Component("resumeStorage")
public class ResumeStorageHealthIndicator implements HealthIndicator {

    private final ObjectProvider<ResumeStorageService> storageServiceProvider;

    public ResumeStorageHealthIndicator(ObjectProvider<ResumeStorageService> storageServiceProvider) {
        this.storageServiceProvider = storageServiceProvider;
    }

    @Override
    public Health health() {
        ResumeStorageService service = storageServiceProvider.getIfAvailable();
        if (service == null) {
            return Health.down()
                    .withDetail("storage", "disabled")
                    .withDetail("reason", "No ResumeStorageService bean active")
                    .build();
        }

        if (service instanceof LocalDiskResumeStorageService diskService) {
            Path dir = diskService.getStorageDir();
            File dirFile = dir.toFile();
            boolean exists = Files.exists(dir);
            boolean writable = Files.isWritable(dir);

            if (exists && writable) {
                return Health.up()
                        .withDetail("type", "LOCAL_DISK")
                        .withDetail("path", dir.toAbsolutePath().toString())
                        .withDetail("writable", true)
                        .withDetail("usableSpaceBytes", dirFile.getUsableSpace())
                        .build();
            } else {
                return Health.down()
                        .withDetail("type", "LOCAL_DISK")
                        .withDetail("path", dir.toAbsolutePath().toString())
                        .withDetail("exists", exists)
                        .withDetail("writable", writable)
                        .build();
            }
        }

        return Health.up()
                .withDetail("type", service.getStorageType())
                .withDetail("status", "operational")
                .build();
    }
}


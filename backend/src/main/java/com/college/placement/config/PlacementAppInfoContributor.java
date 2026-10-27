package com.college.placement.config;

import com.college.placement.config.autoconfig.ResumeStorageService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * TECH TALK: Custom InfoContributor
 * Appends custom metadata to /actuator/info: application name, version,
 * and active storage implementation.
 */
@Component
public class PlacementAppInfoContributor implements InfoContributor {

    private final ObjectProvider<ResumeStorageService> storageServiceProvider;

    public PlacementAppInfoContributor(ObjectProvider<ResumeStorageService> storageServiceProvider) {
        this.storageServiceProvider = storageServiceProvider;
    }

    @Override
    public void contribute(Info.Builder builder) {
        ResumeStorageService service = storageServiceProvider.getIfAvailable();
        String activeStorage = service != null ? service.getClass().getSimpleName() : "DISABLED";

        builder.withDetail("app", Map.of(
                "name", "Campus Placement & Recruitment Portal",
                "version", "1.0.0-SNAPSHOT",
                "capstone", "TTT-JFS-01",
                "activeStorageImplementation", activeStorage
        ));
    }
}


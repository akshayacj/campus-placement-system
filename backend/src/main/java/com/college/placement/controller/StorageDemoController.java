package com.college.placement.controller;

import com.college.placement.config.autoconfig.ResumeStorageService;
import com.college.placement.dto.StorageInfoResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TECH TALK: Storage Auto-Configuration Switch Endpoint
 * Inspects whether LocalDiskResumeStorageService or InMemoryResumeStorageService is active.
 */
@RestController
@RequestMapping("/api/v1/demo/storage")
public class StorageDemoController {

    private final ObjectProvider<ResumeStorageService> storageServiceProvider;

    public StorageDemoController(ObjectProvider<ResumeStorageService> storageServiceProvider) {
        this.storageServiceProvider = storageServiceProvider;
    }

    @GetMapping
    public StorageInfoResponse getStorageInfo() {
        ResumeStorageService service = storageServiceProvider.getIfAvailable();
        if (service == null) {
            return new StorageInfoResponse("NONE", "DISABLED", false);
        }
        return new StorageInfoResponse(
                service.getClass().getSimpleName(),
                service.getStorageType(),
                true
        );
    }
}


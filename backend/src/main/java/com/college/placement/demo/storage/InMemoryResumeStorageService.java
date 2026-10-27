package com.college.placement.demo.storage;

import com.college.placement.config.autoconfig.ResumeStorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TECH TALK: User Bean Back-off Demonstration
 * Active when profile 'test-storage' is enabled.
 * Proves that @ConditionalOnMissingBean backs off the default auto-configured bean.
 */
@Component
@Profile("test-storage")
public class InMemoryResumeStorageService implements ResumeStorageService {

    private final Map<String, byte[]> memoryStore = new ConcurrentHashMap<>();

    @Override
    public String save(String filename, byte[] content) {
        memoryStore.put(filename, content);
        return filename;
    }

    @Override
    public byte[] load(String filename) {
        return memoryStore.getOrDefault(filename, new byte[0]);
    }

    @Override
    public boolean delete(String filename) {
        return memoryStore.remove(filename) != null;
    }

    @Override
    public String getStorageType() {
        return "IN_MEMORY";
    }
}


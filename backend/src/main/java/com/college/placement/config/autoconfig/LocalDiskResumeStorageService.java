package com.college.placement.config.autoconfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Default auto-configured resume storage writing files to local disk.
 */
public class LocalDiskResumeStorageService implements ResumeStorageService {

    private final Path storageDir;

    public LocalDiskResumeStorageService(String baseDir) {
        this.storageDir = Paths.get(baseDir);
        try {
            Files.createDirectories(this.storageDir);
        } catch (IOException ignored) {}
    }

    @Override
    public String save(String filename, byte[] content) {
        String storedName = UUID.randomUUID() + "_" + filename;
        try {
            Files.write(storageDir.resolve(storedName), content);
            return storedName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save resume", e);
        }
    }

    @Override
    public byte[] load(String filename) {
        try {
            return Files.readAllBytes(storageDir.resolve(filename));
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            return Files.deleteIfExists(storageDir.resolve(filename));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getStorageType() {
        return "LOCAL_DISK";
    }

    public Path getStorageDir() {
        return storageDir;
    }
}


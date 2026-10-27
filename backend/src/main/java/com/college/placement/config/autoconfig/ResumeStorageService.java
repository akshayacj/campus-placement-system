package com.college.placement.config.autoconfig;

/**
 * Contract for storing and retrieving candidate resume documents.
 */
public interface ResumeStorageService {
    String save(String filename, byte[] content);
    byte[] load(String filename);
    boolean delete(String filename);
    String getStorageType();
}


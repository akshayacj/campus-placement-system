package com.college.placement.demo.beans;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TECH TALK: @Repository Stereotype
 * Meta-annotated with @Component. In addition to component scanning,
 * @Repository enables automatic PersistenceExceptionTranslationPostProcessor,
 * converting vendor exceptions into Spring's DataAccessException hierarchy.
 */
@Repository
public class DemoInMemoryRepository {

    private final Map<String, String> dataStore = new ConcurrentHashMap<>();

    public void save(String key, String value) {
        dataStore.put(key, value);
    }

    public String find(String key) {
        return dataStore.get(key);
    }
}


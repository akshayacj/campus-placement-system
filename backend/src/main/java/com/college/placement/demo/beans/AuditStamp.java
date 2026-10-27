package com.college.placement.demo.beans;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

/**
 * TECH TALK: @Component (Root Stereotype)
 * Generic Spring-managed component. All other stereotypes (@Service, @Repository,
 * @Controller) are meta-annotated with @Component and scanned uniformly.
 */
@Component
public class AuditStamp {

    public String generateStamp() {
        return "STAMP-" + UUID.randomUUID().toString().substring(0, 8) + "-" + Instant.now();
    }
}


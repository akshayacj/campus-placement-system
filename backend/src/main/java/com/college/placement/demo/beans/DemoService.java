package com.college.placement.demo.beans;

import org.springframework.stereotype.Service;

/**
 * TECH TALK: @Service Stereotype
 * Meta-annotated with @Component. Conveys business service semantics and
 * serves as the conventional layer for transaction demarcation (@Transactional).
 */
@Service
public class DemoService {

    private final AuditStamp auditStamp;

    public DemoService(AuditStamp auditStamp) {
        this.auditStamp = auditStamp;
    }

    public String processAction(String action) {
        return "Processed " + action + " with " + auditStamp.generateStamp();
    }
}


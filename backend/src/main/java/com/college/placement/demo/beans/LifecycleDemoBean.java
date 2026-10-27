package com.college.placement.demo.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TECH TALK: Bean Lifecycle Callbacks
 * Demonstrates the exact order of Spring bean lifecycle events:
 * 1. Constructor -> 2. Injected -> 3. @PostConstruct -> 4. InitializingBean -> 5. Ready -> PreDestroy.
 */
@Component
public class LifecycleDemoBean implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(LifecycleDemoBean.class);
    private AuditStamp auditStamp;

    public LifecycleDemoBean() {
        log.info("[LIFECYCLE 1/5] Constructor called");
    }

    @Autowired
    public void setAuditStamp(AuditStamp auditStamp) {
        this.auditStamp = auditStamp;
        log.info("[LIFECYCLE 2/5] Dependency injected via setter/field");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("[LIFECYCLE 3/5] @PostConstruct initialization callback");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("[LIFECYCLE 4/5] InitializingBean.afterPropertiesSet callback");
        log.info("[LIFECYCLE 5/5] Bean is fully initialized and ready");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("[LIFECYCLE SHUTDOWN] @PreDestroy cleanup callback");
    }
}


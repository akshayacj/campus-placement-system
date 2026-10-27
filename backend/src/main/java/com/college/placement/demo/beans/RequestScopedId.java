package com.college.placement.demo.beans;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import java.util.UUID;

/**
 * TECH TALK: Request Scope
 * A distinct instance is created per HTTP request and destroyed when the request completes.
 */
@Component
@RequestScope
public class RequestScopedId {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

    public int getIdentityHashCode() {
        return System.identityHashCode(this);
    }
}


package com.college.placement.dto;

/**
 * Record holding state and identity hash codes to visibly demonstrate bean scopes.
 */
public record ScopesDemoResponse(
        int singletonHashCode,
        int singletonCount,
        int prototypeHashCode,
        int prototypeCount,
        int requestScopedHashCode,
        String requestScopedId
) {}


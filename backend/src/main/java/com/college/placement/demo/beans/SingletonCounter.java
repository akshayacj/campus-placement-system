package com.college.placement.demo.beans;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TECH TALK: Singleton Scope (Default)
 * Exactly one instance created per Spring ApplicationContext.
 */
@Component
public class SingletonCounter {

    private final AtomicInteger counter = new AtomicInteger(0);

    public int incrementAndGet() {
        return counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }

    public int getIdentityHashCode() {
        return System.identityHashCode(this);
    }
}


package com.college.placement.demo.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TECH TALK: Prototype Scope
 * A new instance is created every time the bean is retrieved from the container.
 */
@Component
@Scope("prototype")
public class PrototypeCounter {

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


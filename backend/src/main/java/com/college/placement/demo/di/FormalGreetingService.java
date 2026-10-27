package com.college.placement.demo.di;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * TECH TALK: @Primary Resolution
 * Marked with @Primary as the default bean when multiple candidates exist
 * without an explicit @Qualifier at the injection site.
 */
@Service
@Primary
public class FormalGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Good day, " + name + ". Welcome to the Campus Placement Portal.";
    }
}


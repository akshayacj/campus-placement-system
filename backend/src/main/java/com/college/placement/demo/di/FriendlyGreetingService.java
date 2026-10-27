package com.college.placement.demo.di;

import org.springframework.stereotype.Service;

/**
 * TECH TALK: @Qualifier Target
 * Injected selectively when consumer specifies @Qualifier("friendlyGreetingService").
 */
@Service("friendlyGreetingService")
public class FriendlyGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        return "Hey " + name + "! Ready for placements? Let's get started!";
    }
}


package com.college.placement.demo.di;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * TECH TALK: Constructor Injection (Best Practice)
 * Shows recommended DI pattern: final fields, explicit dependencies,
 * immutability, thread safety, and effortless unit testing without Spring.
 */
@RestController
@RequestMapping("/api/v1/demo/di/constructor")
public class ConstructorInjectedController {

    private final GreetingService primaryGreetingService;
    private final GreetingService qualifiedGreetingService;

    public ConstructorInjectedController(
            GreetingService primaryGreetingService,
            @Qualifier("friendlyGreetingService") GreetingService qualifiedGreetingService) {
        this.primaryGreetingService = primaryGreetingService;
        this.qualifiedGreetingService = qualifiedGreetingService;
    }

    @GetMapping
    public Map<String, String> getGreetings(
            @RequestParam(defaultValue = "Candidate") String name) {
        return Map.of(
                "style", "constructor-injection",
                "primaryGreeting", primaryGreetingService.greet(name),
                "qualifiedGreeting", qualifiedGreetingService.greet(name)
        );
    }
}


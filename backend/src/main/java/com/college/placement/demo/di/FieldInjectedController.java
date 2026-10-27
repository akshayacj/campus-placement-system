package com.college.placement.demo.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * TECH TALK: Field Injection (Anti-pattern / Discouraged)
 * Demonstrates field injection using @Autowired directly on private fields.
 *
 * WHY FIELD INJECTION IS DISCOURAGED:
 * 1. Immutability violation: Fields cannot be final, allowing unwanted state mutations.
 * 2. Testing barrier: Cannot instantiate with plain Java 'new' in unit tests;
 *    requires reflection or full Spring container setup.
 * 3. Hidden dependencies: Class contract hides required collaborators from callers.
 * 4. Circular dependency risk: Hides circular dependencies until runtime crash.
 */
@RestController
@RequestMapping("/api/v1/demo/di/field")
public class FieldInjectedController {

    @Autowired
    private GreetingService primaryGreetingService;

    @Autowired
    @Qualifier("friendlyGreetingService")
    private GreetingService qualifiedGreetingService;

    @GetMapping
    public Map<String, String> getGreetings(
            @RequestParam(defaultValue = "Candidate") String name) {
        return Map.of(
                "style", "field-injection",
                "primaryGreeting", primaryGreetingService.greet(name),
                "qualifiedGreeting", qualifiedGreetingService.greet(name)
        );
    }
}


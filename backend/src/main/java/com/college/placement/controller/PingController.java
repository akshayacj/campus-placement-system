package com.college.placement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * TECH TALK: Minimal Web Endpoint
 * Simple ping endpoint to verify that Spring MVC (spring-boot-starter-web)
 * and the embedded Tomcat web server are auto-configured and operational.
 */
@RestController
@RequestMapping("/api/v1")
public class PingController {

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok");
    }
}

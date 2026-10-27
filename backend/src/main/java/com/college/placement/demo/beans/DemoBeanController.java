package com.college.placement.demo.beans;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * TECH TALK: @RestController Stereotype
 * Meta-annotated with @Controller and @ResponseBody.
 * Identifies this component as an HTTP request handler producing JSON responses.
 */
@RestController
@RequestMapping("/api/v1/demo/stereotypes")
public class DemoBeanController {

    private final DemoService demoService;

    public DemoBeanController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping
    public Map<String, String> getStereotypeInfo() {
        return Map.of(
                "controller", "DemoBeanController (@RestController)",
                "service", "DemoService (@Service)",
                "result", demoService.processAction("stereotype-demo")
        );
    }
}


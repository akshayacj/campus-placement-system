package com.college.outside;

import org.springframework.stereotype.Component;

/**
 * TECH TALK: Package Scanning Root Boundary
 * This bean is placed outside 'com.college.placement'.
 * Spring's default @ComponentScan will NEVER discover this bean because it only
 * scans the main application class's package and its sub-packages.
 */
@Component
public class ComponentScanProofBean {

    public String getProofMessage() {
        return "I am located outside the scan root: com.college.outside";
    }
}


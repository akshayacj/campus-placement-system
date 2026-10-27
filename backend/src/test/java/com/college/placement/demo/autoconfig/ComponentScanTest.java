package com.college.placement.demo.autoconfig;

import com.college.outside.ComponentScanProofBean;
import com.college.placement.PlacementApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TECH TALK: Demonstrating ComponentScan Boundaries
 * Proves that sibling/parent packages outside the scan root are skipped by default,
 * but picked up when profile 'scan-demo' enables OutsideScanConfig.
 */
class ComponentScanTest {

    @Test
    void outsideBeanNotPickedUpByDefaultScan() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(PlacementApplication.class)
                .profiles("test")
                .properties("server.port=0", "spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=none")
                .run()) {
            assertThat(context.getBeansOfType(ComponentScanProofBean.class)).isEmpty();
        }
    }

    @Test
    void outsideBeanPickedUpWhenScanDemoProfileActive() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(PlacementApplication.class)
                .profiles("test", "scan-demo")
                .properties("server.port=0", "spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=none")
                .run()) {
            assertThat(context.getBeansOfType(ComponentScanProofBean.class)).hasSize(1);
        }
    }
}


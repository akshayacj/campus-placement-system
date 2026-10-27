package com.college.placement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ====================================================================================
 * TECH TALK: Inside @SpringBootApplication
 * ====================================================================================
 * @SpringBootApplication is a meta-annotation that bundles three core annotations:
 *
 * 1. @SpringBootConfiguration:
 *    A specialized form of Spring's @Configuration. Tags this class as a primary source
 *    of bean definitions for the ApplicationContext. Enables cglib proxying for @Bean
 *    methods (proxyBeanMethods = true by default).
 *
 * 2. @EnableAutoConfiguration:
 *    Tells Spring Boot to look at META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 *    on the classpath and conditionally register beans based on matched dependencies,
 *    classes present on the classpath, system properties, and missing user beans.
 *
 * 3. @ComponentScan:
 *    Directs Spring to recursively scan the package of this class (com.college.placement)
 *    and all of its sub-packages for Spring stereotypes: @Component, @Service,
 *    @Repository, @Controller, @RestController, and @Configuration.
 *    Any classes outside this scan root (e.g., com.college.outside) will NOT be
 *    discovered unless explicitly imported or configured via scanBasePackages.
 * ====================================================================================
 */
@SpringBootApplication
public class PlacementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementApplication.class, args);
    }
}

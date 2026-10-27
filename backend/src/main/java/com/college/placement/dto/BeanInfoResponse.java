package com.college.placement.dto;

/**
 * Inspection DTO returning bean name, class, and scope registered in ApplicationContext.
 */
public record BeanInfoResponse(
        String beanName,
        String beanClass,
        String scope
) {}


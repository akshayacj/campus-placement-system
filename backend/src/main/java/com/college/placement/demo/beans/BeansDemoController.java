package com.college.placement.demo.beans;

import com.college.placement.dto.BeanInfoResponse;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * TECH TALK: ApplicationContext Inspection
 * Reads the live Spring ApplicationContext to list all registered beans
 * belonging to com.college.placement and their respective lifecycle scopes.
 */
@RestController
@RequestMapping("/api/v1/demo/beans")
public class BeansDemoController {

    private final ConfigurableApplicationContext applicationContext;

    public BeansDemoController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping
    public List<BeanInfoResponse> listPlacementBeans() {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        List<BeanInfoResponse> result = new ArrayList<>();

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class<?> beanType = applicationContext.getType(beanName);
            if (beanType != null && beanType.getPackageName().startsWith("com.college.placement")) {
                Class<?> userClass = ClassUtils.getUserClass(beanType);
                String scope = "singleton";
                if (beanFactory.containsBeanDefinition(beanName)) {
                    String definedScope = beanFactory.getBeanDefinition(beanName).getScope();
                    if (definedScope != null && !definedScope.isBlank()) {
                        scope = definedScope;
                    }
                }
                result.add(new BeanInfoResponse(beanName, userClass.getName(), scope));
            }
        }
        return result;
    }
}

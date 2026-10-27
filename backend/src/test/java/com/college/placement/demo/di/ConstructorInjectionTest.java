package com.college.placement.demo.di;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * TECH TALK: Unit Testing Constructor vs Field Injection
 * Shows how constructor injection enables sub-millisecond unit tests with pure Java 'new',
 * whereas field-injected classes cannot receive mocks without Spring or reflection.
 */
class ConstructorInjectionTest {

    @Test
    void canInstantiateAndTestWithPlainJavaAndMocksWithoutSpring() {
        // Arrange: Pure Java instantiation, no Spring Context needed
        GreetingService mockPrimary = Mockito.mock(GreetingService.class);
        GreetingService mockQualified = Mockito.mock(GreetingService.class);

        when(mockPrimary.greet("Alice")).thenReturn("Formal Alice");
        when(mockQualified.greet("Alice")).thenReturn("Friendly Alice");

        // Act
        ConstructorInjectedController controller =
                new ConstructorInjectedController(mockPrimary, mockQualified);
        Map<String, String> response = controller.getGreetings("Alice");

        // Assert
        assertThat(response).containsEntry("style", "constructor-injection");
        assertThat(response).containsEntry("primaryGreeting", "Formal Alice");
        assertThat(response).containsEntry("qualifiedGreeting", "Friendly Alice");
    }

    @Test
    @Disabled("Field-injected controller cannot be instantiated with mocks via constructor. " +
            "Calling getGreetings() causes NullPointerException because fields remain null.")
    void fieldInjectedControllerFailsWithoutSpringOrReflection() {
        // Notice: No way to pass mocks into private fields without Spring or reflection
        FieldInjectedController controller = new FieldInjectedController();
        // This line throws NullPointerException if run:
        controller.getGreetings("Alice");
    }
}


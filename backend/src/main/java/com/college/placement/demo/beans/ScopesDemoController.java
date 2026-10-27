package com.college.placement.demo.beans;

import com.college.placement.dto.ScopesDemoResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TECH TALK: Spring Bean Scopes Demo
 * Demonstrates Singleton, Prototype, and Request scopes in action.
 *
 * THE "PROTOTYPE INJECTED INTO SINGLETON" PITFALL:
 * If a prototype bean is injected directly via constructor or field into a singleton bean,
 * the prototype is created ONLY ONCE when the singleton is instantiated.
 * Thereafter, the singleton holds that exact same prototype instance forever!
 * To get a new prototype on every request/invocation, use:
 * 1. ObjectProvider<PrototypeBean> (recommended modern Spring approach)
 * 2. @Lookup method injection
 * 3. Scoped proxy (proxyMode = ScopedProxyMode.TARGET_CLASS)
 */
@RestController
@RequestMapping("/api/v1/demo/scopes")
public class ScopesDemoController {

    private final SingletonCounter singletonCounter;
    private final ObjectProvider<PrototypeCounter> prototypeCounterProvider;
    private final RequestScopedId requestScopedId;

    public ScopesDemoController(
            SingletonCounter singletonCounter,
            ObjectProvider<PrototypeCounter> prototypeCounterProvider,
            RequestScopedId requestScopedId) {
        this.singletonCounter = singletonCounter;
        this.prototypeCounterProvider = prototypeCounterProvider;
        this.requestScopedId = requestScopedId;
    }

    @GetMapping
    public ScopesDemoResponse getScopesDemo() {
        // Prototype retrieved fresh via ObjectProvider to avoid singleton capture
        PrototypeCounter prototype = prototypeCounterProvider.getObject();

        return new ScopesDemoResponse(
                singletonCounter.getIdentityHashCode(),
                singletonCounter.incrementAndGet(),
                prototype.getIdentityHashCode(),
                prototype.incrementAndGet(),
                requestScopedId.getIdentityHashCode(),
                requestScopedId.getId()
        );
    }
}


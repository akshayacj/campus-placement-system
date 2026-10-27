package com.college.placement.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DemoControllersIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pingEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void constructorInjectedDiEndpointWorks() throws Exception {
        mockMvc.perform(get("/api/v1/demo/di/constructor").param("name", "Bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.style").value("constructor-injection"))
                .andExpect(jsonPath("$.primaryGreeting").value(containsString("Good day, Bob")))
                .andExpect(jsonPath("$.qualifiedGreeting").value(containsString("Hey Bob!")));
    }

    @Test
    void fieldInjectedDiEndpointWorks() throws Exception {
        mockMvc.perform(get("/api/v1/demo/di/field").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.style").value("field-injection"))
                .andExpect(jsonPath("$.primaryGreeting").value(containsString("Good day, Alice")))
                .andExpect(jsonPath("$.qualifiedGreeting").value(containsString("Hey Alice!")));
    }

    @Test
    void scopesEndpointCalledTwiceDemonstratesDifferences() throws Exception {
        // Call 1
        MvcResult res1 = mockMvc.perform(get("/api/v1/demo/scopes"))
                .andExpect(status().isOk())
                .andReturn();
        String json1 = res1.getResponse().getContentAsString();
        int singletonHash1 = JsonPath.read(json1, "$.singletonHashCode");
        int singletonCount1 = JsonPath.read(json1, "$.singletonCount");
        int protoHash1 = JsonPath.read(json1, "$.prototypeHashCode");
        String reqId1 = JsonPath.read(json1, "$.requestScopedId");

        // Call 2
        MvcResult res2 = mockMvc.perform(get("/api/v1/demo/scopes"))
                .andExpect(status().isOk())
                .andReturn();
        String json2 = res2.getResponse().getContentAsString();
        int singletonHash2 = JsonPath.read(json2, "$.singletonHashCode");
        int singletonCount2 = JsonPath.read(json2, "$.singletonCount");
        int protoHash2 = JsonPath.read(json2, "$.prototypeHashCode");
        String reqId2 = JsonPath.read(json2, "$.requestScopedId");

        // Assert Singleton: Same identity hashCode across requests, counter increases
        assertThat(singletonHash1).isEqualTo(singletonHash2);
        assertThat(singletonCount2).isGreaterThan(singletonCount1);

        // Assert Prototype: Different identity hashCode on every request via ObjectProvider
        assertThat(protoHash1).isNotEqualTo(protoHash2);

        // Assert RequestScope: Different UUID per HTTP request
        assertThat(reqId1).isNotEqualTo(reqId2);
    }

    @Test
    void beansDemoEndpointReturnsPlacementBeans() throws Exception {
        mockMvc.perform(get("/api/v1/demo/beans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.beanClass == 'com.college.placement.service.BranchService')].scope").value("singleton"));
    }

    @Test
    void configDemoEndpointReturnsMaskedSecretAndProfiles() throws Exception {
        mockMvc.perform(get("/api/v1/demo/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeProfiles").isArray())
                .andExpect(jsonPath("$.properties.jwt.secret").value(containsString("MASKED")));
    }

    @Test
    void storageDemoEndpointReturnsStorageDetails() throws Exception {
        mockMvc.perform(get("/api/v1/demo/storage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void datasourceDemoEndpointReturnsDataSourceDetails() throws Exception {
        mockMvc.perform(get("/api/v1/demo/datasource"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataSourceClass").value(containsString("HikariDataSource")));
    }

    @Test
    void actuatorHealthReturnsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}


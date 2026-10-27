package com.college.placement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BranchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBranchesReturnsSixSeededBranchesFromFlyway() throws Exception {
        mockMvc.perform(get("/api/v1/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].code").value("CSE"))
                .andExpect(jsonPath("$[1].code").value("ECE"))
                .andExpect(jsonPath("$[2].code").value("EEE"))
                .andExpect(jsonPath("$[3].code").value("MECH"))
                .andExpect(jsonPath("$[4].code").value("CIVIL"))
                .andExpect(jsonPath("$[5].code").value("IT"));
    }
}


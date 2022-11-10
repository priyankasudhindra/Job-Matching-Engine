package com.jobmatching.resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JobFinderResourceTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void shouldReturnExceptionWhenUserIdNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/findJobs/111"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn200WhenRecordIsFoundForGivenWorkerId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/findJobs/4"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\r\n" + " {\r\n" + " \"driverLicenseRequired\": false,\r\n"
                        + " \"requiredCertificates\": [\r\n"
                        + " \"The Encouraging Word Award\"\r\n" + " ],\r\n"
                        + " \"location\": {\r\n" + " \"longitude\": \"14.987061\",\r\n"
                        + "   \"latitude\": \"50.212725\"\r\n" + " },\r\n"
                        + " \"billRate\": \"$14.79\",\r\n" + " \"workersRequired\": 4,\r\n"
                        + " \"startDate\": \"2015-11-14\",\r\n"
                        + " \"about\": \"Magna commodo velit dolor aliquip exercitation. Esse irure duis eu duis veniam ea minim ex. Aliqua deserunt dolore officia do.\",\r\n"
                        + " \"jobTitle\": \"Director of First Impressions\",\r\n" + " \"company\": \"Nimon\",\r\n"
                        + " \"guid\": \"562f66aaf3cb186fc0776de9\",\r\n" + " \"jobId\": 27\r\n"
                        + " }\r\n" + "]"));
    }

    @Test
    void shouldReturnExceptionForBadUserId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/findJobs/null")).andExpect(status().isBadRequest());

    }

}

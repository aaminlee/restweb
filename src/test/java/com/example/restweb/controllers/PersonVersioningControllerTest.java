package com.example.restweb.controllers;

import com.example.restweb.model.versioning.PersonV1;
import com.example.restweb.model.versioning.PersonV2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonVersioningControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(PersonVersioningController.class).build();
    }

    @Test
    void personV1() {
        PersonV1 response = restTemplate.getForObject("/v1/person", PersonV1.class);
        assertEquals("Bob Charlie", response.getName());
    }

    @Test
    void personV2() {
        PersonV2 response = restTemplate.getForObject("/v2/person", PersonV2.class);
        assertEquals("Bob", response.getName().getFirstName());
        assertEquals("Charlie", response.getName().getLastName());
    }

    @Test
    void paramV1() {
        PersonV1 response = restTemplate.getForObject("/person/param?version={version}",
                PersonV1.class, "1");
        assertEquals("Bob Charlie", response.getName());
    }

    @Test
    void paramV2() {
        PersonV2 response = restTemplate.getForObject("/person/param?version={version}",
                PersonV2.class, "2");
        assertEquals("Bob", response.getName().getFirstName());
        assertEquals("Charlie", response.getName().getLastName());
    }

    @Test
    void headerV1() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-VERSION", "1");
        ResponseEntity<PersonV1> exchange = restTemplate.exchange("/person/header", HttpMethod.GET,
                new HttpEntity<>(headers), PersonV1.class);
        assertEquals("Bob Charlie", exchange.getBody().getName());
    }

    @Test
    void headerV2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-VERSION", "2");
        ResponseEntity<PersonV2> exchange = restTemplate.exchange("/person/header", HttpMethod.GET,
                new HttpEntity<>(headers), PersonV2.class);
        assertEquals("Bob", exchange.getBody().getName().getFirstName());
        assertEquals("Charlie", exchange.getBody().getName().getLastName());
    }

    @Test
    void producesV1() throws Exception {
        ResultActions perform = mockMvc.perform(get("/person/produces").accept(MediaTypes.HAL_JSON_VALUE)
                .accept("application/vnd.company.app-v1+json")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", containsString("Bob Charlie")));
    }

    @Test
    void producesV2() throws Exception {
        ResultActions perform = mockMvc.perform(get("/person/produces").accept(MediaTypes.HAL_JSON_VALUE)
                .accept("application/vnd.company.app-v2+json")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name.firstName", containsString("Bob")))
                .andExpect(jsonPath("$.name.lastName", containsString("Charlie")));

    }
}
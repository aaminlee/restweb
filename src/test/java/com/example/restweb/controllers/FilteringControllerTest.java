package com.example.restweb.controllers;

import com.example.restweb.model.SomeBean;
import com.example.restweb.model.SomeBeanStaticFiltering;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilteringControllerTest {

    private MockMvc mockMvc;

    private FilteringController filteringController;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        //mockMvc = MockMvcBuilders.standaloneSetup(filteringController).build();
    }

    @Test
    void retrieveSomeBean() throws Exception {
        SomeBeanStaticFiltering someBeanStaticFiltering = restTemplate
                .getForObject("/filtering-static", SomeBeanStaticFiltering.class);
        assertEquals(null, someBeanStaticFiltering.getValue1());
        assertEquals(null, someBeanStaticFiltering.getValue2());
        assertEquals("value3", someBeanStaticFiltering.getValue3());
    }

    @Test
    void retrieveSomeBeanList() {
        Object[] objects = restTemplate.getForObject("/filtering-list", Object[].class);
        SomeBean bean1 = (SomeBean) objects[0];
        System.out.println(bean1.getValue1());
    }

    @Test
    void retrieveSomeBeanDynamicFilter() {
    }
}
package com.example.restweb.controllers;

import com.example.restweb.model.SomeBean;
import com.example.restweb.model.SomeBeanStaticFiltering;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ExtendWith(SpringExtension.class)
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
        assertNull(someBeanStaticFiltering.getValue1());
        assertNull(someBeanStaticFiltering.getValue2());
        assertEquals("value3", someBeanStaticFiltering.getValue3());
    }

    @Test
    void retrieveSomeBeanList() {
        ResponseEntity<List<SomeBean>> rateResponse =
                restTemplate.exchange("/filtering-list",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<SomeBean>>() {});
        List<SomeBean> someBeans = rateResponse.getBody();

        assertNull(someBeans.get(0).getValue1());
        assertEquals(someBeans.get(0).getValue2(), "value2");
        assertEquals(someBeans.get(0).getValue3(), "value3");

        assertNull(someBeans.get(1).getValue1());
        assertEquals(someBeans.get(1).getValue2(), "value22");
        assertEquals(someBeans.get(1).getValue3(), "value33");

    }

    @Test
    void retrieveSomeBeanDynamicFilter() {

        ResponseEntity<SomeBean> rateResponse =
                restTemplate.exchange("/filtering-filter",
                        HttpMethod.GET, null, new ParameterizedTypeReference<SomeBean>() {});

        SomeBean someBean = rateResponse.getBody();
        assertEquals(someBean.getValue1(), "value1");
        assertEquals(someBean.getValue2(), "value2");
        assertNull(someBean.getValue3());
    }
}
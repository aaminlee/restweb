package com.example.restweb.controllers;

import com.example.restweb.model.SomeBean;
import com.example.restweb.model.SomeBeanStaticFiltering;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering-static")
    public SomeBeanStaticFiltering retrieveSomeBean() {
        return new SomeBeanStaticFiltering("value1", "value2", "value3");
    }

    @GetMapping("/filtering-list")
    public MappingJacksonValue retrieveSomeBeanList() {
        List<SomeBean> list = Arrays.asList(new SomeBean("value1", "value2", "value3"),
                new SomeBean("value11", "value22", "value33"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("value2", "value3");
        FilterProvider filters = new SimpleFilterProvider().addFilter("someBeanFilter", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    //Dynamic filtering
    @GetMapping("/filtering-filter")
    public MappingJacksonValue retrieveSomeBeanDynamicFilter() {
        SomeBean someBean = new SomeBean("value1", "value2", "value3");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("value1", "value2");
        FilterProvider filters = new SimpleFilterProvider().addFilter("someBeanFilter", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}

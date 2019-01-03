package com.example.restweb.controllers;

import com.example.restweb.model.versioning.Name;
import com.example.restweb.model.versioning.PersonV1;
import com.example.restweb.model.versioning.PersonV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonVersioningController {

    @GetMapping("v1/person")
    public PersonV1 personV1() {
        return new PersonV1("Bob Charlie");
    }

    //url : http://localhost:8080/v2/person
    @GetMapping("v2/person")
    public PersonV2 personV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    @GetMapping(value = "person/param", params = "version=1")
    public PersonV1 paramV1() {
        return new PersonV1("Bob Charlie");
    }

    //url : http://localhost:8080/person/param?version=2
    @GetMapping(value = "person/param", params = "version=2")
    public PersonV2 paramV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    @GetMapping(value = "person/header", headers = "X-API-VERSION=1")
    public PersonV1 headerV1() {
        return new PersonV1("Bob Charlie");
    }

    //headers
    //Key : X-API-VERSION
    //Value : 2
    @GetMapping(value = "person/header", headers = "X-API-VERSION=2")
    public PersonV2 headerV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    //headers
    //Key : Accept
    //Value : application/vnd.company.app-v1+json
    @GetMapping(value = "person/produces", produces = "application/vnd.company.app-v1+json")
    public PersonV1 producesV1() {
        return new PersonV1("Bob Charlie");
    }

    @GetMapping(value = "person/produces", produces = "application/vnd.company.app-v2+json")
    public PersonV2 producesV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }
}

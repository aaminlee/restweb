package com.example.restweb.controllers;

import com.example.restweb.model.HelloWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void helloWorld() {
        assertEquals("Hello World !", restTemplate.getForObject("/hello-world", String.class));
    }

    @Test
    void helloWorldBean() {
        HelloWorld helloWord = restTemplate.getForObject("/hello-world-bean", HelloWorld.class);
        assertEquals("Hello World !", helloWord.getMessage());
    }

    @Test
    void helloWorldPathVariable() {
        String resultMsg = restTemplate.getForObject("http://localhost/hello-world/path-variable/{name}", String.class, "Aamin");
        assertEquals("Hello World :  Aamin", resultMsg);
    }

    @Test
    void helloWorldInternationalized_DE() {

        Locale.setDefault(Locale.CANADA);

        switch (Locale.getDefault().getCountry()) {
            case "NL":
                assertEquals("Good Morning", restTemplate.getForObject("/hello-world-internationalized", String.class));
                break;
            case "DE":
                assertEquals("Guten morgen", restTemplate.getForObject("/hello-world-internationalized", String.class));
                break;
            case "FR":
                assertEquals("Bonjour", restTemplate.getForObject("/hello-world-internationalized", String.class));
                break;
            default:
                assertEquals("Good Morning", restTemplate.getForObject("/hello-world-internationalized", String.class));
                break;
        }

    }
}
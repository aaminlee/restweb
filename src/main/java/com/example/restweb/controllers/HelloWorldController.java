package com.example.restweb.controllers;

import com.example.restweb.model.HelloWorld;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    private MessageSource messageSource;

    public HelloWorldController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping(path = "hello-world")
    public String helloWorld() {
        return "Hello World !";
    }

    @GetMapping(path = "hello-world-bean")
    public HelloWorld helloWorldBean() {
        return new HelloWorld("Hello World !");
    }

    @GetMapping(path = "hello-world/path-variable/{name}")
    public String helloWorldPathVariable(@PathVariable("name") String nome) {
        return "Hello World :  " + nome;
    }

    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized() {
        return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
    }

}

package com.example.restweb.controllers;

import com.example.restweb.model.User;
import com.example.restweb.service.UserDaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserDaoService userDaoService;

    @InjectMocks
    private UserController userController;

    MockMvc mockMvc;
    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User(1, "Adam", new Date()),
                new User(2, "Eve", new Date()),
                new User(3, "Jack", new Date())
        );

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void retrieveAllUsers() throws Exception {
        when(userDaoService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Adam")));
    }

    @Test
    void retrieveUser() throws Exception {
        User user = new User(1, "TestUser", new Date());

        when(userDaoService.findOne(anyInt())).thenReturn(user);

        mockMvc.perform(get("/users/1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.links[0].href", is("http://localhost/users")));
    }

    @Test
    void createUser() throws Exception {
        User user = new User(100, "InsertUser", new Date());
        when(userDaoService.save(any(User.class))).thenReturn(user);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/users/100"));
    }

    @Test
    void deleteUser() throws Exception {
        when(userDaoService.findOne(anyInt())).thenReturn(users.get(1));

        mockMvc.perform(delete("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
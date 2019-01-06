package com.example.restweb.controllers;

import com.example.restweb.model.Post;
import com.example.restweb.model.User;
import com.example.restweb.repository.PostRepository;
import com.example.restweb.repository.UserRepository;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserJpaControllerTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    UserJpaController controller;

    private List<User> users;
    private List<Post> posts;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        posts = Arrays.asList(
                new Post(1, "First Test Post"),
                new Post(1, "Second Test Post")
        );

        User user1 = new User(1, "Test-A", new Date());
        user1.setPosts(posts);
        User user2 = new User(2, "Test-B", new Date());
        users = Arrays.asList(user1, user2);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void retrieveAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/jpa/users")
                .contentType("application/json"))
                .andExpect(content().string(containsString("Test-A")))
                .andExpect(content().string(containsString("Test-B")))
                .andExpect(status().isOk());
    }

    @Test
    void retrieveUser() throws Exception {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(users.get(1)));


        mockMvc.perform(get("/jpa/users/1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Test-B")))
                .andExpect(jsonPath("$.links[0].href", is("http://localhost/jpa/users")));
    }

    @Test
    void createUser() throws Exception {
        User user = new User(1, "Test-C", new Date());
        when(userRepository.save(any(User.class))).thenReturn(user);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String userJson = ow.writeValueAsString(user);

        mockMvc.perform(post("/jpa/users").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/jpa/users/1"));
    }

    @Test
    void deleteUser() throws Exception {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(users.get(1)));

        mockMvc.perform(delete("/jpa/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void retrievePostsByUserId() throws Exception {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(users.get(0)));

        mockMvc.perform(get("/jpa/users/1/posts").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description", is("First Test Post")))
                .andExpect(jsonPath("$[1].description", is("Second Test Post")));
    }

    @Test
    void createPostForUserId() throws Exception {
        Post testPost = new Post(1, "Adding Post");
        testPost.setUser(users.get(1));

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(users.get(1)));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String testPostJson = ow.writeValueAsString(testPost);

        mockMvc.perform(post("/jpa/users/1/posts").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testPostJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/jpa/users/1/posts/1"))
                .andDo(print());
    }
}
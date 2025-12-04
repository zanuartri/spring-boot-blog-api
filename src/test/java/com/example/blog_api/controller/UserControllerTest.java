package com.example.blog_api.controller;

import com.example.blog_api.dto.CreateUserRequest;
import com.example.blog_api.dto.UserDto;
import com.example.blog_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private UserDto sampleUser(Long id) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setName("User " + id);
        dto.setEmail("user" + id + "@example.com");
        dto.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        return dto;
    }

    @Test
    @DisplayName("GET /api/users returns 200 and list of users")
    void listUsers() throws Exception {
        given(userService.getAllUsers()).willReturn(List.of(sampleUser(1L), sampleUser(2L)));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", containsString("@example.com")));
    }

    @Test
    @DisplayName("GET /api/users/{id} returns 200")
    void getUser() throws Exception {
        given(userService.getUserById(10L)).willReturn(sampleUser(10L));

        mockMvc.perform(get("/api/users/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("User 10")));
    }

    @Test
    @DisplayName("POST /api/users returns 201, Location header and body")
    void createUser() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Charlie");
        req.setEmail("charlie@example.com");

        UserDto created = sampleUser(123L);
        created.setName(req.getName());
        created.setEmail(req.getEmail());
        given(userService.createUser(any(CreateUserRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/123"))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.email", is("charlie@example.com")));
    }

    @Test
    @DisplayName("PUT /api/users/{id} returns 200")
    void updateUser() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Updated");
        req.setEmail("updated@example.com");

        UserDto updated = sampleUser(55L);
        updated.setName("Updated");
        updated.setEmail("updated@example.com");
        given(userService.updateUser(eq(55L), any(CreateUserRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/users/{id}", 55)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(55)))
                .andExpect(jsonPath("$.name", is("Updated")));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} returns 204")
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(77L);

        mockMvc.perform(delete("/api/users/{id}", 77))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/users with blank email returns 400")
    void createUserValidationError() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("No Email");
        req.setEmail("");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}


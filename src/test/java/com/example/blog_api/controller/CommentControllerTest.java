package com.example.blog_api.controller;

import com.example.blog_api.dto.CommentDto;
import com.example.blog_api.dto.CreateCommentRequest;
import com.example.blog_api.service.CommentService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        CommentService commentService() {
            return Mockito.mock(CommentService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    private CommentDto sampleComment(Long id) {
        CommentDto dto = new CommentDto();
        dto.setId(id);
        dto.setText("Comment " + id);
        dto.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        return dto;
    }

    @Test
    @DisplayName("POST /api/comments returns 201, Location header and body")
    void createComment() throws Exception {
        CreateCommentRequest req = new CreateCommentRequest();
        req.setPostId(1L);
        req.setText("Nice post!");

        CommentDto created = sampleComment(123L);
        created.setText(req.getText());
        given(commentService.createComment(any(CreateCommentRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/comments/123"))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.text", is("Nice post!")));
    }

    @Test
    @DisplayName("GET /api/comments/{id} returns 200")
    void getComment() throws Exception {
        given(commentService.getCommentById(10L)).willReturn(sampleComment(10L));

        mockMvc.perform(get("/api/comments/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.text", containsString("Comment")));
    }

    @Test
    @DisplayName("GET /api/posts/{postId}/comments returns 200 and list")
    void listCommentsForPost() throws Exception {
        given(commentService.getCommentsByPostId(5L)).willReturn(List.of(sampleComment(1L), sampleComment(2L)));

        mockMvc.perform(get("/api/posts/{postId}/comments", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @DisplayName("DELETE /api/comments/{id} returns 204")
    void deleteComment() throws Exception {
        doNothing().when(commentService).deleteComment(77L);

        mockMvc.perform(delete("/api/comments/{id}", 77))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/comments with blank text returns 400")
    void createCommentValidationError() throws Exception {
        CreateCommentRequest req = new CreateCommentRequest();
        req.setPostId(1L);
        req.setText("");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}


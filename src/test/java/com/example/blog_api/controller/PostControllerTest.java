package com.example.blog_api.controller;

import com.example.blog_api.dto.CreatePostRequest;
import com.example.blog_api.dto.PostDto;
import com.example.blog_api.service.PostService;
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

@WebMvcTest(PostController.class)
class PostControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        PostService postService() {
            return Mockito.mock(PostService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    private PostDto samplePostDto(Long id) {
        PostDto dto = new PostDto();
        dto.setId(id);
        dto.setTitle("Sample Title " + id);
        dto.setContent("Sample Content");
        dto.setAuthorName("Alice");
        dto.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        return dto;
    }

    @Test
    @DisplayName("GET /api/posts returns 200 and list of posts")
    void listPosts() throws Exception {
        given(postService.getAllPosts()).willReturn(List.of(samplePostDto(1L), samplePostDto(2L)));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", containsString("Sample Title")));
    }

    @Test
    @DisplayName("GET /api/posts/{id} returns 200 and the post")
    void getPostById() throws Exception {
        given(postService.getPostById(10L)).willReturn(samplePostDto(10L));

        mockMvc.perform(get("/api/posts/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.authorName", is("Alice")));
    }

    @Test
    @DisplayName("POST /api/posts returns 201, Location header and body")
    void createPost() throws Exception {
        CreatePostRequest req = new CreatePostRequest();
        req.setTitle("New Post");
        req.setContent("Hello world");
        req.setAuthorId(1L);

        PostDto created = samplePostDto(123L);
        created.setTitle(req.getTitle());
        created.setContent(req.getContent());
        given(postService.createPost(any(CreatePostRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/posts/123"))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.title", is("New Post")));
    }

    @Test
    @DisplayName("PUT /api/posts/{id} returns 200 and updated body")
    void updatePost() throws Exception {
        CreatePostRequest req = new CreatePostRequest();
        req.setTitle("Updated Title");
        req.setContent("Updated Content");
        req.setAuthorId(1L);

        PostDto updated = samplePostDto(55L);
        updated.setTitle("Updated Title");
        updated.setContent("Updated Content");
        given(postService.updatePost(eq(55L), any(CreatePostRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/posts/{id}", 55)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(55)))
                .andExpect(jsonPath("$.title", is("Updated Title")));
    }

    @Test
    @DisplayName("DELETE /api/posts/{id} returns 204")
    void deletePost() throws Exception {
        doNothing().when(postService).deletePost(77L);

        mockMvc.perform(delete("/api/posts/{id}", 77))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/posts with blank title returns 400")
    void createPostValidationError() throws Exception {
        CreatePostRequest req = new CreatePostRequest();
        req.setTitle("");
        req.setContent("Some content");
        req.setAuthorId(1L);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}

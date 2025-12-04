package com.example.blog_api.controller;

import com.example.blog_api.dto.CreatePostRequest;
import com.example.blog_api.dto.PostDto;
import com.example.blog_api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> list() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> get(@PathVariable Long id) {
        PostDto dto = postService.getPostById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@Valid @RequestBody CreatePostRequest req) {
        PostDto created = postService.createPost(req);
        return ResponseEntity.created(URI.create("/api/posts/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> update(@PathVariable Long id, @Valid @RequestBody CreatePostRequest req) {
        PostDto updated = postService.updatePost(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

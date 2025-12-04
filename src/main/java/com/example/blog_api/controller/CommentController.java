package com.example.blog_api.controller;

import com.example.blog_api.dto.CommentDto;
import com.example.blog_api.dto.CreateCommentRequest;
import com.example.blog_api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/comments")
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CreateCommentRequest req) {
        CommentDto created = commentService.createComment(req);
        return ResponseEntity.created(URI.create("/api/comments/" + created.getId())).body(created);
    }

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> listByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

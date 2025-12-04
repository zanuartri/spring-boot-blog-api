package com.example.blog_api.service;

import com.example.blog_api.dto.CommentDto;
import com.example.blog_api.dto.CreateCommentRequest;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CreateCommentRequest req);
    CommentDto getCommentById(Long id);
    List<CommentDto> getCommentsByPostId(Long postId);
    void deleteComment(Long id);
}

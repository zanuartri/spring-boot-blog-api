package com.example.blog_api.service;

import com.example.blog_api.dto.CreatePostRequest;
import com.example.blog_api.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostRequest req);
    PostDto getPostById(Long id);
    List<PostDto> getAllPosts();
    PostDto updatePost(Long id, CreatePostRequest req);
    void deletePost(Long id);
}

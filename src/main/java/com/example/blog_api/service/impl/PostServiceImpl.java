package com.example.blog_api.service.impl;

import com.example.blog_api.dto.CommentDto;
import com.example.blog_api.dto.CreatePostRequest;
import com.example.blog_api.dto.PostDto;
import com.example.blog_api.entity.Post;
import com.example.blog_api.entity.User;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.repository.PostRepository;
import com.example.blog_api.repository.UserRepository;
import com.example.blog_api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostDto createPost(CreatePostRequest req) {
        User author = userRepository.findById(req.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User", req.getAuthorId()));
        Post p = Post.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .author(author)
                .build();
        Post saved = postRepository.save(p);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
        return toDto(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(Long id, CreatePostRequest req) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());
        Post updated = postRepository.save(p);
        return toDto(updated);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", id);
        }
        postRepository.deleteById(id);
    }

    /* simple manual mapping to DTO to avoid exposing entity directly */
    private PostDto toDto(Post p) {
        PostDto dto = new PostDto();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setContent(p.getContent());
        dto.setCreatedAt(p.getCreatedAt());
        if (p.getAuthor() != null) dto.setAuthorName(p.getAuthor().getName());
        // map comments to CommentDto if any
        if (p.getComments() != null) {
            dto.setComments(p.getComments().stream().map(c -> {
                CommentDto cd = new CommentDto();
                cd.setId(c.getId());
                cd.setText(c.getText());
                cd.setCreatedAt(c.getCreatedAt());
                return cd;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}

package com.example.blog_api.service.impl;

import com.example.blog_api.dto.CommentDto;
import com.example.blog_api.dto.CreateCommentRequest;
import com.example.blog_api.entity.Comment;
import com.example.blog_api.entity.Post;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.repository.CommentRepository;
import com.example.blog_api.repository.PostRepository;
import com.example.blog_api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public CommentDto createComment(CreateCommentRequest req) {
        Post post = postRepository.findById(req.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post", req.getPostId()));
        Comment c = Comment.builder()
                .post(post)
                .text(req.getText())
                .build();
        Comment saved = commentRepository.save(c);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long id) {
        Comment c = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id));
        return toDto(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        return post.getComments().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment", id);
        }
        commentRepository.deleteById(id);
    }

    private CommentDto toDto(Comment c) {
        CommentDto dto = new CommentDto();
        dto.setId(c.getId());
        dto.setText(c.getText());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}

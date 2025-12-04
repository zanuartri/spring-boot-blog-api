package com.example.blog_api.service;

import com.example.blog_api.dto.CommentDto;
import com.example.blog_api.dto.CreateCommentRequest;
import com.example.blog_api.entity.Comment;
import com.example.blog_api.entity.Post;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.repository.CommentRepository;
import com.example.blog_api.repository.PostRepository;
import com.example.blog_api.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        postRepository = mock(PostRepository.class);
        commentService = new CommentServiceImpl(commentRepository, postRepository);
    }

    @Test
    void createComment_whenPostExists_shouldSave() {
        Post post = Post.builder().id(2L).title("t").createdAt(Instant.now()).build();
        when(postRepository.findById(2L)).thenReturn(Optional.of(post));

        CreateCommentRequest req = new CreateCommentRequest();
        req.setPostId(2L);
        req.setText("Nice");

        Comment saved = Comment.builder().id(11L).post(post).text("Nice").createdAt(Instant.now()).build();
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        CommentDto dto = commentService.createComment(req);
        assertThat(dto.getId()).isEqualTo(11L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getCommentsByPostId_whenPostNotFound_shouldThrow() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> commentService.getCommentsByPostId(99L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteComment_whenNotExists_shouldThrow() {
        when(commentRepository.existsById(7L)).thenReturn(false);
        assertThatThrownBy(() -> commentService.deleteComment(7L)).isInstanceOf(ResourceNotFoundException.class);
    }
}


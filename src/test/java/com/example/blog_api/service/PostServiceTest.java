package com.example.blog_api.service;

import com.example.blog_api.dto.CreatePostRequest;
import com.example.blog_api.dto.PostDto;
import com.example.blog_api.entity.Post;
import com.example.blog_api.entity.User;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.repository.PostRepository;
import com.example.blog_api.repository.UserRepository;
import com.example.blog_api.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);
        postService = new PostServiceImpl(postRepository, userRepository);
    }

    @Test
    void createPost_whenAuthorExists_shouldSaveAndReturnDto() {
        User u = User.builder().id(1L).name("Alice").email("a@x.com").createdAt(Instant.now()).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        CreatePostRequest req = new CreatePostRequest();
        req.setTitle("Hello");
        req.setContent("World");
        req.setAuthorId(1L);

        Post saved = Post.builder().id(10L).title(req.getTitle()).content(req.getContent()).author(u).createdAt(Instant.now()).build();
        when(postRepository.save(any(Post.class))).thenReturn(saved);

        PostDto dto = postService.createPost(req);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        verify(postRepository, times(1)).save(any(Post.class));

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Hello");
    }

    @Test
    void getPostById_whenNotFound_shouldThrow() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> postService.getPostById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Post");
    }

    @Test
    void getAllPosts_shouldReturnList() {
        when(postRepository.findAll()).thenReturn(List.of(
                Post.builder().id(1L).title("t1").createdAt(Instant.now()).build()
        ));
        List<PostDto> res = postService.getAllPosts();
        assertThat(res).hasSize(1);
    }

    @Test
    void deletePost_whenNotExists_shouldThrow() {
        when(postRepository.existsById(5L)).thenReturn(false);
        assertThatThrownBy(() -> postService.deletePost(5L)).isInstanceOf(ResourceNotFoundException.class);
    }
}


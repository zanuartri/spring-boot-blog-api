package com.example.blog_api.service;

import com.example.blog_api.dto.CreateUserRequest;
import com.example.blog_api.dto.UserDto;
import com.example.blog_api.entity.User;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.repository.UserRepository;
import com.example.blog_api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_shouldSaveAndReturnDto() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Charlie");
        req.setEmail("c@example.com");

        User saved = User.builder().id(3L).name(req.getName()).email(req.getEmail()).createdAt(Instant.now()).build();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDto dto = userService.createUser(req);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(3L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(
                User.builder().id(1L).name("A").email("a@x.com").createdAt(Instant.now()).build()
        ));
        List<UserDto> res = userService.getAllUsers();
        assertThat(res).hasSize(1);
    }

    @Test
    void getUserById_whenNotFound_shouldThrow() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }

    @Test
    void deleteUser_whenNotExists_shouldThrow() {
        when(userRepository.existsById(42L)).thenReturn(false);
        assertThatThrownBy(() -> userService.deleteUser(42L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}


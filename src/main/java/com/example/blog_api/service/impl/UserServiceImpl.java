package com.example.blog_api.service.impl;

import com.example.blog_api.dto.CreateUserRequest;
import com.example.blog_api.dto.UserDto;
import com.example.blog_api.entity.User;
import com.example.blog_api.exception.ResourceNotFoundException;
import com.example.blog_api.repository.UserRepository;
import com.example.blog_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(CreateUserRequest req) {
        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .build();
        User saved = userRepository.save(u);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return toDto(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long id, CreateUserRequest req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        User updated = userRepository.save(u);
        return toDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    private UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }
}

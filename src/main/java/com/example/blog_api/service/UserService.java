package com.example.blog_api.service;

import com.example.blog_api.dto.CreateUserRequest;
import com.example.blog_api.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserRequest req);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, CreateUserRequest req);
    void deleteUser(Long id);
}

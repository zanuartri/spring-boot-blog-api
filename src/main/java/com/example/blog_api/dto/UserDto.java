package com.example.blog_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Instant createdAt;
}

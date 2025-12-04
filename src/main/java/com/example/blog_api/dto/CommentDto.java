package com.example.blog_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Instant createdAt;
}

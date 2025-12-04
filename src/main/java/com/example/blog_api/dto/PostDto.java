package com.example.blog_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Instant createdAt;
    private List<CommentDto> comments;
}

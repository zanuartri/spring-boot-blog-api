package com.example.blog_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePostRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotNull(message = "authorId must be provided")
    private Long authorId;
}

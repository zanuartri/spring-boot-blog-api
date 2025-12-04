package com.example.blog_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCommentRequest {
    @NotNull(message = "postId must be provided")
    private Long postId;

    @NotBlank(message = "Text must not be blank")
    private String text;
}

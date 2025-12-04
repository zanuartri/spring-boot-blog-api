package com.example.blog_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Name must not be blank")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    @Size(max = 150)
    private String email;
}

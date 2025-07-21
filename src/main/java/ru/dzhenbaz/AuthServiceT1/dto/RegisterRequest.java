package ru.dzhenbaz.AuthServiceT1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be from 3 to 50 characters long")
        String username,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be from 6 to 100 characters long")
        String password,

        Set<String> roles
) {
}

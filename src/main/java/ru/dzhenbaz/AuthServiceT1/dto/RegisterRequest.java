package ru.dzhenbaz.AuthServiceT1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Запрос на регистрацию нового пользователя")
public record RegisterRequest(

        @Schema(description = "Имя пользователя (уникальное)", example = "john doe")
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be from 3 to 50 characters long")
        String username,

        @Schema(description = "Email пользователя (уникальный)", example = "john@example.com")
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        String email,

        @Schema(description = "Пароль пользователя", example = "123456")
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be from 6 to 100 characters long")
        String password,

        @Schema(
                description = "Список ролей (опционально). Если не указано — назначается ROLE_GUEST",
                example = "[\"ROLE_GUEST\"]"
        )
        Set<String> roles
) {
}

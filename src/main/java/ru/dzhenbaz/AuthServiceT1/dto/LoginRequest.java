package ru.dzhenbaz.AuthServiceT1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию пользователя (логин)")
public record LoginRequest(

        @Schema(description = "Имя пользователя (уникальное)", example = "john doe")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "Пароль пользователя", example = "123456")
        @NotBlank(message = "Password is required")
        String password
) {
}

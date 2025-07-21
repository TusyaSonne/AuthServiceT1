package ru.dzhenbaz.AuthServiceT1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на обновление access токена с использованием refresh токена")
public record RefreshTokenRequest(

        @Schema(
                description = "Refresh токен, полученный ранее при логине или регистрации",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {
}

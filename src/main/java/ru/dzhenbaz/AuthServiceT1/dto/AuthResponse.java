package ru.dzhenbaz.AuthServiceT1.dto;

public record AuthResponse(

        String accessToken,
        String refreshToken
) {
}

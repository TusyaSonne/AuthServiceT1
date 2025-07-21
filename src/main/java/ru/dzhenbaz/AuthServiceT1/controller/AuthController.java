package ru.dzhenbaz.AuthServiceT1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dzhenbaz.AuthServiceT1.dto.AuthResponse;
import ru.dzhenbaz.AuthServiceT1.dto.LoginRequest;
import ru.dzhenbaz.AuthServiceT1.dto.RefreshTokenRequest;
import ru.dzhenbaz.AuthServiceT1.dto.RegisterRequest;
import ru.dzhenbaz.AuthServiceT1.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Аутентификация", description = "Регистрация, логин, обновление токена и выход")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт пользователя и выдаёт access/refresh токены"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или пользователь уже существует")
    })
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(
            summary = "Вход пользователя",
            description = "Проверяет логин и пароль, возвращает access/refresh токены"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "400", description = "Неверные учётные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Обновление access-токена",
            description = "Выдаёт новый access токен по валидному refresh токену"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access токен успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверный или отозванный refresh токен"),
            @ApiResponse(responseCode = "401", description = "Истёкший или отсутствующий токен")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request));
    }

    @Operation(
            summary = "Выход (logout)",
            description = "Отзывает refresh токен и блокирует его дальнейшее использование"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Токен отозван успешно"),
            @ApiResponse(responseCode = "400", description = "Токен невалиден или не является refresh токеном")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok("Successfully logged out (refresh-token is blacklisted)");
    }
}

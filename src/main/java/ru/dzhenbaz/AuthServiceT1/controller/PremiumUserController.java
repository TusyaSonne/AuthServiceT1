package ru.dzhenbaz.AuthServiceT1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/premium_user")
@Tag(name = "Premium user", description = "Эндпоинт только для пользователей с ролью PREMIUM_USER")
public class PremiumUserController {

    @Operation(
            summary = "Приветствие для премиум-пользователя",
            description = "Доступ разрешён только пользователям с ролью ROLE_PREMIUM_USER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный доступ (ROLE_PREMIUM_USER)"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "У пользователя нет роли ROLE_PREMIUM_USER")
    })
    @GetMapping
    public ResponseEntity<?> helloPremiumUser() {
        return ResponseEntity.ok("Hello, premium user!");
    }
}

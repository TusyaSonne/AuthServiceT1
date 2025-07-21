package ru.dzhenbaz.AuthServiceT1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Эндпоинт только для пользователей с ролью ADMIN")
public class AdminController {

    @Operation(
            summary = "Приветствие для администратора",
            description = "Доступ разрешён только пользователям с ролью ROLE_ADMIN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный доступ (ROLE_ADMIN)"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "У пользователя нет роли ROLE_ADMIN")
    })
    @GetMapping
    public ResponseEntity<?> helloAdmin() {
        return ResponseEntity.ok("Hello, admin!");
    }
}

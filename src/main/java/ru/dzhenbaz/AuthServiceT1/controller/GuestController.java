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
@RequestMapping("/api/guest")
@Tag(name = "Guest", description = "Эндпоинт для пользователей с ролью GUEST")
public class GuestController {

    @Operation(
            summary = "Приветствие для гостя",
            description = "Доступ разрешён только пользователям с ролью ROLE_GUEST"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный доступ к ресурсу"),
            @ApiResponse(responseCode = "401", description = "Неавторизован (отсутствует или просрочен токен)"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (недостаточно прав)")
    })
    @GetMapping
    public ResponseEntity<?> helloGuest() {
        return ResponseEntity.ok("Hello, guest!");
    }
}

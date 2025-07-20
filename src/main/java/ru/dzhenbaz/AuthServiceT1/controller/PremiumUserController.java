package ru.dzhenbaz.AuthServiceT1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/premium_user")
public class PremiumUserController {

    @GetMapping
    public ResponseEntity<?> helloPremiumUser() {
        return ResponseEntity.ok("Hello, premium user!");
    }
}

package ru.dzhenbaz.AuthServiceT1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guest")
public class GuestController {

    @GetMapping
    public ResponseEntity<?> helloGuest() {
        return ResponseEntity.ok("Hello, guest!");
    }
}

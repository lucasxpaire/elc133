package com.jardimbotanico.backend.controller;

import com.jardimbotanico.backend.model.Credenciais;
import com.jardimbotanico.backend.repository.CredenciaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class CredenciaisController {

    @Autowired
    private CredenciaisRepository credenciaisRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Credenciais credenciais = credenciaisRepository.findByUsername(username).orElse(null);

        if (credenciais != null && credenciais.getPassword().equals(password)) {
            return ResponseEntity.ok("Login bem-sucedido");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }
}

package com.ucsy.springjwtauth.controller;

import com.ucsy.springjwtauth.dtos.LoginRequest;
import com.ucsy.springjwtauth.dtos.RefreshTokenRequest;
import com.ucsy.springjwtauth.dtos.RegisterRequest;
import com.ucsy.springjwtauth.dtos.TokenPairs;
import com.ucsy.springjwtauth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
        return ResponseEntity.ok().body("New User Created");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairs> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        TokenPairs tokenPairs = authService.loginUser(loginRequest);
        return ResponseEntity.ok().body(tokenPairs);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenPairs> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenPairs tokenPairs = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok().body(tokenPairs);
    }
}

package com.hrworkflow.identityservice.controller;

import com.hrworkflow.identityservice.dto.UserDetailsDTO;
import com.hrworkflow.identityservice.dto.UserLoginDTO;
import com.hrworkflow.identityservice.dto.UserRegisterDTO;
import com.hrworkflow.identityservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserDetailsDTO register(@RequestBody UserRegisterDTO user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginCredentials) {

        String token = authService.login(loginCredentials);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    private record TokenResponse(String token) {}
}

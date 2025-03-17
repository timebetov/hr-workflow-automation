package com.hrworkflow.authservice.controller;

import com.hrworkflow.authservice.dto.AuthResponseDTO;
import com.hrworkflow.authservice.dto.LoginRequestDTO;
import com.hrworkflow.authservice.dto.RegisterRequestDTO;
import com.hrworkflow.authservice.dto.UserResponseDTO;
import com.hrworkflow.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}

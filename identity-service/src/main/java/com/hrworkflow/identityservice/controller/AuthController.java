package com.hrworkflow.identityservice.controller;

import com.hrworkflow.identityservice.dto.UserDetailsDTO;
import com.hrworkflow.identityservice.dto.UserLoginDTO;
import com.hrworkflow.identityservice.dto.UserRegisterDTO;
import com.hrworkflow.identityservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody UserRegisterDTO user) {

        String token = authService.register(user);
        Map<String, String> response = new HashMap<>();
        response.put("AccessToken", token);
        return response;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserLoginDTO loginCredentials) {

        String token = authService.login(loginCredentials);
        Map<String, String> response = new HashMap<>();
        response.put("AccessToken", token);
        return response;
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader(value = "Authorization") String authHeader) {
        authService.logout(authHeader);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully logged out");
        return response;
    }
}

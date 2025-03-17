package com.hrworkflow.authservice.service;

import com.hrworkflow.authservice.dto.*;
import com.hrworkflow.authservice.feignclient.UserClient;
import com.hrworkflow.authservice.util.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponseDTO register(RegisterRequestDTO req) {

        String hashedPassword = passwordEncoder.encode(req.getPassword());
        CreateUserRequestDTO userReq = CreateUserRequestDTO.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(hashedPassword)
                .position(req.getPosition())
                .role("CANDIDATE")
                .build();

        UserResponseDTO createdUser = userClient.createUser(userReq);

        String jwtToken = jwtService.generateJwtToken(createdUser);
        createdUser.setJwtToken(jwtToken);
        return createdUser;
    }

    public AuthResponseDTO login(LoginRequestDTO req) {

        UserResponseDTO user = userClient.getUserByEmail(req.getEmail());

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtService.generateJwtToken(user);

        return new AuthResponseDTO(token);
    }
}

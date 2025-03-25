package com.hrworkflow.identityservice.service;

import com.hrworkflow.common.utils.JwtUtil;
import com.hrworkflow.identityservice.dto.Token;
import com.hrworkflow.identityservice.dto.UserLoginDTO;
import com.hrworkflow.identityservice.dto.UserRegisterDTO;
import com.hrworkflow.identityservice.model.Role;
import com.hrworkflow.identityservice.model.User;
import com.hrworkflow.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TokenService tokenService;

    public String login(UserLoginDTO userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(userDetails.getUsername()));

        if (!passwordEncoder.matches(userDetails.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword())
        );

        log.info("Username {} is logged in", user.getUsername());
        kafkaTemplate.send("user.logged.in", user.getUsername());

        String token = generateJwtToken(user);
        tokenService.save(Token.builder()
                        .expiresAt(jwtUtil.extractExpiration(token))
                        .userId(user.getId())
                        .username(user.getUsername())
                        .userRole(String.valueOf(user.getRole()))
                        .value(token)
                        .build()
        );

        return token;
    }

    public String register(UserRegisterDTO user) {

        User newUser = modelMapper.map(user, User.class);

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.CANDIDATE);

        User savedUser = userRepository.save(newUser);

        if (savedUser.getUsername() == null) {
            savedUser.setUsername("user"+savedUser.getId());
            savedUser = userRepository.save(savedUser);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        String token = generateJwtToken(savedUser);
        tokenService.save(Token.builder()
                        .expiresAt(jwtUtil.extractExpiration(token))
                        .userId(savedUser.getId())
                        .username(savedUser.getUsername())
                        .userRole(String.valueOf(savedUser.getRole()))
                        .value(token)
                        .build()
        );

        log.info("New user {} is registered", savedUser.getUsername());
        kafkaTemplate.send("user.registered", savedUser.getUsername());
        return token;
    }

    public void logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        tokenService.evict(token);

        log.info("User {} is logged out", userId);
        kafkaTemplate.send("user.logged.out", String.valueOf(userId));
    }

    private String generateJwtToken(User userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole());

        return jwtUtil.generateToken(claims, userDetails.getUsername());
    }
}

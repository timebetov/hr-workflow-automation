package com.hrworkflow.identityservice.service;

import com.hrworkflow.identityservice.dto.UserDetailsDTO;
import com.hrworkflow.identityservice.dto.UserLoginDTO;
import com.hrworkflow.identityservice.dto.UserRegisterDTO;
import com.hrworkflow.identityservice.model.Role;
import com.hrworkflow.identityservice.model.User;
import com.hrworkflow.identityservice.repository.UserRepository;
import com.hrworkflow.identityservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.topics.log-info}")
    private String logInfoTopic;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String login(UserLoginDTO userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(userDetails.getUsername()));

        if (!passwordEncoder.matches(userDetails.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword())
        );

        String msg = String.format("Username: %s is logged in", user.getUsername());
        kafkaTemplate.send(logInfoTopic, msg);

        return jwtUtil.generateJwtToken(user);
    }

    public UserDetailsDTO register(UserRegisterDTO user) {

        User newUser = modelMapper.map(user, User.class);

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.CANDIDATE);

        User savedUser = userRepository.save(newUser);

        if (savedUser.getUsername() == null) {
            savedUser.setUsername("user"+savedUser.getId());
            savedUser = userRepository.save(savedUser);
        }

        String msg = String.format("New user: %s with ID: %d is registered", savedUser.getUsername(), savedUser.getId());
        kafkaTemplate.send(logInfoTopic, msg);
        return modelMapper.map(savedUser, UserDetailsDTO.class);
    }
}

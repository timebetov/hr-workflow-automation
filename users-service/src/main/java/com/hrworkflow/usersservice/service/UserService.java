package com.hrworkflow.usersservice.service;

import com.hrworkflow.usersservice.dto.auth.CreateUserRequestDTO;
import com.hrworkflow.usersservice.dto.ResourceNotFoundException;
import com.hrworkflow.usersservice.dto.auth.UserResponseDTO;
import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import com.hrworkflow.usersservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserResponseDTO findByEmailDTO(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email: " + email + " not found"));


        return UserResponseDTO.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .position(user.getPosition())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
    }

    public List<User> findByPosition (String position) {
        return userRepository.findByPosition(position);
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public UserResponseDTO save(CreateUserRequestDTO user) {

        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .position(user.getPosition())
                .role(Role.valueOf(user.getRole()))
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);

        return UserResponseDTO.builder()
                .userId(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .position(savedUser.getPosition())
                .role(savedUser.getRole().name())
                .build();
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

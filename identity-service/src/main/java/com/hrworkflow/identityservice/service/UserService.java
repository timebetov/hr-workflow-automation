package com.hrworkflow.identityservice.service;

import com.hrworkflow.common.exceptions.ResourceNotFoundException;
import com.hrworkflow.identityservice.dto.Token;
import com.hrworkflow.identityservice.dto.UserDetailsDTO;
import com.hrworkflow.identityservice.dto.UserRegisterDTO;
import com.hrworkflow.identityservice.model.Role;
import com.hrworkflow.identityservice.model.User;
import com.hrworkflow.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;
    private final RedisTemplate<String, Token> redisTemplate;

    public boolean existsByIdAndRoleIs(Long userId, Role role) {

        return userRepository.existsByIdAndRoleIs(userId, role);
    }

    public UserDetailsDTO getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->new ResourceNotFoundException("User not found with username: " + username));
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    public UserDetailsDTO getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->new ResourceNotFoundException("User not found with email: " + email));
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    public UserDetailsDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    public List<UserDetailsDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDetailsDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserDetailsDTO> getAllUsersByRole(String role) {

        List<User> users = userRepository.findByRole(Role.valueOf(role));
        return users.stream()
                .map(user -> modelMapper.map(user, UserDetailsDTO.class))
                .collect(Collectors.toList());
    }

    public UserDetailsDTO updateUser(Long id, UserRegisterDTO userDetailsDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("User not found with id: " + id));

        user.setFirstName(userDetailsDTO.getFirstName());
        user.setLastName(userDetailsDTO.getLastName());
        if (userDetailsDTO.getUsername() != null) {
            user.setUsername(userDetailsDTO.getUsername());
        }
        user.setEmail(userDetailsDTO.getEmail());
        user.setPassword(userDetailsDTO.getPassword());
        user.setPosition(userDetailsDTO.getPosition());

        User updatedUser = userRepository.save(user);

        String msg = String.format("User: %s with ID: %d is updated", updatedUser.getUsername(), updatedUser.getId());
        kafkaTemplate.send("user.updated", msg);
        return modelMapper.map(updatedUser, UserDetailsDTO.class);
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        );

        userRepository.deleteById(id);
        tokenService.evictAllForUser(id);

        String msg = String.format("User with ID: %s is deleted", id);
        kafkaTemplate.send("user.deleted", msg);
    }

    public UserDetailsDTO getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return modelMapper.map(user, UserDetailsDTO.class);
        }
        throw new AccessDeniedException("Unauthorized");
    }
}

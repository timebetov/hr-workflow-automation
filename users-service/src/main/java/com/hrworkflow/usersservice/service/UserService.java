package com.hrworkflow.usersservice.service;

import com.hrworkflow.usersservice.dto.ResourceNotFoundException;
import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import com.hrworkflow.usersservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email: " + email + " not found"));
    }

    public List<User> findByPosition (String position) {
        return userRepository.findByPosition(position);
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(int id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

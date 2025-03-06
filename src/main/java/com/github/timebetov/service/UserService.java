package com.github.timebetov.service;

import com.github.timebetov.model.User;
import com.github.timebetov.model.status.RoleStatus;
import com.github.timebetov.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(UUID id, User user) {

        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setPosition(user.getPosition());

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(RoleStatus role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByPosition(String position) {
        return userRepository.findByPosition(position);
    }

    @Transactional(readOnly = true)
    public List<User> getCandidatesByJob(UUID jobId) {

        return userRepository.findByJobId(jobId);
    }

}

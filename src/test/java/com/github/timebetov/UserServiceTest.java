package com.github.timebetov;

import com.github.timebetov.model.User;
import com.github.timebetov.model.status.RoleStatus;
import com.github.timebetov.repository.UserRepository;
import com.github.timebetov.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mockUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .email("GpN8H@example.com")
                .password("password123")
                .role(RoleStatus.CANDIDATE)
                .build();
    }

    @Test
    void registerUser_ShouldSaveAndReturnUser() {

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.registerUser(mockUser);

        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(result);
        assertEquals(mockUser.getId(), result.getId());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {

        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {

        User updatedUser = User.builder()
                .id(userId)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane@example.com")
                .password("passwordJane")
                .role(RoleStatus.HR)
                .position("Manager")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Manager", result.getPosition());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void getUsersByRole_ShouldReturnListOfUsers() {
        when(userRepository.findByRole(RoleStatus.CANDIDATE)).thenReturn(List.of(mockUser));

        List<User> users = userService.getUsersByRole(RoleStatus.CANDIDATE);

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findByRole(RoleStatus.CANDIDATE);
    }

    @Test
    void getUsersByPosition_ShouldReturnListOfUsers() {
        when(userRepository.findByPosition("Developer")).thenReturn(List.of(mockUser));

        List<User> users = userService.getUsersByPosition("Developer");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findByPosition("Developer");
    }

    @Test
    void getCandidatesByJob_ShouldReturnListOfUsers() {
        UUID jobId = UUID.randomUUID();
        when(userRepository.findByJobId(jobId)).thenReturn(List.of(mockUser));

        List<User> candidates = userService.getCandidatesByJob(jobId);

        assertFalse(candidates.isEmpty());
        assertEquals(1, candidates.size());
        verify(userRepository, times(1)).findByJobId(jobId);
    }
}

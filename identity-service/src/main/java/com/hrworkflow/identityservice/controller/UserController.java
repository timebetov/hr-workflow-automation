package com.hrworkflow.identityservice.controller;

import com.hrworkflow.identityservice.dto.UserDetailsDTO;
import com.hrworkflow.identityservice.dto.UserRegisterDTO;
import com.hrworkflow.identityservice.model.Role;
import com.hrworkflow.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDetailsDTO> getUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/role/{role}")
    public List<UserDetailsDTO> getUsersByRole(@PathVariable String role) {

        return userService.getAllUsersByRole(role);
    }

    @GetMapping("/{id}")
    public UserDetailsDTO getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public UserDetailsDTO getUserByEmail(@PathVariable String email) {

        return userService.getUserByEmail(email);
    }

    @GetMapping("/username/{username}")
    public UserDetailsDTO getUserByUsername(@PathVariable String username) {

        return userService.getUserByUsername(username);
    }

    @GetMapping("/checkUserForRole/{id}")
    public boolean getUserByRole(
            @PathVariable Long id,
            @RequestParam String role) {

        return userService.existsByIdAndRoleIs(id, Role.valueOf(role));
    }

    @PutMapping("/{id}")
    public UserDetailsDTO updateUser(@PathVariable Long id, @RequestBody UserRegisterDTO userDetailsDTO) {

        return userService.updateUser(id, userDetailsDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {

        userService.deleteUserById(id);
        return "User with id: " + id + " deleted";
    }
}

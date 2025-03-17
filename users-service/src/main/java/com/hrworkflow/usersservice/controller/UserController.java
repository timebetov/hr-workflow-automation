package com.hrworkflow.usersservice.controller;

import com.hrworkflow.usersservice.dto.auth.CreateUserRequestDTO;
import com.hrworkflow.usersservice.dto.auth.UserResponseDTO;
import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import com.hrworkflow.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers(
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "role", required = false) String role
    ) {

        List<User> users;

        if (role != null) {
            users = userService.findByRole(Role.valueOf(role));
        } else if (position != null) {
            users = userService.findByPosition(position);
        } else {
            users = userService.findAll();
        }
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable String email) {
        return userService.findByEmailDTO(email);
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody CreateUserRequestDTO req) {
        return userService.save(req);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}

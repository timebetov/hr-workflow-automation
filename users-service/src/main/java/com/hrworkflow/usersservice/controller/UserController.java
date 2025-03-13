package com.hrworkflow.usersservice.controller;

import com.hrworkflow.usersservice.model.Role;
import com.hrworkflow.usersservice.model.User;
import com.hrworkflow.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public User getUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.delete(id);
    }

}

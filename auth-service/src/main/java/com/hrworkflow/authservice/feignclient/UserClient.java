package com.hrworkflow.authservice.feignclient;

import com.hrworkflow.authservice.dto.CreateUserRequestDTO;
import com.hrworkflow.authservice.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "USERS-SERVICE", path = "/users-app/api")
public interface UserClient {

    @PostMapping("/users")
    UserResponseDTO createUser(@RequestBody CreateUserRequestDTO req);

    @GetMapping("/users/email/{email}")
    UserResponseDTO getUserByEmail(@PathVariable String email);
}

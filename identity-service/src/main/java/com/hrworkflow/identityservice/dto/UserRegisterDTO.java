package com.hrworkflow.identityservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "Firstname is required")
    @Size(min = 3, message = "Firstname must be at least 3 characters long")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    @Size(min = 3, message = "Lastname must be at least 3 characters long")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Size(min = 4, message = "Username must be at least 4 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Position is required")
    @Size(min = 2, message = "Firstname must be at least 2 characters long")
    private String position;
}

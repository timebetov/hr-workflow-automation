package com.hrworkflow.usersservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Firstname must not be blank")
    @Size(min = 3, message = "Firstname must be at least 3 characters long")
    private String firstName;

    @NotBlank(message = "Lastname must not be blank")
    @Size(min = 3, message = "Lastname must be at least 3 characters long")
    private String lastName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Position must be not blank")
    @Size(min = 2, message = "Position must be at least 2 characters long")
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}

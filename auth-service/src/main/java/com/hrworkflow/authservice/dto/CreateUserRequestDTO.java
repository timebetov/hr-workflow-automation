package com.hrworkflow.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String position;
    private String role;
}

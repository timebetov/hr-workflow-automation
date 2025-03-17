package com.hrworkflow.usersservice.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String position;
    private String role;
}

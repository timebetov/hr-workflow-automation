package com.hrworkflow.identityservice.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String position;
    private String role;
}

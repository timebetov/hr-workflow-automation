package com.github.timebetov.model;

import com.github.timebetov.model.status.RoleStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@Data
@SuperBuilder
public class User extends BaseEntity {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleStatus role;
}

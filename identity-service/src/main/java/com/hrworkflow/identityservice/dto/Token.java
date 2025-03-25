package com.hrworkflow.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

    private String value;
    private Long userId;
    private String username;
    private String userRole;
    private Date expiresAt;
}

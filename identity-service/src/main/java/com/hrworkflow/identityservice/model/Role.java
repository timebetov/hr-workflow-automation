package com.hrworkflow.identityservice.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    HR, ADMIN, CANDIDATE;

    @Override
    public String getAuthority() {
        return name();
    }
}

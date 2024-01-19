package com.tinexlab.backend.model.dto.request;

import lombok.Getter;

@Getter
public class AuthenticationRequest {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

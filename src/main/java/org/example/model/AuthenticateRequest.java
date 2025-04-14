package org.example.model;

import lombok.Data;

@Data
public class AuthenticateRequest {

    private String userName;

    private String password;
}

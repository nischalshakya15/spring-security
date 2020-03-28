package org.personal.spring.security.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationRequest implements Serializable {

    private String userName;

    private String password;
}

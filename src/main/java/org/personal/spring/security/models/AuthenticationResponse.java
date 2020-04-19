package org.personal.spring.security.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationResponse implements Serializable {

    private final String accessToken;
}

package org.personal.spring.security.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {

    private String accessToken;

    private String refreshToken;

    public AuthenticationResponse() {
    }
}

package org.personal.spring.security.resources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.spring.security.config.UserPrincipal;
import org.personal.spring.security.jwt.JwtTokenProvider;
import org.personal.spring.security.models.AuthenticationRequest;
import org.personal.spring.security.models.AuthenticationResponse;
import org.personal.spring.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthResource {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {

        log.info("Authenticating user {} ", authenticationRequest.getUserName());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUserName(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Generating token");

        String jwt = "Bearer " + jwtTokenProvider.generateToken(authentication);

        userRepository.findByUserName(((UserPrincipal) authentication.getPrincipal()).getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

        log.info("Token generated");

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

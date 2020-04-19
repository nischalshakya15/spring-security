package org.personal.spring.security.resources;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.spring.security.config.SecurityConfig;
import org.personal.spring.security.config.UserPrincipal;
import org.personal.spring.security.jwt.JwtTokenProvider;
import org.personal.spring.security.models.AuthenticationRequest;
import org.personal.spring.security.models.AuthenticationResponse;
import org.personal.spring.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConfig.BASE_URL + AuthResource.BASE_URL)
public class AuthResource {

    public static final String BASE_URL = "/auth";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @Value("${spring-security.client.redirect-url}")
    private String redirectUrl;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(AuthenticationRequest authenticationRequest,
                                                                   HttpServletResponse response, Model model) throws IOException {
        log.info("Authenticating user {} ", authenticationRequest.toString());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUserName(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        Cookie accessToken = new Cookie("accessToken", jwt);
        accessToken.setMaxAge(86400000);
        accessToken.setPath("/");
        response.addCookie(accessToken);

        userRepository.findByUserName(((UserPrincipal) authentication.getPrincipal()).getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

        model.addAttribute("authenticatedRequest", new AuthenticationRequest());

        return ResponseEntity.ok().body(new AuthenticationResponse(jwt));

    }

    @PostMapping("/authorize")
    public ResponseEntity<UserPrincipal> authorizeUser(HttpServletRequest request) {
        String authorizationToken = request.getHeader("Authorization");
        if (authorizationToken == null) {
            throw new JwtException("Token is empty");
        }
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(userPrincipal);
    }
}

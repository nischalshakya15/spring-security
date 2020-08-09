package org.personal.spring.security.resources;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.spring.security.config.SecurityConfig;
import org.personal.spring.security.config.UserPrincipal;
import org.personal.spring.security.domain.User;
import org.personal.spring.security.jwt.JwtTokenProvider;
import org.personal.spring.security.models.AuthenticationRequest;
import org.personal.spring.security.models.AuthenticationResponse;
import org.personal.spring.security.repository.UserRepository;
import org.personal.spring.security.service.CustomUserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConfig.BASE_URL + AuthResource.BASE_URL)
public class AuthResource {

    public static final String BASE_URL = "/auth";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final CustomUserDetailService customUserDetailService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Authenticating user {} ", authenticationRequest.toString());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUserName(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = "Bearer " + jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        userRepository.findByUserName(((UserPrincipal) authentication.getPrincipal()).getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

        return ResponseEntity.ok().body(new AuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/generate-access-token")
    public ResponseEntity<Map<String, String>> generateAccessToken(HttpServletRequest request, @RequestBody AuthenticationResponse authenticationResponse) {
        final Map<String, String> accessTokenMap = new HashMap<>();
        boolean isRefreshTokenValid = jwtTokenProvider.validateRefreshToken(authenticationResponse.getRefreshToken());
        if (isRefreshTokenValid) {
            Long id = jwtTokenProvider.getUserIdFromRefreshToken(authenticationResponse.getRefreshToken());
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                UsernamePasswordAuthenticationToken authentication = customUserDetailService.getAuthentication(id);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = "Bearer " + jwtTokenProvider.generateAccessToken(authentication);
                accessTokenMap.put("accessToken", token);
            }
        }
        return ResponseEntity.ok().body(accessTokenMap);
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

package org.personal.spring.security.resources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.spring.security.config.SecurityConfig;
import org.personal.spring.security.domain.User;
import org.personal.spring.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConfig.BASE_URL + UserResource.BASE_URL)
public class UserResource {

    final static String BASE_URL = "/users";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        log.info("fetching users");
        final List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) throws URISyntaxException {
        log.info("creating user");
        log.info("encoding password");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User createdUser = userRepository.save(user);
        log.info("created user with id {}", createdUser.getId());
        return ResponseEntity.created(new URI(BASE_URL)).body(createdUser);
    }
}

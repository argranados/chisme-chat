package com.ciberaccion.chisme_chat.controller;

import com.ciberaccion.chisme_chat.model.User;
import com.ciberaccion.chisme_chat.repository.UserRepository;
import com.ciberaccion.chisme_chat.security.JwtUtil;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "username/password required"));
            }
            if (userRepository.existsByUsername(username)) {
                logger.warn("Attempted to register existing username: " + username);
                return ResponseEntity.badRequest().body(Map.of("error", "username exists"));
            }

            User user = new User(username, passwordEncoder.encode(password));
            userRepository.save(user);
            logger.info(
                    "Se ejecutó el endpoint /register [POST] para el usuario: " + username + " id: " + user.getId());
            return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername()));
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(500).body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("username/password required");
            }

            return userRepository.findByUsername(username)
                    .map(u -> {
                        try {
                            if (passwordEncoder.matches(password, u.getPasswordHash())) {
                                String token = jwtUtil.generateToken(u.getId());
                                logger.info("se ejecuto el endpoint /login [POST] para el usuario: " + username);
                                return ResponseEntity.ok(Map.of("token", token));
                            } else {
                                logger.warn(
                                        "invalid credentials en endpoint /login [POST] para el usuario: " + username);
                                return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
                            }
                        } catch (Exception e) {
                            logger.error("Error during password validation or token generation for user: " + username
                                    + " : " + e.getMessage()); // null to avoid stack trace clutter
                            return ResponseEntity.status(500)
                                    .body(Map.of("error", "Authentication error: " + e.getMessage()));
                        }
                    })
                    .orElseGet(() -> {
                        logger.warn("User not found in /login [POST]: " + username);
                        return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
                    });
        } catch (Exception e) {
            logger.error("Unexpected error in /login endpoint", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }
}

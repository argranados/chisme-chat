package com.ciberaccion.chisme_chat.controller;

import com.ciberaccion.chisme_chat.model.User;
import com.ciberaccion.chisme_chat.repository.UserRepository;
import com.ciberaccion.chisme_chat.security.JwtUtil;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
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
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body("username/password required");
        if (userRepository.existsByUsername(username)) return ResponseEntity.badRequest().body("username exists");
        User user = new User(username, passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body("username/password required");
        return userRepository.findByUsername(username)
                .map(u -> {
                    if (passwordEncoder.matches(password, u.getPasswordHash())) {
                        String token = jwtUtil.generateToken(u.getId());
                        return ResponseEntity.ok(Map.of("token", token));
                    } else {
                        return ResponseEntity.status(401).body("invalid credentials");
                    }
                })
                .orElse(ResponseEntity.status(401).body("invalid credentials"));
    }
}

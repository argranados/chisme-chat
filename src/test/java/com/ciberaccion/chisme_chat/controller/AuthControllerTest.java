package com.ciberaccion.chisme_chat.controller;

import com.ciberaccion.chisme_chat.repository.UserRepository;
import com.ciberaccion.chisme_chat.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtUtil jwtUtil;
    
    @Test
    public void contextLoads() {
        assertThat(userRepository).isNotNull();
    }    
}

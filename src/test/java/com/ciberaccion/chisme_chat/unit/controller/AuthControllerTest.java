package com.ciberaccion.chisme_chat.unit.controller;

import com.ciberaccion.chisme_chat.controller.AuthController;
import com.ciberaccion.chisme_chat.model.User;
import com.ciberaccion.chisme_chat.repository.UserRepository;
import com.ciberaccion.chisme_chat.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {
    // @Autowired UserRepository userRepository;
    // @Autowired PasswordEncoder passwordEncoder;
    // @Autowired JwtUtil jwtUtil;
    
    // @Test
    // public void contextLoads() {
    //     assertThat(userRepository).isNotNull();
    // }    

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        controller = new AuthController(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void testRegisterCreatesUser() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        User savedUser = new User("alice", "hashed");
        savedUser.setUsername("alice");
        savedUser.setPasswordHash("hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ResponseEntity<?> response = controller.register(Map.of("username", "alice", "password", "secret"));

        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("alice", body.get("username"));
    }

    @Test
    void testLoginReturnsToken() {
        User user = new User("alice", "hashed");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken(user.getId())).thenReturn("jwt-token");

        ResponseEntity<?> response = controller.login(Map.of("username", "alice", "password", "secret"));

        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("jwt-token", body.get("token"));
    }

}

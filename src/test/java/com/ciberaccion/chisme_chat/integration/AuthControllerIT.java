package com.ciberaccion.chisme_chat.integration;

import com.ciberaccion.chisme_chat.model.User;
import com.ciberaccion.chisme_chat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegisterAndLoginFlow() {
        // Register user
        Map<String, String> registerBody = Map.of("username", "alice", "password", "secret");
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity("/api/auth/register", registerBody, Map.class);

        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
        assertTrue(userRepository.existsByUsername("alice"));

        // Login user
        Map<String, String> loginBody = Map.of("username", "alice", "password", "secret");
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/api/auth/login", loginBody, Map.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody().get("token"));
    }
}
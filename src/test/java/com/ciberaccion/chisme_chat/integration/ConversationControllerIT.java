package com.ciberaccion.chisme_chat.integration;

import com.ciberaccion.chisme_chat.model.Conversation;
import com.ciberaccion.chisme_chat.repository.ConversationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConversationControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConversationRepository conversationRepository;

    @BeforeEach
    void cleanDb() {
        conversationRepository.deleteAll();
    }

    @Test
    void testCreateAndListConversation() {
        // Primero registrar y loguear usuario
        restTemplate.postForEntity("/api/auth/register",
        Map.of("username", "alice", "password", "secret"), Map.class);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/api/auth/login",
        Map.of("username", "alice", "password", "secret"), Map.class);
        String token = (String) loginResponse.getBody().get("token");

        // Crear conversación con Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        Map<String, String> body = Map.of("title", "chat", "participantIds", "aliceId,bobId");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);


        ResponseEntity<Conversation> response = restTemplate.postForEntity("/api/conversations", request, Conversation.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Conversation conv = response.getBody();
        assertNotNull(conv.getId());

        // Verify repository
        assertTrue(conversationRepository.findByParticipantIdsContaining("aliceId").size() > 0);
    }
}

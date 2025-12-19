package com.ciberaccion.chisme_chat.integration;

import com.ciberaccion.chisme_chat.model.Conversation;
import com.ciberaccion.chisme_chat.model.Message;
import com.ciberaccion.chisme_chat.repository.ConversationRepository;
import com.ciberaccion.chisme_chat.repository.MessageRepository;
import com.ciberaccion.chisme_chat.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void cleanDb() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSendAndListMessages() {
        // Registrar y loguear usuario
        // restTemplate.postForEntity("/api/auth/register",
        // Map.of("username", "alice", "password", "secret"), Map.class);

        // Registrar usuario y obtener su id
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
                "/api/auth/register",
                Map.of("username", "alice", "password", "secret"),
                Map.class);
        String userId = (String) registerResponse.getBody().get("id");
        System.out.println("Registered user with id: " + userId);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/api/auth/login",
                Map.of("username", "alice", "password", "secret"), Map.class);
        String token = (String) loginResponse.getBody().get("token");
        System.out.println("Obtained JWT token: " + token);
        // Enviar mensaje con Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // Crear conversación primero
        Map<String, String> convBody = Map.of("title", "chat", "participantIds", userId + ",bobId");
        HttpEntity<Map<String, String>> convRequest = new HttpEntity<>(convBody, headers);
        ResponseEntity<Conversation> convResponse = restTemplate.postForEntity("/api/conversations", convRequest,
                Conversation.class);
        System.out.println("Created conversation with id: " + convResponse.getBody().getId());
        String conversationId = convResponse.getBody().getId();
        System.out.println("Using conversationId: " + conversationId);
        // Primero crear un mensaje
        Map<String, String> body = Map.of(
                "conversationId", conversationId,
                "senderId", userId,
                "content", "Hola mundo");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Message> response = restTemplate.postForEntity("/api/messages", request, Message.class);
        System.out.println("Received response: " + response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Message msg = response.getBody();
        assertNotNull(msg.getId());
        assertEquals("Hola mundo", msg.getContent());

        // Listar mensajes
        // ResponseEntity<Message[]> listResponse = restTemplate
        //         .getForEntity("/api/messages/" + conversationId + "?limit=10", Message[].class);
        // System.out.println("Listed messages response: " + listResponse);
        // assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        // assertTrue(listResponse.getBody().length > 0);

        // Listar mensajes con Authorization header
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);

        ResponseEntity<Message[]> listResponse = restTemplate.exchange("/api/messages/" + conversationId + "?limit=10",
                HttpMethod.GET,
                getRequest,
                Message[].class);

        System.out.println("Listed messages response: " + listResponse);

        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertTrue(listResponse.getBody().length > 0);
    }
}

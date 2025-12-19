package com.ciberaccion.chisme_chat.unit.controller;

import com.ciberaccion.chisme_chat.controller.MessageController;
import com.ciberaccion.chisme_chat.model.Message;
import com.ciberaccion.chisme_chat.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    private MessageRepository repository;
    private MessageController controller;

    @BeforeEach
    void setUp() {
        repository = mock(MessageRepository.class);
        controller = new MessageController(repository);
    }

    @Test
    void testSendMessage() {
        Message msg = new Message("conv1", "aliceId", "Hola mundo");
        when(repository.save(any(Message.class))).thenReturn(msg);

        ResponseEntity<?> response = controller.sendMessage(Map.of(
                "conversationId", "conv1",
                "senderId", "aliceId",
                "content", "Hola mundo"
        ));

        assertEquals(200, response.getStatusCodeValue());
        Message body = (Message) response.getBody();
        assertEquals("conv1", body.getConversationId());
        assertEquals("aliceId", body.getSenderId());
        assertEquals("Hola mundo", body.getContent());
    }

    @Test
    void testListMessages() {
        Message msg = new Message("conv1", "aliceId", "Hola mundo");
        when(repository.findByConversationIdOrderByCreatedAtDesc("conv1", PageRequest.of(0, 20)))
                .thenReturn(List.of(msg));

        ResponseEntity<?> response = controller.listMessages("conv1", 20);

        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
        assertEquals("Hola mundo", ((Message) body.get(0)).getContent());
    }
}

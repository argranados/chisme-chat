package com.ciberaccion.chisme_chat.unit.controller;

import com.ciberaccion.chisme_chat.controller.ConversationController;
import com.ciberaccion.chisme_chat.model.Conversation;
import com.ciberaccion.chisme_chat.repository.ConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConversationControllerTest {

    private ConversationRepository repository;
    private ConversationController controller;

    @BeforeEach
    void setUp() {
        repository = mock(ConversationRepository.class);
        controller = new ConversationController(repository);
    }

    @Test
    void testCreateConversation() {
        Conversation conv = new Conversation("chat", "aliceId,bobId");
        when(repository.save(any(Conversation.class))).thenReturn(conv);

        ResponseEntity<?> response = controller.create(Map.of("title", "chat", "participantIds", "aliceId,bobId"));

        assertEquals(200, response.getStatusCodeValue());
        Conversation body = (Conversation) response.getBody();
        assertEquals("chat", body.getTitle());
        assertEquals("aliceId,bobId", body.getParticipantIds());
    }

    @Test
    void testListForUser() {
        Conversation conv = new Conversation("chat", "aliceId,bobId");
        when(repository.findByParticipantIdsContaining("aliceId")).thenReturn(List.of(conv));

        ResponseEntity<?> response = controller.listForUser("aliceId");

        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
        assertEquals("chat", ((Conversation) body.get(0)).getTitle());
    }
}

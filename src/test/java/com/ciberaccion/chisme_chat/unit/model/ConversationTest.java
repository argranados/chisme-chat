package com.ciberaccion.chisme_chat.unit.model;
import com.ciberaccion.chisme_chat.model.Conversation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversationTest {

    @Test
    void testConversationConstructor() {
        Conversation conv = new Conversation("Chat de prueba", "aliceId,bobId");

        assertNotNull(conv.getId()); // UUID generado automáticamente
        assertEquals("Chat de prueba", conv.getTitle());
        assertEquals("aliceId,bobId", conv.getParticipantIds());
        assertNotNull(conv.getCreatedAt()); // fecha inicializada
    }

    @Test
    void testSetters() {
        Conversation conv = new Conversation();
        conv.setTitle("Nuevo título");
        conv.setParticipantIds("user1,user2");

        assertEquals("Nuevo título", conv.getTitle());
        assertEquals("user1,user2", conv.getParticipantIds());
    }
}
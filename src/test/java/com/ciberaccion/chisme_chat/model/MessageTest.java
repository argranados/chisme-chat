package com.ciberaccion.chisme_chat.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testMessageConstructor() {
        Message msg = new Message("conv1", "aliceId", "Hola mundo");

        assertNotNull(msg.getId()); // UUID generado automáticamente
        assertEquals("conv1", msg.getConversationId());
        assertEquals("aliceId", msg.getSenderId());
        assertEquals("Hola mundo", msg.getContent());
        assertNotNull(msg.getCreatedAt());
        assertFalse(msg.isReadStatus()); // valor por defecto
    }

    @Test
    void testSetters() {
        Message msg = new Message();
        msg.setConversationId("conv2");
        msg.setSenderId("bobId");
        msg.setContent("Mensaje nuevo");
        msg.setReadStatus(true);

        assertEquals("conv2", msg.getConversationId());
        assertEquals("bobId", msg.getSenderId());
        assertEquals("Mensaje nuevo", msg.getContent());
        assertTrue(msg.isReadStatus());
    }
}
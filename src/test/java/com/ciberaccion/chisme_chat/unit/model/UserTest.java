package com.ciberaccion.chisme_chat.unit.model;
import org.junit.jupiter.api.Test;
import com.ciberaccion.chisme_chat.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    @Test
    void testUserConstructorAndGetters() {
        User user = new User("alice", "hashedPassword");

        assertNotNull(user.getId()); // UUID generado automáticamente
        assertEquals("alice", user.getUsername());
        assertEquals("hashedPassword", user.getPasswordHash());
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setUsername("bob");
        user.setPasswordHash("secret");

        assertEquals("bob", user.getUsername());
        assertEquals("secret", user.getPasswordHash());
    }

}

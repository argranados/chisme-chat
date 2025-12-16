package com.ciberaccion.chisme_chat.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // For simplicity store participants as comma-separated user ids (later: ConversationUser join table)
    @Column(length = 2000)
    private String participantIds;

    public Conversation() {}
    public Conversation(String title, String participantIds) {
        this.title = title;
        this.participantIds = participantIds;
    }
    // getters/setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Instant getCreatedAt() { return createdAt; }
    public String getParticipantIds() { return participantIds; }
    public void setParticipantIds(String participantIds) { this.participantIds = participantIds; }
}

package com.ciberaccion.chisme_chat.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_conv_created", columnList = "conversationId,createdAt")
})
public class Message {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String conversationId;

    @Column(nullable = false)
    private String senderId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    private boolean readStatus = false;

    public Message() {}
    public Message(String conversationId, String senderId, String content) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
    }
    // getters/setters
    public String getId() { return id; }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean read) { this.readStatus = read; }
}

package com.ciberaccion.chisme_chat.controller;

import com.ciberaccion.chisme_chat.model.Conversation;
import com.ciberaccion.chisme_chat.repository.ConversationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ConversationController {
    private final ConversationRepository conversationRepository;

    public ConversationController(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String participants = body.get("participantIds"); // comma-separated user ids
        if (title == null || participants == null) return ResponseEntity.badRequest().body("missing");
        Conversation c = new Conversation(title, participants);
        conversationRepository.save(c);
        return ResponseEntity.ok(c);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listForUser(@PathVariable String userId) {
        List<Conversation> convos = conversationRepository.findByParticipantIdsContaining(userId);
        return ResponseEntity.ok(convos);
    }    
}

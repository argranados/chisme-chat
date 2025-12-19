package com.ciberaccion.chisme_chat.controller;

import com.ciberaccion.chisme_chat.model.Conversation;
import com.ciberaccion.chisme_chat.repository.ConversationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);
    private final ConversationRepository conversationRepository;

    public ConversationController(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String participants = body.get("participantIds"); // comma-separated user ids
        if (title == null || participants == null)
            return ResponseEntity.badRequest().body("missing");
        Conversation c = new Conversation(title, participants);
        conversationRepository.save(c);
        logger.info("Created conversation: id={}, title={}, participantIds={}", c.getId(), c.getTitle(), c.getParticipantIds());
        return ResponseEntity.ok(c);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listForUser(@PathVariable String userId) {
        logger.info("Searching conversations for userId: {}", userId);        
        List<Conversation> convos = conversationRepository.findByParticipantIdsContaining(userId);
        logger.info("Found {} conversations for userId: {}", convos.size(), userId);
        return ResponseEntity.ok(convos);
    }
}

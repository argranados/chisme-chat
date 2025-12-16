package com.ciberaccion.chisme_chat.controller;

import com.ciberaccion.chisme_chat.model.Message;
import com.ciberaccion.chisme_chat.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> body) {
        String conversationId = body.get("conversationId");
        String senderId = body.get("senderId");
        String content = body.get("content");
        if (conversationId == null || senderId == null || content == null)
            return ResponseEntity.badRequest().body("missing");
        Message m = new Message(conversationId, senderId, content);
        messageRepository.save(m);
        return ResponseEntity.ok(m);
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<?> listMessages(@PathVariable String conversationId,
            @RequestParam(defaultValue = "20") int limit) {
        List<Message> msgs = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId,
                PageRequest.of(0, limit));
        return ResponseEntity.ok(msgs);
    }
}

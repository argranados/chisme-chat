package com.ciberaccion.chisme_chat.repository;

import com.ciberaccion.chisme_chat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByConversationIdOrderByCreatedAtDesc(String conversationId, Pageable pageable);
}

package com.ciberaccion.chisme_chat.repository;

import com.ciberaccion.chisme_chat.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
    List<Conversation> findByParticipantIdsContaining(String userId);
}

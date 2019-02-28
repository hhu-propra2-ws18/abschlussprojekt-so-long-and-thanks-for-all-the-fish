package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findById(long id);
}

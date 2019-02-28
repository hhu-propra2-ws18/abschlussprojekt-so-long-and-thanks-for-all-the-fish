package de.hhu.rhinoshareapp.chat.service;

import de.hhu.rhinoshareapp.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findById(long id);
}

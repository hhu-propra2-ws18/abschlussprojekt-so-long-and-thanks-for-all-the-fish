package de.hhu.rhinoshareapp.chat.service;

import de.hhu.rhinoshareapp.chat.model.ChatMessage;
import de.hhu.rhinoshareapp.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}

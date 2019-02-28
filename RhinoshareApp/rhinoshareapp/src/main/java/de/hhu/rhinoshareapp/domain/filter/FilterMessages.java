package de.hhu.rhinoshareapp.domain.filter;

import de.hhu.rhinoshareapp.domain.model.ChatMessage;
import de.hhu.rhinoshareapp.domain.service.ChatMessageRepository;

import java.util.List;
import java.util.stream.Collectors;


public class FilterMessages {

    public List<ChatMessage> filterFrom(String name, ChatMessageRepository chatMessageRepository) {
        return chatMessageRepository.findAll().stream()
                .filter(chatMessage -> chatMessage.getFromName().equals(name)).collect(Collectors.toList());

    }

    public List<ChatMessage> filterTo(String name, ChatMessageRepository chatMessageRepository) {
        return chatMessageRepository.findAll().stream()
                .filter(chatMessage -> chatMessage.getToName().equals(name)).collect(Collectors.toList());

    }
}

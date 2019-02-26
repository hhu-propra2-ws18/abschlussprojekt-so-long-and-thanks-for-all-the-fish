package de.hhu.rhinoshareapp.chat.filter;

import de.hhu.rhinoshareapp.chat.model.ChatMessage;
import de.hhu.rhinoshareapp.chat.service.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


public class FilterMessages {

    public List<ChatMessage> filterFrom(String name, ChatMessageRepository chatMessageRepository) {
        List<ChatMessage> filteredMessagesFrom = new ArrayList<>();
        for (ChatMessage chatMessage:chatMessageRepository.findAll()) {
            if(chatMessage.getFromName().equals(name)) {
                filteredMessagesFrom.add(chatMessage);
            }
        }
        return filteredMessagesFrom;
    }

    public List<ChatMessage> filterTo(String name, ChatMessageRepository chatMessageRepository) {
        List<ChatMessage> filteredMessagesTo = new ArrayList<>();
        for (ChatMessage chatMessage:chatMessageRepository.findAll()) {
            if(chatMessage.getToName().equals(name)) {
                filteredMessagesTo.add(chatMessage);
            }
        }
        return filteredMessagesTo;
    }
}

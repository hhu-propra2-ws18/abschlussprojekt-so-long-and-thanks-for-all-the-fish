package de.hhu.rhinoshareapp.chat.chatController;

import de.hhu.rhinoshareapp.chat.filter.FilterMessages;
import de.hhu.rhinoshareapp.chat.model.ChatMessage;
import de.hhu.rhinoshareapp.chat.service.ChatMessageRepository;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class chatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;


    public FilterMessages filter = new FilterMessages();

    @GetMapping("/chat")
    public String chatOverview(Model model, Principal p) {
            List<ChatMessage> listFrom = filter.filterFrom(p.getName(), chatMessageRepository); //liste für gesendende Nachrichen
            List<ChatMessage> listTo = filter.filterTo(p.getName(), chatMessageRepository); //liste für empfangene Nachrichten
            model.addAttribute("listFrom", listFrom);
            model.addAttribute("listTo", listTo );

        ActualUserChecker.checkActualUser(model, p, userRepository);

        return "/Chat/chat_overview";
    }

    @GetMapping("/newchat")
    public String newChat(Model model, Principal p) {
        ChatMessage chatMessage = new ChatMessage();
        ActualUserChecker.checkActualUser(model, p, userRepository);
        model.addAttribute("chatMessage", chatMessage);
        return "/Chat/chat_newChat";
    }

    @PostMapping("/newchat")
    public String sendChat(@ModelAttribute ChatMessage chatMessage, Principal p) {
        chatMessage.setFromName(p.getName());
        chatMessageRepository.save(chatMessage);
        return "redirect:/chat";
    }

    @GetMapping("/deletechat/{ID}")
    public String deleteChat(@PathVariable long ID, Model model) {
        ChatMessage chatMessage = chatMessageRepository.findById(ID);
        model.addAttribute("chatMessage", chatMessage);

        return "/Chat/chat_deleteChat";
    }

    @PostMapping("/deletechat/{ID}")
    public String deleteChatFromDb(@PathVariable long ID) {
        ChatMessage chatMessage = chatMessageRepository.findById(ID);
        chatMessageRepository.delete(chatMessage);

        return "redirect:/chat";
    }

}

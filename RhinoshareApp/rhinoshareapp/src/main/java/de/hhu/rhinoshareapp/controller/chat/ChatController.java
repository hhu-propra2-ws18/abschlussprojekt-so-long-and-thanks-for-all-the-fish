package de.hhu.rhinoshareapp.controller.chat;

import de.hhu.rhinoshareapp.domain.filter.FilterMessages;
import de.hhu.rhinoshareapp.domain.model.ChatMessage;
import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.service.ChatMessageRepository;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class ChatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;


    public FilterMessages filter = new FilterMessages();

    @GetMapping("/")
    public String chatOverview(Model model, Principal p) {
        List<ChatMessage> listFrom = filter.filterFrom(p.getName(), chatMessageRepository);
        List<ChatMessage> listTo = filter.filterTo(p.getName(), chatMessageRepository);
        model.addAttribute("chatActive","active");
        model.addAttribute("listFrom", listFrom);
        model.addAttribute("listTo", listTo );
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Chat/chat_overview";
    }

    @GetMapping("/new/{ID}")
    public String newChat(@PathVariable long ID, Model model, Principal p) {
        ChatMessage chatMessage = new ChatMessage();
        Person recipient = userRepository.findUserByuserID(ID).get();
        ActualUserChecker.checkActualUser(model, p, userRepository);
        model.addAttribute("chatActive","active");
        model.addAttribute("recipient", recipient);
        model.addAttribute("chatMessage", chatMessage);
        return "Chat/chat_newChat";
    }

    @PostMapping("/new/{ID}")
    public String sendChat(@ModelAttribute ChatMessage chatMessage, Principal p, @PathVariable long ID) {
        chatMessage.setFromName(p.getName());
        chatMessage.setToName(userRepository.findUserByuserID(ID).get().getUsername());
        chatMessageRepository.save(chatMessage);
        return "redirect:/messages";
    }

    @GetMapping("/delete/{ID}")
    public String deleteChat(@PathVariable long ID, Model model, Principal p) {
        ChatMessage chatMessage = chatMessageRepository.findById(ID);
        ActualUserChecker.checkActualUser(model, p, userRepository);
        model.addAttribute("chatActive","active");
        model.addAttribute("chatMessage", chatMessage);
        return "Chat/chat_deleteChat";
    }

    @PostMapping("/delete/{ID}")
    public String deleteChatFromDb(@PathVariable long ID) {
        ChatMessage chatMessage = chatMessageRepository.findById(ID);
        chatMessageRepository.delete(chatMessage);
        return "redirect:/messages";
    }

    @GetMapping("/answer/{ID}")
    public String answerChat(@PathVariable long ID, Model model, Principal p) {
        ChatMessage chatMessage = chatMessageRepository.findById(ID);
        ActualUserChecker.checkActualUser(model, p, userRepository);
        model.addAttribute("chatActive","active");
        model.addAttribute("chatMessage", chatMessage);
        return "/Chat/chat_answer";
    }

    @PostMapping("/answer/{ID}")
    public String answerChatAndSend(@PathVariable long ID, @ModelAttribute ChatMessage answer, Principal p) {
        ChatMessage chatMessage = chatMessageRepository.findById(ID);
        answer.setFromName(p.getName());
        answer.setToName(chatMessage.getFromName());
        chatMessageRepository.save(answer);
        return "redirect:/messages";
    }

}

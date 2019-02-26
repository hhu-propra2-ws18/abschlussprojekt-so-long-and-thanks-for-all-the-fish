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

import java.security.Principal;
import java.util.ArrayList;
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
            List<ChatMessage> listTo = filter.filterTo(p.getName(), chatMessageRepository);
            model.addAttribute("listFrom", listFrom);
            model.addAttribute("listTo", listTo );

        ActualUserChecker.checkActualUser(model, p, userRepository);

        return "/Chat/chat_overview";
    }


}

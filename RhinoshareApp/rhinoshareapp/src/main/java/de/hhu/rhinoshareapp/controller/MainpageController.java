package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainpageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @GetMapping("/")
    public String viewAll(Model model, Principal p){
        ActualUserChecker.checkActualUser(model, p, userRepository);
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("articleActive","active");
        return "Article/viewAll";
    }

}

package de.ProPra.Articles.infrastructure.web;

import de.ProPra.Articles.domain.model.Article;
import de.ProPra.Articles.domain.service.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/article/{id}")
    public String articleView(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "articleView";
    }
    @GetMapping("/article")
    public String articleView(Model model){
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "viewAll";
    }

    @GetMapping("/newArticle")
    public String newArticle(Model model){
        Article article = new Article();
        model.addAttribute("article",article);
        return  "newArticle";
    }

    @PostMapping("/newArticle")
    public String saveArticle(@ModelAttribute("article") Article article){
        articleRepository.save(article);
        return "redirect:/article";
    }


}

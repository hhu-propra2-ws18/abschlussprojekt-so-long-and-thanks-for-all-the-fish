package de.ProPra.Articles.Controller;

import de.ProPra.Articles.domain.model.Article;
import de.ProPra.Articles.domain.service.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class testController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/")
    public String mainPage() {
        return "hallo";
    }

    @GetMapping("/article/{id}")
    public String articleView(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "articleView";
    }
    @GetMapping("/article/")
    public String articleView(Model model){
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "viewAll";
    }


}

package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;


    //View
    @GetMapping("/")
    public String viewAll(Model model){
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "Article/viewAll";
    }

    @GetMapping("/view/{personID}")
    public String viewMyArticles(Model model, @PathVariable long personID, Principal p){
        p.getName();
        List<Article> articles = articleRepository.findByPersonID(personID);
        model.addAttribute("userID",personID);
        model.addAttribute("articles", articles);
        return "Article/viewFromPerson";
    }

    @GetMapping("/open/{userID}")
    public String openArticleView(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        return "Article/openArticleView";
    }

    @GetMapping("/admin/{userID}")
    public String privateArticleView(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        return "Article/privateArticleView";
    }


    //Create
    @GetMapping("/new/{personID}")
    public String newArticle(Model model, @PathVariable long personID){
        Article article = new Article();
        model.addAttribute("article",article);
        return  "Article/newArticle";
    }

    @PostMapping("/new/{personID}")
    public String saveArticle(HttpServletRequest request, Model model, @ModelAttribute("article") Article article, @PathVariable long personID) throws IOException, SQLException {
        article.saveImage();
        article.setPersonID(personID);
        articleRepository.save(article);
        return "redirect:/article/";
    }

    //Edit
    @GetMapping("/edit/{userID}")
    public String editArticle(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id);
        model.addAttribute("article", article);
        return "Article/editArticle";
    }

    @PostMapping("/edit/{userID}")
    public String editArticlePostMapping(@ModelAttribute("article") Article article, @PathVariable long id){
        Article oldArticle = articleRepository.findById(id);
        oldArticle.setName(article.getName());
        oldArticle.setComment(article.getComment());
        oldArticle.setRent(article.getRent());
        oldArticle.setDeposit(article.getDeposit());
        oldArticle.setAvailable(article.isAvailable());
        articleRepository.save(oldArticle);
        return "redirect:/article/" + id;
    }

    //Delete
    @GetMapping("/delete/{userID}")
    public String deleteAArticle(@PathVariable long id, Model model){
        Article article = articleRepository.findById(id);
        model.addAttribute("article",article);
        return "Article/deleteArticle";
    }

    @PostMapping("/delete/{userID}")
    public String deleteArticleFromDB(@PathVariable long id){
        Article article = articleRepository.findById(id);
        articleRepository.delete(article);
        return "redirect:/article/";
    }

    //Search
    @GetMapping("/search")
    public String searchForArticle(@RequestParam String query, Model model){
        model.addAttribute("articles", articleRepository.findAllByNameContainingOrCommentContainingAllIgnoreCase(query,query));
        model.addAttribute("query", query);
        return "Article/searchArticle";
    }
}

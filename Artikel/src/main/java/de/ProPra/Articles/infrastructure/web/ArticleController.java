package de.ProPra.Articles.infrastructure.web;

import de.ProPra.Articles.domain.model.Article;
import de.ProPra.Articles.domain.service.ArticleRepository;
import de.ProPra.Articles.domain.service.ImageRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

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

    @GetMapping("/article/new")
    public String newArticle(Model model){
        Article article = new Article();
        model.addAttribute("article",article);
        return  "newArticle";
    }

    @PostMapping("/article/new")
    public String saveArticle(HttpServletRequest request, Model model, @ModelAttribute("article") Article article) throws IOException, SQLException {
        article.saveImage();
        articleRepository.save(article);
        return "redirect:/article/";
    }
    @GetMapping("/article/edit/{id}")
    public String editArticle(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "editArticle";
    }
    @PostMapping("/article/edit/{id}")
    public String editArticlePostMapping(@ModelAttribute("article") Article article, @PathVariable Long id){
        Article oldArticle = articleRepository.findById(id).get();
        oldArticle.setName(article.getName());
        oldArticle.setComment(article.getComment());
        oldArticle.setRent(article.getRent());
        oldArticle.setDeposit(article.getDeposit());
        oldArticle.setAvailable(article.isAvailable());
        articleRepository.save(oldArticle);
        return "redirect:/article/" + id;
    }

}

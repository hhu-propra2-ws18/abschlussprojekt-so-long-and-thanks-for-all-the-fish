package de.ProPra.Articles.infrastructure.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
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
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/{id}")
    public String articleView(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "articleView";
    }
    @GetMapping("/")
    public String articleView(Model model){
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "viewAll";
    }

    // The New Article Mapping
    @GetMapping("/new")
    public String newArticle(Model model){
        Article article = new Article();
        model.addAttribute("article",article);
        return  "newArticle";
    }

    @PostMapping("/new")
    public String saveArticle(HttpServletRequest request, Model model, @ModelAttribute("article") Article article) throws IOException, SQLException {
        article.saveImage();
        articleRepository.save(article);
        return "redirect:/article/";
    }

    // The Edit Mapping
    @GetMapping("/edit/{id}")
    public String editArticle(Model model, @PathVariable long id){
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "editArticle";
    }
    @PostMapping("/edit/{id}")
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

    //DeleteMapping
    @GetMapping("/delete/{id}")
    public String deleteAArticle(@PathVariable long id, Model model){
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article",article);
        return "deleteArticle";
    }

    @PostMapping("/delete/{id}")
    public String deleteArticleFromDB(@PathVariable long id){
        Article article = articleRepository.findById(id).get();
        articleRepository.delete(article);
        return "redirect:/article/";
    }

    //Search
    @GetMapping("/search")
    public String searchForArticle(@RequestParam String query, Model model){
        model.addAttribute("articles", articleRepository.findAllByNameContainingOrCommentContainingAllIgnoreCase(query,query));
        model.addAttribute("query", query);
        return "searchArticle";
    }
}

package de.hhu.rhinoshareapp.controller.article;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.ImageRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    //View
    @GetMapping("/")
    public String viewAll(Model model){
        Iterable<Article> articles= articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "Article/viewAll";
    }

    @GetMapping("/view")
    public String viewMyArticles(Model model, Principal p){
        User user = userRepository.findByUsername(p.getName()).get();
        List<Article> articles = articleRepository.findAllByOwner(user);
        model.addAttribute("userID",user.getUserID());
        model.addAttribute("articles", articles);
        return "Article/viewFromPerson";
    }

    @GetMapping("/open/{articleID}")
    public String openArticleView(Model model, @PathVariable long articleID){
        Article article = articleRepository.findById(articleID).get();
        model.addAttribute("article", article);
        return "Article/viewFromPerson";
    }

    @GetMapping("/open/{articleID}")
    public String privateArticleView(Model model, @PathVariable long articleID){
        Article article = articleRepository.findById(articleID).get();
        model.addAttribute("article", article);
        return "Article/privateArticleView";
    }


    //Create
    @GetMapping("/new/")
    public String newArticle(Model model){
        Article article = new Article();
        model.addAttribute("article",article);
        return  "Article/newArticle";
    }

    @PostMapping("/new/")
    public String saveArticle(HttpServletRequest request, Model model, @ModelAttribute("article") Article article, Principal p) throws IOException, SQLException {
        article.saveImage();
        article.setOwner(userRepository.findByUsername(p.getName()).get());
        articleRepository.save(article);
        return "redirect:/article/";
    }

    //Edit
    @GetMapping("/edit/{articleID}")
    public String editArticle(Model model, @PathVariable long articleID){
        Article article = articleRepository.findById(articleID).get();
        model.addAttribute("article", article);
        return "Article/editArticle";
    }

    @PostMapping("/edit/{articleID}")
    public String editArticlePostMapping(@ModelAttribute("article") Article article, @PathVariable long articleID){
        Article oldArticle = articleRepository.findById(articleID).get();
        oldArticle.setName(article.getName());
        oldArticle.setComment(article.getComment());
        oldArticle.setRent(article.getRent());
        oldArticle.setDeposit(article.getDeposit());
        oldArticle.setAvailable(article.isAvailable());
        articleRepository.save(oldArticle);
        return "redirect:/article/" + articleID;
    }

    //Delete
    @GetMapping("/delete/{articleID}")
    public String deleteAArticle(@PathVariable long articleID, Model model){
        Article article = articleRepository.findById(articleID).get();
        model.addAttribute("article",article);
        return "Article/deleteArticle";
    }

    @PostMapping("/delete/{articleID}")
    public String deleteArticleFromDB(@PathVariable long articleID){
        Article article = articleRepository.findById(articleID).get();
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

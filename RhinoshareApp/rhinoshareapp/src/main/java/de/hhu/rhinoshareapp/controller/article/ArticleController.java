package de.hhu.rhinoshareapp.controller.article;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.ImageRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
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

    @Autowired
    LendingRepository lendingRepository;

    //View
    @GetMapping("/")
    public String redirectToMainpage(){
        return "redirect:/";
    }


    @GetMapping("/view")
    public String viewMyArticles(Model model, Principal p){
        User user = userRepository.findByUsername(p.getName()).get();
        List<Article> articles = articleRepository.findAllByOwner(user);
        model.addAttribute("articles", articles);
        model.addAttribute("userActive","active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Article/viewFromPerson";
    }


    @GetMapping("/{articleID}")
    public String privateArticleView(Model model, @PathVariable long articleID, Principal p){
        User user = userRepository.findByUsername(p.getName()).get();
        Article article = articleRepository.findById(articleID).get();
        if(checkIfArticleIsBeingLended(article).isPresent()) {
            String endDate = checkIfArticleIsBeingLended(article).get().getFormattedEndDate();
            model.addAttribute("endDate", endDate);
        }
        model.addAttribute("user" , user);
        model.addAttribute("article", article);
        model.addAttribute("articleActive","active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Article/articleView";
    }


    //Create
    @GetMapping("/new/")
    public String newArticle(Model model, Principal p){
        Article article = new Article();
        model.addAttribute("article",article);
        model.addAttribute("newActive","active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return  "Article/newArticle";
    }


    @PostMapping("/new/")
    public String saveArticle(@ModelAttribute("article") Article article, Principal p, Model model) throws IOException{
        if(article.getFile() == null)
            article.image = null;
        else
           article.saveImage();
        article.setOwner(userRepository.findByUsername(p.getName()).get());
        articleRepository.save(article);
        return "redirect:/article/";
    }

    //Edit
    @GetMapping("/edit/{articleID}")
    public String editArticle(Model model, @PathVariable long articleID, Principal p){
        Article article = articleRepository.findById(articleID).get();
        if (checkIfLoggedInIsOwner(p, article) && checkIfArticleIsBeingLended(article).isPresent()) {
            model.addAttribute("article", article);
            model.addAttribute("articleActive","active");
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Article/editArticle";
        }
        return "error/403";
    }


    @PostMapping("/edit/{articleID}")
    public String editArticlePostMapping(@ModelAttribute("article") Article article, @PathVariable long articleID){
        Article oldArticle = articleRepository.findById(articleID).get();
        if (!article.getName().equals(""))
            oldArticle.setName(article.getName());
        if (!article.getComment().equals(""))
            oldArticle.setComment(article.getComment());
        oldArticle.setRent(article.getRent());
        oldArticle.setDeposit(article.getDeposit());
        oldArticle.setSellingPrice(article.getSellingPrice());
        oldArticle.setAvailable(article.isAvailable());
        oldArticle.setForSale(article.isForSale());
        articleRepository.save(oldArticle);
        return "redirect:/article/" + articleID;
    }


    //Delete
    @GetMapping("/delete/{articleID}")
    public String deleteAArticle(@PathVariable long articleID, Model model, Principal p){
        Article article = articleRepository.findById(articleID).get();
        if ((article.isAvailable()) == false){
            return "error/lended";
        }
        if (checkIfLoggedInIsOwner(p, article)) {
            model.addAttribute("article", article);
            model.addAttribute("articleActive","active");
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Article/deleteArticle";
        }
        return "error/403";
    }


    @PostMapping("/delete/{articleID}")
    public String deleteArticleFromDB(@PathVariable long articleID){

        Article article = articleRepository.findById(articleID).get();
        articleRepository.delete(article);
        return "redirect:/article/";
    }


    //Search
    @GetMapping("/search")
    public String searchForArticle(@RequestParam String query, Model model, Principal p){
        model.addAttribute("articles", articleRepository.findAllByNameContainingOrCommentContainingAllIgnoreCase(query,query));
        model.addAttribute("query", query);
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Article/searchArticle";
    }


    public boolean checkIfLoggedInIsOwner(Principal p, Article article) {
        User user = userRepository.findByUsername(p.getName()).get();
        return (article.getOwner() == user);
    }

    public Optional<Lending> checkIfArticleIsBeingLended(Article article) {
        Optional<Lending> lending = lendingRepository.findLendingBylendedArticle(article);
        return lending;
    }
}

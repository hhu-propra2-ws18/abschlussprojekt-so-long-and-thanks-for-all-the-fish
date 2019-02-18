package de.ProPra.Lending.Controller;

import de.ProPra.Lending.Dataaccess.PostProccessor;
import de.ProPra.Lending.Dataaccess.Repositories.*;
import de.ProPra.Lending.Dataaccess.Representations.LendingRepresentation;
import de.ProPra.Lending.Dataaccess.Representations.RequestRepresentation;
import de.ProPra.Lending.Dataaccess.Representations.ReturnProcessRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
public class LendingController {

    private LendingRepository lendings;
    private UserRepository users;
    private ArticleRepository articles;
    //TODO: add History for lendings

    @Autowired
    public LendingController(LendingRepository lendings, UserRepository users, ArticleRepository articles){
        this.lendings = lendings;
        this.articles = articles;
        this.users = users;
    }

    @GetMapping("/lendings/{lendID}")
        public String LendingPage(Model model, @PathVariable final long lendID) {
            LendingRepresentation filledLendings = new LendingRepresentation(lendings, users,articles);
            model.addAttribute("lendings", filledLendings.FillLendings(lendID));
            model.addAttribute("id", lendID);
            return "overviewLendings";
        }

    @PostMapping("/lendings/{lendID}")
    public String LendingPageNew(Model model, @PathVariable final long lendID, @RequestBody String postBody) { //TODO: return procces erzeugen
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.initializeNewReturn(postBodyParas, lendings);
        LendingRepresentation filledLendings = new LendingRepresentation(lendings, users,articles);
        model.addAttribute("lendings", filledLendings.FillLendings(lendID));
        model.addAttribute("id", lendID);
        return "overviewLendings";
    }

    @GetMapping("/borrows/{borrowID}")
    public String BorrowPage(Model model, @PathVariable final long borrowID) {
        LendingRepresentation filledArticles = new LendingRepresentation(lendings, users,articles);
        model.addAttribute("articles", filledArticles.FillBorrows(borrowID));
        return "overviewBorrows";
    }

    @GetMapping("/{id}")
    public String Overview(Model model, @PathVariable final long id) {
        RequestRepresentation filledRequests = new RequestRepresentation(users, articles,lendings , id);
        ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(users, articles, id, lendings);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        model.addAttribute("returns", filledReturns.FillReturns());
        return "overview";
    }

    @PostMapping("/{id}")
    public String PostOverview(Model model,@PathVariable final long id, @RequestBody String postBody) {
        RequestRepresentation filledRequests = new RequestRepresentation(users, articles, lendings , id);
        ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(users, articles, id, lendings);
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CheckDecision(postBodyParas, lendings, articles);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        model.addAttribute("returns", filledReturns.FillReturns());
        return "overview";
    }

    @GetMapping("/lendingRequest")
        public String LendingRequest(Model model, @RequestParam("requesterID") long requesterID, @RequestParam("articleID") long articleID){
        model.addAttribute("requesterID",requesterID);
        model.addAttribute("articleID", articleID);
        return "lendingRequest";
    }
    @PostMapping("/inquiry")
    public String inquiry(Model model, @RequestBody String postBody) {
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CreateNewLending(postBodyParas, articles, lendings, users);
        model.addAttribute("id", postBodyParas.get("requesterID"));
        return "inquiry";
    }
}

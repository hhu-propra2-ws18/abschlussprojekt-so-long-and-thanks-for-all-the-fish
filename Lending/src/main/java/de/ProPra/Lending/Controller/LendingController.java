package de.ProPra.Lending.Controller;

import de.ProPra.Lending.APIProcessor;
import de.ProPra.Lending.Dataaccess.PostProccessor;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.ReservationRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Dataaccess.Representations.LendingRepresentation;
import de.ProPra.Lending.Dataaccess.Representations.RequestRepresentation;
import de.ProPra.Lending.Dataaccess.Representations.ReturnProcessRepresentation;
import de.ProPra.Lending.Model.Account;
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
    private ReservationRepository reservations;
    private PostProccessor postProccessor = new PostProccessor();
    private APIProcessor apiProcessor = new APIProcessor();
    //TODO: add History for lendings

    @Autowired
    public LendingController(LendingRepository lendings, UserRepository users, ArticleRepository articles, ReservationRepository reservations){
        this.lendings = lendings;
        this.articles = articles;
        this.users = users;
        this.reservations=reservations;
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
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        postProccessor.initializeNewReturn(postBodyParas, lendings);
        LendingRepresentation filledLendings = new LendingRepresentation(lendings, users,articles);
        model.addAttribute("lendings", filledLendings.FillLendings(lendID));
        model.addAttribute("id", lendID);
        return "overviewLendings";
    }

    @GetMapping("/borrows/{borrowID}")
    public String BorrowPage(Model model, @PathVariable final long borrowID) {
        LendingRepresentation filledArticles = new LendingRepresentation(lendings, users,articles);
        model.addAttribute("articles", filledArticles.FillBorrows(borrowID));
        model.addAttribute("id", borrowID);
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
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        postProccessor.CheckDecision(postBodyParas, lendings, articles, users, reservations);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        model.addAttribute("returns", filledReturns.FillReturns());
        return "overview";
    }

    @GetMapping("/lendingRequest")
        public String LendingRequest(Model model, @RequestParam("requesterID") long requesterID, @RequestParam("articleID") long articleID){
        Account lenderAccountInformation = apiProcessor.getAccountInformationWithId(requesterID, users);
        if(apiProcessor.hasEnoughMoneyForDeposit(lenderAccountInformation, articleID, articles)){
            model.addAttribute("requesterID",requesterID);
            model.addAttribute("articleID", articleID);
            return "lendingRequest";
        }
        model.addAttribute("requesterID",requesterID);
        return "povertyPage";
    }
    @PostMapping("/inquiry")
    public String inquiry(Model model, @RequestBody String postBody) {
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        postProccessor.CreateNewLending(postBodyParas, articles, lendings, users);
        model.addAttribute("id", postBodyParas.get("requesterID"));
        return "inquiry";
    }

    //TODO: getmapping für testzwecke löschen
    @GetMapping("/test")
    public String test(){
        System.out.println(apiProcessor.getAccountInformation("Kathrin", Account.class));
        System.out.println(apiProcessor.getAccountInformation("Lisa", Account.class));
        System.out.println(apiProcessor.getAccountInformation("Olaf", Account.class));
        System.out.println(apiProcessor.getAccountInformation("Memfred", Account.class));

        return "overview";
    }

}

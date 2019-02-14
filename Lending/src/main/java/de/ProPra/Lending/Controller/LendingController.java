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
    private PersonRepository persons;
    private ArticleRepository articles;
    private RequestRepository requests;
    private ReturnProcessRepository returns;
    //TODO: add History for lendings

    @Autowired
    public LendingController(LendingRepository lendings, PersonRepository persons, ArticleRepository articles, RequestRepository requests,ReturnProcessRepository returns){
        this.lendings = lendings;
        this.articles = articles;
        this.persons = persons;
        this.requests = requests;
        this.returns = returns;
    }

    @GetMapping("/lendings/{lendID}")
        public String LendingPage(Model model, @PathVariable final long lendID) {
            LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles, requests);
            model.addAttribute("lendings", filledLendings.FillLendings(lendID));
            model.addAttribute("id", lendID);
            return "overviewLendings";
        }

    @PostMapping("/lendings/{lendID}")
    public String LendingPageNew(Model model, @PathVariable final long lendID, @RequestBody String postBody) { //TODO: return procces erzeugen
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CreateNewReturnProccess(postBodyParas, lendings, articles, returns);
        LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles, requests);
        model.addAttribute("lendings", filledLendings.FillLendings(lendID));
        model.addAttribute("id", lendID);
        return "overviewLendings";
    }

    @GetMapping("/borrows/{borrowID}")
    public String BorrowPage(Model model, @PathVariable final long borrowID) {
        LendingRepresentation filledLendings = new LendingRepresentation(lendings,persons,articles,requests);
        model.addAttribute("lendings", filledLendings.FillBorrows(borrowID));
        return "overviewBorrows";
    }

    @GetMapping("/{id}")
    public String Overview(Model model, @PathVariable final long id) {
        RequestRepresentation filledRequests = new RequestRepresentation(persons, articles, requests, id);
        ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(persons, articles, returns, id, lendings);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        model.addAttribute("returns", filledReturns.FillReturns());
        return "overview";
    }

    @PostMapping("/{id}")
    public String PostOverview(Model model,@PathVariable final long id, @RequestBody String postBody) {
        RequestRepresentation filledRequests = new RequestRepresentation(persons, articles, requests, id);
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CheckDecision(postBodyParas, lendings, articles, requests, returns);
        model.addAttribute("id", id);
        model.addAttribute("requests", filledRequests.FillRequest());
        return "overview";
    }

    @GetMapping("/lendingRequest")
        public String LendingRequest(Model model, @RequestParam("requesterID") long requesterID, @RequestParam("articleID") long articleID){
        model.addAttribute("requesterID",requesterID);
        model.addAttribute("articleID", articleID);
        return "lendingRequest";
    }
    @PostMapping("/inquiry") //TODO: timewindow is important!
    public String inquiry(Model model, @RequestBody String postBody) {
        HashMap<String, String> postBodyParas = PostProccessor.SplitString(postBody);
        PostProccessor.CreateNewEntryRequest(postBodyParas, requests);
        model.addAttribute("id", postBodyParas.get("requesterID"));
        return "inquiry";
    }
}

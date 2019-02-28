package de.hhu.rhinoshareapp.controller.lendings;

import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.Representations.LendingProcessor.PostProccessor;
import de.hhu.rhinoshareapp.Representations.LendingRepresentation;
import de.hhu.rhinoshareapp.Representations.RequestRepresentation;
import de.hhu.rhinoshareapp.Representations.ReturnProcessRepresentation;
import de.hhu.rhinoshareapp.Representations.TransactionRepresentation;
import de.hhu.rhinoshareapp.domain.model.Account;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/overview")
public class LendingController {

    @Autowired
    private LendingRepository lendingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private PostProccessor postProccessor = new PostProccessor();

    private APIProcessor apiProcessor = new APIProcessor();

    @GetMapping("/")
    public String overview(Model model, Principal p) {
        long id = postProccessor.FindUserIDByUser(userRepository, p.getName());
        LendingRepresentation filledLendings = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
        filledLendings.FillLendings(id);
        model.addAttribute("id", id);
        model.addAttribute("warning", filledLendings.isHasWarning());
        RequestRepresentation filledRequests = new RequestRepresentation(userRepository, articleRepository, lendingRepository, id);
        ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(userRepository, articleRepository, id, lendingRepository);
        model.addAttribute("requests", filledRequests.FillRequest());
        model.addAttribute("returns", filledReturns.FillReturns());
        model.addAttribute("denies", filledRequests.FillDenies());
        model.addAttribute("lendingActive", "active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/overview";
    }

    @PostMapping("/")
    public String postOverview(Model model, Principal p, @RequestBody String postBody) {
        long id = postProccessor.FindUserIDByUser(userRepository, p.getName());
        RequestRepresentation filledRequests = new RequestRepresentation(userRepository, articleRepository, lendingRepository, id);
        ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(userRepository, articleRepository, id, lendingRepository);
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        postProccessor.ProccessPostRequest(apiProcessor, postBodyParas, lendingRepository, articleRepository, userRepository, reservationRepository, transactionRepository);
        model.addAttribute("id", id);
        if (apiProcessor.isErrorOccurred()) {
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            return "Lending/errorPage";
        }
        model.addAttribute("requests", filledRequests.FillRequest());
        model.addAttribute("returns", filledReturns.FillReturns());
        model.addAttribute("denies", filledRequests.FillDenies());
        return "Lending/overview";
    }

    @GetMapping("/lendings")
    public String lendingPage(Model model, Principal p) {
        long lendID = postProccessor.FindUserIDByUser(userRepository, p.getName());
        LendingRepresentation filledLendings = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
        model.addAttribute("id", lendID);
        model.addAttribute("lendings", filledLendings.FillLendings(lendID));
        model.addAttribute("lendingActive", "active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/overviewLendings";
    }

    @PostMapping("/lendings")
    public String LendingPageNew(Model model, Principal p, @RequestBody String postBody) {
        long lendID = postProccessor.FindUserIDByUser(userRepository, p.getName());
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        postProccessor.initializeNewReturn(postBodyParas, lendingRepository);
        LendingRepresentation filledLendings = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
        model.addAttribute("id", lendID);
        model.addAttribute("lendings", filledLendings.FillLendings(lendID));
        return "Lending/overviewLendings";
    }

    @GetMapping("/borrows")
    public String borrowPage(Model model, Principal p) {
        long borrowID = postProccessor.FindUserIDByUser(userRepository, p.getName());
        model.addAttribute("id", borrowID);
        LendingRepresentation filledArticles = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
        model.addAttribute("articles", filledArticles.FillBorrows(borrowID));
        model.addAttribute("lendingActive", "active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/overviewBorrows";
    }

    @GetMapping("/lendingRequest/{articleID}")
    public String lendingRequest(Model model, Principal p, @PathVariable("articleID") long articleID) {
        long requesterID = postProccessor.FindUserIDByUser(userRepository, p.getName());
        Account lenderAccountInformation = apiProcessor.getAccountInformationWithId(requesterID, userRepository);
        model.addAttribute("requesterID", requesterID);
        model.addAttribute("lendingActive", "active");

        if (apiProcessor.isErrorOccurred()) {
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }
        if (apiProcessor.hasEnoughMoneyForDeposit(lenderAccountInformation, articleID, articleRepository)) {
            model.addAttribute("articleID", articleID);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/lendingRequest";
        }
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/povertyPage";
    }

    @GetMapping("/saleRequest/{articleID}")
    public String sellingRequest(Model model, Principal p, @PathVariable("articleID") long articleID) {
        long requesterID = postProccessor.FindUserIDByUser(userRepository, p.getName());
        Account sellerAccountInformation = apiProcessor.getAccountInformationWithId(requesterID, userRepository);
        model.addAttribute("requesterID", requesterID);
        model.addAttribute("lendingActive", "active");
        if (apiProcessor.isErrorOccurred()) {
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }
        if (apiProcessor.hasEnoughMoneyForSelling(sellerAccountInformation, articleID, articleRepository)) {
            model.addAttribute("articleID", articleID);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/sellRequest";
        }
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/povertyPage";
    }

    @PostMapping("/inquiry")
    public String inquiry(Model model, @RequestBody String postBody, Principal p) {
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        Optional<User> user = userRepository.findUserByuserID(Long.parseLong(postBodyParas.get("requesterID")));
        model.addAttribute("username", user.get().getUsername());
        model.addAttribute("id", postBodyParas.get("requesterID"));
        if (postBodyParas.get("requestValue").equals("lending")) {
            System.out.println("CREATENEWLENDING");
            postProccessor.CreateNewLending(postBodyParas, articleRepository, lendingRepository, userRepository);
        } else {
            postProccessor.SellArticle(postBodyParas, articleRepository, userRepository, apiProcessor, transactionRepository);
        }
        if (apiProcessor.isErrorOccurred()) {
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/inquiry";
    }

    //ProPay
    @GetMapping("/proPay")
    public String getProPayOverview(Model model, Principal p) {
        long id = postProccessor.FindUserIDByUser(userRepository, p.getName());
        model.addAttribute("id", id);
        Account account = apiProcessor.getAccountInformationWithId(id, userRepository);
        model.addAttribute("lendingActive", "active");
        if (apiProcessor.isErrorOccurred()) {
            System.out.println("ich geh rein !");
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }
        model.addAttribute("account", account);
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/proPayOverview";
    }

    @PostMapping("/proPay")
    public String setProPayOverview(Model model, Principal p, @RequestBody String postBody) {
        long id = postProccessor.FindUserIDByUser(userRepository, p.getName());
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        model.addAttribute("id", id);
        try {
            Account account = apiProcessor.getAccountInformationWithId(id, userRepository);
            account = apiProcessor.postMoney(Account.class, account, Double.parseDouble(postBodyParas.get("amount")));
            model.addAttribute("account", account);
        } catch (Exception e) {
            model.addAttribute("error", "Propay is not reachable, try it again later");
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }

        if (apiProcessor.isErrorOccurred()) {
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/proPayOverview";
    }

    @PostMapping("/releaseConflictingLending") //TODO: get resolve from conflict
    public String releaseConflictingLending(@RequestBody String postBody) {
        HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
        apiProcessor.PunishOrReleaseConflictingLending(postBodyParas, lendingRepository, userRepository, articleRepository, reservationRepository);
        return "Lending/overview";
    }

    @GetMapping("/conflictPage")
    public String releaseConflictingLending(Model model, Principal p) {
        long id = postProccessor.FindUserIDByUser(userRepository, p.getName());
        LendingRepresentation filledConflicts = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
        model.addAttribute("id", id);
        model.addAttribute("conflicts", filledConflicts.FillConflicts(id));
        model.addAttribute("yourConflicts", filledConflicts.FillConflictsOwner(id));
        model.addAttribute("lendingActive", "active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/conflictPage";
    }

    @GetMapping("/transaction")
    public String showTransactions(Model model, Principal p) {
        long id = postProccessor.FindUserIDByUser(userRepository, p.getName());
        TransactionRepresentation transactionRepresentation = new TransactionRepresentation(transactionRepository, userRepository);
        model.addAttribute("id", id);
        model.addAttribute("givings", transactionRepresentation.FillGivings(id));
        model.addAttribute("recieves", transactionRepresentation.FillRecieves(id));
        model.addAttribute("lendingActive", "active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Lending/transactionsPage";
    }
}

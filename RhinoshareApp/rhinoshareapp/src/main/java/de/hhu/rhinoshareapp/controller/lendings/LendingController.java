package de.hhu.rhinoshareapp.controller.lendings;

import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.Representations.LendingProcessor.PostProccessor;
import de.hhu.rhinoshareapp.Representations.LendingRepresentation;
import de.hhu.rhinoshareapp.Representations.RequestRepresentation;
import de.hhu.rhinoshareapp.Representations.ReturnProcessRepresentation;
import de.hhu.rhinoshareapp.Representations.TransactionRepresentation;
import de.hhu.rhinoshareapp.domain.model.Account;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class LendingController {

	private LendingRepository lendings;
	private UserRepository users;
	private ArticleRepository articles;
	private ReservationRepository reservations;
	private TransactionRepository transactions;
	private PostProccessor postProccessor = new PostProccessor();
	private APIProcessor apiProcessor = new APIProcessor();

	//TODO: add History for lendings

	@Autowired
	public LendingController(LendingRepository lendings, UserRepository users, ArticleRepository articles, ReservationRepository reservations, TransactionRepository transactions) {
		this.lendings = lendings;
		this.articles = articles;
		this.users = users;
		this.reservations = reservations;
		this.transactions = transactions;

	}

	@GetMapping("/lendings/{username}")
	public String LendingPage(Model model, Principal p) {
		String username = p.getName();
		long lendID = postProccessor.FindUserIDByUser(users, username);
		model.addAttribute("username", username);
		model.addAttribute("id", lendID);
		LendingRepresentation filledLendings = new LendingRepresentation(lendings, users, articles);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		model.addAttribute("lendings", filledLendings.FillLendings(lendID));
		return "Lending/overviewLendings";
	}

	@PostMapping("/lendings/{username}")
	public String LendingPageNew(Model model, Principal p, @RequestBody String postBody) { //TODO: return procces erzeugen
		String username = p.getName();
		long lendID = postProccessor.FindUserIDByUser(users, username);
		model.addAttribute("username", username);
		model.addAttribute("id", lendID);
		HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
		postProccessor.initializeNewReturn(postBodyParas, lendings);
		LendingRepresentation filledLendings = new LendingRepresentation(lendings, users, articles);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		model.addAttribute("lendings", filledLendings.FillLendings(lendID));
		return "Lending/overviewLendings";
	}

	@GetMapping("/borrows/{username}")
	public String BorrowPage(Model model, Principal p) {
		String username = p.getName();
		long borrowID = postProccessor.FindUserIDByUser(users, username);
		model.addAttribute("username", username);
		model.addAttribute("id", borrowID);
		LendingRepresentation filledArticles = new LendingRepresentation(lendings, users, articles);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		model.addAttribute("articles", filledArticles.FillBorrows(borrowID));
		return "Lending/overviewBorrows";
	}

	@GetMapping("/{username}")
	public String Overview(Model model, Principal p) {
		String username = p.getName();
		long id = postProccessor.FindUserIDByUser(users, username);
		LendingRepresentation filledLendings = new LendingRepresentation(lendings, users, articles);
		filledLendings.FillLendings(id);
		model.addAttribute("username", username);
		model.addAttribute("id", id);
		model.addAttribute("warning", filledLendings.isHasWarning());
		RequestRepresentation filledRequests = new RequestRepresentation(users, articles, lendings, id);
		ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(users, articles, id, lendings);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		model.addAttribute("requests", filledRequests.FillRequest());
		model.addAttribute("returns", filledReturns.FillReturns());
		return "Lending/overview";
	}

	@PostMapping("/{username}")
	public String PostOverview(Model model, Principal p, @RequestBody String postBody) {
		String username = p.getName();
		long id = postProccessor.FindUserIDByUser(users, username);
		model.addAttribute("username", username);
		model.addAttribute("id", id);
		RequestRepresentation filledRequests = new RequestRepresentation(users, articles, lendings, id);
		ReturnProcessRepresentation filledReturns = new ReturnProcessRepresentation(users, articles, id, lendings);
		HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
		postProccessor.CheckDecision(apiProcessor, postBodyParas, lendings, articles, users, reservations, transactions);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		model.addAttribute("requests", filledRequests.FillRequest());
		model.addAttribute("returns", filledReturns.FillReturns());
		return "Lending/overview";
	}

	@GetMapping("/lendingRequest")
	public String LendingRequest(Model model, Principal p, @RequestParam("articleID") long articleID) {
		String username = p.getName();
		long requesterID = postProccessor.FindUserIDByUser(users, username);
		model.addAttribute("username", username);
		Account lenderAccountInformation = apiProcessor.getAccountInformationWithId(requesterID, users);
		model.addAttribute("requesterID", requesterID);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		if (apiProcessor.hasEnoughMoneyForDeposit(lenderAccountInformation, articleID, articles)) {
			model.addAttribute("requesterID", requesterID);
			model.addAttribute("articleID", articleID);
			return "Lending/lendingRequest";
		}
		return "Lending/povertyPage";
	}

	@PostMapping("/inquiry")
	public String inquiry(Model model, @RequestBody String postBody) {
		HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
		Optional<User> user = users.findUserByuserID(Long.parseLong(postBodyParas.get("requesterID")));
		model.addAttribute("username", user.get().getUsername());
		model.addAttribute("id", postBodyParas.get("requesterID"));
		postProccessor.CreateNewLending(postBodyParas, articles, lendings, users);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		return "Lending/inquiry";
	}

	//
	@GetMapping("/proPay/{username}")
	public String GetProPayOverview(Model model, Principal p) {
		String username = p.getName();
		long id = postProccessor.FindUserIDByUser(users, username);
		model.addAttribute("username", username);
		model.addAttribute("id", id);
		Account account = apiProcessor.getAccountInformationWithId(id, users);
		if (apiProcessor.isErrorOccurred()) {
			System.out.println("ich geh rein !");
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		model.addAttribute("account", account);
		return "Lending/proPayOverview";
	}

	@PostMapping("/proPay/{username}")
	public String SetProPayOverview(Model model, Principal p, @RequestBody String postBody) {
		String username = p.getName();
		long id = postProccessor.FindUserIDByUser(users, username);
		HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
		Account account = apiProcessor.getAccountInformationWithId(id, users);
		account = apiProcessor.postMoney(Account.class, account, Double.parseDouble(postBodyParas.get("amount")));
		model.addAttribute("account", account);
		model.addAttribute("username", username);
		model.addAttribute("id", id);
		if (apiProcessor.isErrorOccurred()) {
			model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
			apiProcessor.setErrorOccurred(false);
			return "Lending/errorPage";
		}
		return "Lending/proPayOverview";
	}

	@PostMapping("/releaseConflictingLending") //TODO: get resolve from conflict
	public String ReleaseConflictingLending(@RequestBody String postBody) {
		HashMap<String, String> postBodyParas = postProccessor.SplitString(postBody);
		apiProcessor.PunishOrReleaseConflictingLending(postBodyParas, lendings, users, articles, reservations);
		return "Lending/overview";
	}

	@GetMapping("/conflictPage/{username}")
	public String ReleaseConflictingLending(Model model, Principal p) {
		String username = p.getName();
		long id = postProccessor.FindUserIDByUser(users, username);
		LendingRepresentation filledConflicts = new LendingRepresentation(lendings, users, articles);
		model.addAttribute("id", id);
		model.addAttribute("username", username);
		model.addAttribute("conflicts", filledConflicts.FillConflicts(id));
		model.addAttribute("yourConflicts", filledConflicts.FillConflictsOwner(id));
		return "Lending/conflictPage";
	}

	@GetMapping("/transaction/{username}")
	public String ShowTransactions(Model model, Principal p) {
		String username = p.getName();
		long id = postProccessor.FindUserIDByUser(users, username);
		TransactionRepresentation transactionRepresentation = new TransactionRepresentation(transactions, users);
		model.addAttribute("id", id);
		model.addAttribute("username", username);
		model.addAttribute("givings", transactionRepresentation.FillGivings(id));
		model.addAttribute("recieves", transactionRepresentation.FillRecieves(id));
		return "Lending/transactionsPage";
	}


	//TODO: getmapping für testzwecke löschen
	@GetMapping("/test")
	public String test() {
		System.out.println(apiProcessor.getAccountInformation("Kathrin", Account.class));
		System.out.println(apiProcessor.getAccountInformation("Lisa", Account.class));
		System.out.println(apiProcessor.getAccountInformation("Olaf", Account.class));
		System.out.println(apiProcessor.getAccountInformation("Memfred", Account.class));

		return "Lending/overview";
	}

}

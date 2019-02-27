package de.hhu.rhinoshareapp.Representations.LendingProcessor;

import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.*;
import de.hhu.rhinoshareapp.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

public class PostProccessor {
	@Autowired
	private MailService mailservice;

	public HashMap<String, String> SplitString(String postBody) {
		HashMap<String, String> postBodyParas = new HashMap<>();
		String[] splittedPostBody = postBody.split("&");
		for (String para : splittedPostBody) {
			String[] splittedPara = para.split("=");
			postBodyParas.put(splittedPara[0], splittedPara[1].replace("+", " "));
		}
		return postBodyParas;
	}

	public void CreateNewLending(HashMap<String, String> postBodyParas, ArticleRepository articles, LendingRepository lendings, UserRepository users) {
		//set timeperiod information
		Calendar startDate = Calendar.getInstance();
		String[] datePieces = postBodyParas.get("startDate").split("-");
		startDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1]) - 1, Integer.parseInt(datePieces[2]));
		Calendar endDate = Calendar.getInstance();
		datePieces = postBodyParas.get("endDate").split("-");
		endDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1]) - 1, Integer.parseInt(datePieces[2]));

		//collect necessary information
		User lendingPerson = users.findUserByuserID(Long.parseLong(postBodyParas.get("requesterID"))).get();
		Article lendedArticle = articles.findArticleByarticleID(Long.parseLong(postBodyParas.get("articleID"))).get();
		//lendedArticle.setLendingUser(lendingPerson);
		lendedArticle.setRequestComment(postBodyParas.get("requestComment"));
		lendedArticle.setRequested(true);
		lendedArticle.setAvailable(false);
		articles.save(lendedArticle);

		// create new Lending
		Lending newLending = new Lending();
		newLending.setLendingPerson(lendingPerson);
		newLending.setLendedArticle(lendedArticle);
		newLending.setEndDate(endDate);
		newLending.setStartDate(startDate);
		newLending.FillFormattedDates();
		lendings.save(newLending);
	}


	public void CheckDecision(APIProcessor apiProcessor, HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles, UserRepository users, ReservationRepository reservations, TransactionRepository transactions) {
		if (postBodyParas.containsKey("choice")) {
			if (postBodyParas.get("choice").equals("accept")) {
				//Deposit check and lock depositamount

				Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
				Article article = lending.getLendedArticle();

				try {
					Account lendingAccount = apiProcessor.getAccountInformationWithId(lending.getLendingPerson().getUserID(), users);
					if (apiProcessor.hasEnoughMoneyForDeposit(lendingAccount, article.getArticleID(), articles)) {
						//make reservation
						Reservation reservation = apiProcessor.postCreateReservation(Reservation.class, lendingAccount, article);
						reservations.save(reservation);
						lending.setProPayReservation(reservation);
					}
				} catch (Exception e) {
					apiProcessor.setErrorOccurred(true);
					apiProcessor.addErrorMessage("Propay is not reachable, try it again later");
					e.printStackTrace();
					return;
				}
				article.setRequestComment("");
				article.setRequested(false);
				articles.save(article);

				lending.setAccepted(true);
				lendings.save(lending);

			} else {
				CleanUpLending(postBodyParas, lendings, articles);
			}
		} else if (postBodyParas.containsKey("choicereturn")) {
			Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
			Article article = lending.getLendedArticle();
			Account lendingAccount = apiProcessor.getAccountInformationWithId(lending.getLendingPerson().getUserID(), users);
			double amount = CalculateLendingPrice(lending, article);
			if (HasEnoughMoneyForRent(lendingAccount, article.getArticleID(), articles) && postBodyParas.get("choicereturn").equals("accept")) {
				try {
					apiProcessor.postTransfer(String.class, lendingAccount, article, amount);
					Calendar timeStamp = Calendar.getInstance();
					Transaction transaction = new Transaction(article.getOwner(), lending.getLendingPerson(), article, amount, timeStamp);
					transactions.save(transaction);
					apiProcessor.punishOrRealeseReservation(Account.class, lendingAccount, article, lending.getProPayReservation().getId(), "release");
					CleanUpLending(postBodyParas, lendings, articles);
					reservations.delete(lending.getProPayReservation());

				} catch (Exception e) {
					apiProcessor.setErrorOccurred(true);
					apiProcessor.addErrorMessage("Propay is not reachable, try it again later");
					e.printStackTrace();
					return;
				}
			} else {
				Lending tmpLending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
				tmpLending.setReturn(false);
				tmpLending.setConflict(true);
				lendings.save(tmpLending);
				//apiProcessor.punishOrRealeseReservation(Account.class, lendingAccount, article, lending.getProPayReservation().getId(), "punish");
				mailservice.sendConflict(tmpLending.getLendingID(), "Der Artikel wurde in einem nicht angebrachten Zustand zurückgegeben", tmpLending.getLendedArticle().getOwner().getUserID(), tmpLending.getLendingPerson().getUserID());
				//TODO: Admin muss richtig gefunden werden.
			}
		}
	}

	public boolean HasEnoughMoneyForRent(Account lenderAccountInformation, long articleID, ArticleRepository articles) {
		double amount = lenderAccountInformation.getAmount();
		double deposit = articles.findArticleByarticleID(articleID).get().getRent();
		if (amount >= deposit) {
			return true;
		} else {
			return false;
		}
	}

	public void CleanUpLending(HashMap<String, String> postBodyParas, LendingRepository lendings, ArticleRepository articles) {
		Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
		Article article = lending.getLendedArticle();
		//remove request out off article
		article.setRequestComment("");
		article.setRequested(false);
		//article.setLendingUser(null);
		article.setAvailable(true);
		articles.save(article);
		//remove lending
		lendings.delete(lending);
	}

	public void initializeNewReturn(HashMap<String, String> postBodyParas, LendingRepository lendings) {
		Lending lending = lendings.findLendingBylendingID(Long.parseLong(postBodyParas.get("lendingID"))).get();
		lending.setReturn(true);
		lendings.save(lending);
	}

	public double CalculateLendingPrice(Lending lending, Article article) {
		Calendar currentDate = Calendar.getInstance();
		Calendar startDate = lending.getStartDate();
		long time = currentDate.getTime().getTime() - startDate.getTime().getTime();
		long days = Math.round((double) time / (24. * 60. * 60. * 1000.));
		return (days + 1) * article.getRent();
	}

	public long FindUserIDByUser(UserRepository users, String username) {
		Optional<User> byUsername = users.findByUsername(username);
		return byUsername.get().getUserID();
	}

	public void SellArticle(HashMap<String, String> postBodyParas, ArticleRepository articles, UserRepository users, APIProcessor apiProcessor, TransactionRepository transactions) {
		try {
			Optional<User> requester = users.findUserByuserID(Long.parseLong(postBodyParas.get("requesterID")));
			Account buyingAccount = apiProcessor.getAccountInformationWithId(requester.get().getUserID(), users);
			Optional<Article> article = articles.findArticleByarticleID(Long.parseLong(postBodyParas.get("articleID")));
			apiProcessor.postTransfer(String.class, buyingAccount, article.get(), article.get().getSellingPrice());
			Calendar timeStamp = Calendar.getInstance();
			Transaction transaction = new Transaction(article.get().getOwner(), users.findUserByuserID(Long.parseLong(postBodyParas.get("requesterID"))).get(), article.get(), article.get().getSellingPrice(), timeStamp);
			transactions.save(transaction);
			articles.delete(article.get());
		} catch (Exception e) {
			apiProcessor.setErrorOccurred(true);
			apiProcessor.addErrorMessage("Propay is not reachable, try it again later");
			e.printStackTrace();
			return;
		}
	}
}

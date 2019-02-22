package de.hhu.rhinoshareapp.domain.database;


import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.Calendar;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

	@Autowired
	UserRepository users;

	@Autowired
	ArticleRepository articles;

	@Autowired
	LendingRepository lending;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException { //hier wird die Datenbank gefüllt
		System.out.println("Populating the database");

		Address testadress = Address.builder()
				.street("Teststre.")
				.houseNumber("18")
				.postCode("41749")
				.city("Viersen")
				.country("Germany")
				.build();

		User root = new User("Pasquier", "Jacques", testadress, "root", "japas102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
		User user = new User("Test", "Test", testadress, "user", "test102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
		User otherUser = new User("Test", "Test", testadress, "2", "jtest111@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");

		users.saveAll(Arrays.asList(root, user, otherUser));



		long id1 = user.getUserID();
		long id2 = otherUser.getUserID();

		Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 500, 25, true, null);
		Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 250, 25, true, null);
		Article testArticle3 = new Article("Grillkohle", "schon verbrannt", 25230, 88, false, null);
		testArticle1.setOwner(user);
		testArticle2.setOwner(user);
		testArticle3.setOwner(otherUser);

		users.save(user);
		users.save(user);
		users.save(otherUser);
		articles.save(testArticle1);
		articles.save(testArticle2);
		articles.save(testArticle3);

		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		date1.set(2019, 1, 12);
		date2.set(2019, 1, 12);
		Calendar date3 = Calendar.getInstance();
		Calendar date4 = Calendar.getInstance();
		date3.set(2019, 4, 12);
		date4.set(2019, 1, 12);
		Lending testLending1 = new Lending(date4, date3, user, testArticle2);
		Lending testLending2 = new Lending(date3, date4, user, testArticle1);
		testLending2.setConflict(true);
		lending.save(testLending1);
		lending.save(testLending2);
	}
}

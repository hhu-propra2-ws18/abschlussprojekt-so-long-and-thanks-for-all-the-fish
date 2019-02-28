package de.hhu.rhinoshareapp.domain.database;


import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.domain.model.*;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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

		User root = new User("Pasquier", "Jacques", testadress, "root", "rhinoshareconflict@gmail.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
		User user = new User("Test", "Test", testadress, "user", "rhinoshareconflict@gmail.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
		User otherUser = new User("Test", "Test", testadress, "2", "rhinoshareconflict@gmail.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
		User kratos = User.builder().role("ROLE_USER").name("Kratos").username("KnoppelKratos").email("rhinoshareconflict@gmail.com").password("$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO").address(testadress).build();
		APIProcessor apiProcessor = new APIProcessor();
		try {
			apiProcessor.getAccountInformation(kratos.getUsername(), Account.class);
			apiProcessor.getAccountInformation(user.getUsername(), Account.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		users.saveAll(Arrays.asList(root, user, otherUser, kratos));


		long id1 = user.getUserID();
		long id2 = otherUser.getUserID();

		Article testArticle1 = Article.builder().name("Rasenmäher").comment("funktioniert, kein Benzin, Schnitthöhe 1cm - 50m").deposit(500).rent(25).available(true).owner(user).build();
		Article testArticle2 = Article.builder().name("Geschirr").comment("nur ein bisschen zerbrochen, für 20 mann").deposit(250).rent(25).available(true).owner(user).build();
		Article testArticle3 = Article.builder().name("Grillkohle").comment("schon verbrannt").deposit(25230).rent(88).available(false).owner(otherUser).build();


		users.save(user);
		users.save(user);
		users.save(otherUser);
		articles.save(testArticle1);
		articles.save(testArticle2);
		articles.save(testArticle3);

		List<Article> all = articles.findAll();
		for (Article article : all) {
			System.out.println(article);
		}
	}
}

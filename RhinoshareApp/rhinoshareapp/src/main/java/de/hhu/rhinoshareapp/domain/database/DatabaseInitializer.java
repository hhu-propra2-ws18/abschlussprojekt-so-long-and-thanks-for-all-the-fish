package de.hhu.rhinoshareapp.domain.database;


import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.ChatMessageRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.Arrays;

@Component
public class DatabaseInitializer implements ServletContextInitializer {


	@Autowired
	UserRepository users;

	@Autowired
	ArticleRepository articles;

	@Autowired
	LendingRepository lending;

	@Autowired
	ChatMessageRepository chatMessageRepository;

	//Fills the database to add a little life to the application
	@Override
	public void onStartup(ServletContext servletContext) {
		 if (users.findAll().size() > 0) return;
		//Generate test addresses
		Address testAddress1 = Address.builder()
				.street("Teststr.")
				.houseNumber("18")
				.postCode("41749")
				.city("Viersen")
				.country("Germany")
				.build();

		Address testAddress2 = Address.builder()
				.street("Hauptstr.")
				.houseNumber("9a")
				.postCode("42254")
				.city("Hamburg")
				.country("Germany")
				.build();

		Address testAddress3 = Address.builder()
				.street("Bergstr.")
				.houseNumber("583")
				.postCode("92256")
				.city("Frankfurt")
				.country("Germany")
				.build();

		//Generate test users and two admins
		Person testRoot1 = new Person("Hammel", "Frank", testAddress1, "root1", "rhinoshareconflict@gmail.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
		Person testRoot2 = new Person("Olo", "Hans", testAddress3, "root2", "rhinoshareconflict@gmail.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
		Person testPerson1 = new Person("Müller", "Klaus", testAddress2, "kMueller", "klausmueller@rhinoshare.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
		Person testPerson2 = new Person("Faust", "Herbert", testAddress3, "hFaust", "herbertfaust@rhinoshare.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
		Person testPerson3 = new Person("Baum", "Tina", testAddress2, "tineTree", "tinabaum@rhinoshare.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
		Person testPerson4 = new Person("Stein", "Maria", testAddress1, "moosStein", "mariastein@rhinoshare.com", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");

		users.saveAll(Arrays.asList(testRoot1, testRoot2, testPerson1, testPerson2, testPerson3, testPerson4));

		//Generate test articles
		Article testArticle1 = Article.builder().name("Rasenmäher").comment("funktioniert, kein Benzin, Schnitthöhe 3cm - 10cm").deposit(200).rent(25).available(true).owner(testPerson1).build();
		Article testArticle2 = Article.builder().name("Heckenschere").comment("Gute Heckenschere, vorsicht mit den Fingern!").deposit(50).rent(10).sellingPrice(75).available(true).forSale(true).owner(testPerson1).build();
		Article testArticle3 = Article.builder().name("Grill").comment("gut für den Sommer! Ohne Kohle.").deposit(50).rent(30).available(true).owner(testPerson2).build();
		Article testArticle4 = Article.builder().name("Käsemesser").comment("Schneidet Käse super gut. Riecht auch nach Käse.").deposit(25).rent(5).sellingPrice(15).available(false).forSale(true).owner(testPerson3).build();
		Article testArticle5 = Article.builder().name("Trampolin").comment("Toll für Kinder!").deposit(100).rent(25).available(true).owner(testPerson4).build();
		Article testArticle6 = Article.builder().name("Aktenvernichter").comment("Wenn mal was verschwinden muss...").deposit(30).rent(10).sellingPrice(50).available(true).forSale(true).owner(testPerson4).build();
		Article testArticle7 = Article.builder().name("Leiter").comment("Für die kleineren Leute").deposit(50).rent(10).available(false).owner(testPerson2).build();
		Article testArticle8 = Article.builder().name("Drucker").comment("Wenn mal schnell was gedruckt werden muss, Tinte zahlen Sie!").deposit(80).rent(10).available(true).owner(testPerson3).build();
		Article testArticle9 = Article.builder().name("Beamer").comment("Kinofeeling für Zuhause!").deposit(500).rent(30).available(true).owner(testPerson4).build();
		Article testArticle10 = Article.builder().name("Raclettegrill").comment("Gut für Familienfeiern").deposit(50).rent(15).sellingPrice(50).available(false).forSale(true).owner(testPerson2).build();
		Article testArticle11 = Article.builder().name("Presslufthammer").comment("Wenn man mal für seinen großen Mitbewohner einen Eingang vergrößern muss ;)").deposit(200).rent(30).available(false).owner(testPerson2).build();

		articles.saveAll(Arrays.asList(testArticle1, testArticle2, testArticle3, testArticle4, testArticle5, testArticle6, testArticle7, testArticle8, testArticle9, testArticle10, testArticle11));
	}
}

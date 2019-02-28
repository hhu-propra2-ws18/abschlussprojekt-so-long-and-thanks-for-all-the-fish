package de.hhu.rhinoshareapp.infrastructure.web;


import de.hhu.rhinoshareapp.controller.article.ArticleController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

	@Autowired
	ArticleController controller;

	@Autowired
	MockMvc mvc;

	@MockBean
	Model m;

	@MockBean
	LendingRepository lendingRepo;

	@MockBean
	UserRepository userRepo;

	@MockBean
	ArticleRepository articleRepo;

	@MockBean
	ImageRepository imageRepo;

	@MockBean
	MailService mailService;

	@MockBean
	ReservationRepository reserveRepo;

	@MockBean
	TransactionRepository transRepo;

	@Mock
	Principal p;

	@Mock
	Principal notOwner;

	Article article;

	@Before
	public void setUp() {
		article = new Article("Motorsäge", "wie neu", 250, 25, true, null);
		Optional<Article> optionalArticle = Optional.of(article);
		Mockito.when(articleRepo.findById((long) 1)).thenReturn(optionalArticle);
		User testUser1 = new User("Fritz", "Muller", null, "fritz", "fritz@mail.com", "1234", "user");
		User testUser2 = new User("Tom", "Muller", null, "tom", "tom@mail.com", "1234", "user");
		Optional<User> optionalUser = Optional.of(testUser1);
		Optional<User> optionalUser2 = Optional.of(testUser2);
		article.setOwner(testUser1);

		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		date1.set(2019, 1, 12);
		date2.set(2019, 1, 12);
		Calendar date3 = Calendar.getInstance();
		Calendar date4 = Calendar.getInstance();
		date3.set(2019, 4, 12);
		date4.set(2019, 1, 12);

		Lending testLending1 = new Lending(date1, date2, testUser1, article);
		Optional<Lending> olending = Optional.of(testLending1);

		Mockito.when(userRepo.findByUsername("fritz")).thenReturn(optionalUser);
		Mockito.when(userRepo.findByUsername("tom")).thenReturn(optionalUser2);
		Mockito.when(p.getName()).thenReturn("fritz");
		Mockito.when(notOwner.getName()).thenReturn("tom");
		Mockito.when(lendingRepo.findLendingBylendedArticle(article)).thenReturn(olending);


	}

	@Test
	public void openConflictTest() {
		Article article = new Article("Motorsäge", "wie neu", 250, 25, true, null);
		assertEquals("redirect:/", controller.redirectToMainpage());
		assertEquals("redirect:/article/1", controller.editArticlePostMapping(article, 1));
		assertEquals("redirect:/article/", controller.deleteArticleFromDB(1));
	}

	@Test
	public void isPictureEmptyTest() {
		Article article = new Article("Motorsäge", "wie neu", 250, 25, true, null);
		assertEquals(true, article.imageIsEmpty());
	}

	@Test
	public void checkLoginTest() {

		assertEquals(true, controller.checkIfLoggedInIsOwner(p, article));
	}

	@Test
	public void editArticleTest() {

		assertEquals("Article/editArticle", controller.editArticle(m, 1, p));
		assertEquals("error/403", controller.editArticle(m, 1, notOwner));
	}

	@Test
	public void viewMyArticlesTest() {
		assertEquals("Article/viewFromPerson", controller.viewMyArticles(m, p));
	}

	@Test
	public void privateViewTest() {
		assertEquals("Article/articleView", controller.privateArticleView(m, 1, p));
	}

	@Test
	public void newArticleTest() {
		assertEquals("Article/newArticle", controller.newArticle(m, p));

	}

	@Test
	public void saveArticleTest() throws IOException {
		Article newArt = new Article("Motorsäge 2", "wie neu ", 500, 25, true, null);
		assertEquals("redirect:/article/", controller.saveArticle(newArt, p, m));

	}

	@Test
	public void deleteArticleTest() {
		assertEquals("Article/deleteArticle", controller.deleteAArticle(1, m, p));
		assertEquals("error/403", controller.deleteAArticle(1, m, notOwner));

	}

	@Test
	public void searchArticleTest() {
		assertEquals("Article/searchArticle", controller.searchForArticle("kohle", m, notOwner));

	}
}

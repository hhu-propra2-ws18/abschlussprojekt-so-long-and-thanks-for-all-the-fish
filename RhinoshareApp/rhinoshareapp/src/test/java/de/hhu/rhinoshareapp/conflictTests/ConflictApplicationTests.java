package de.hhu.rhinoshareapp.conflictTests;


import de.hhu.rhinoshareapp.controller.MainpageController;
import de.hhu.rhinoshareapp.controller.conflict.ConflictController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
@SuppressFBWarnings//siehe kommentar bei unbenutzen gemockten repros
public class ConflictApplicationTests {
	@Autowired
	ConflictController controller;

	@Autowired
	MainpageController cm;

	@MockBean
	Model m;

	@MockBean
	LendingRepository lendingRepo;

	@MockBean
	UserRepository userRepo;

	@MockBean
	ArticleRepository articleRepo;

	//Diese repositories müssen annotiert werden, weil es sich hier um Controller Tests handelt
	@MockBean
	ImageRepository imageRepository;
	@MockBean
	ChatMessageRepository chatMessageRepository;
	@MockBean
	MailService mailService;
	@MockBean
	ReservationRepository reservationRepository;
	@MockBean
	TransactionRepository transactionRepository;

	@Mock
	Principal p;


	@Before
	public void setUp() {

		User testUser1 = new User("Jeff", "Nosbusch", null, "jeff", "jeff@mail.com", "1234", "user");
		User testUser2 = new User("George", "Pi", null, "george", "george@mail.com", "1234", "user");
		User testUser3 = new User("Franz", "Hoff", null, "franz", "franz@mail.com", "1234", "user");

		Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 500, 25, true, null);
		Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 250, 25, true, null);
		Article testArticle3 = new Article("Grillkohle", "schon verbrannt", 25230, 88, false, null);

		userRepo.save(testUser1);
		userRepo.save(testUser2);
		userRepo.save(testUser3);
		articleRepo.save(testArticle1);
		articleRepo.save(testArticle2);
		articleRepo.save(testArticle3);

		Calendar date1 = Calendar.getInstance();
		Calendar date2 = Calendar.getInstance();
		date1.set(2019, 1, 12);
		date2.set(2019, 1, 12);
		Calendar date3 = Calendar.getInstance();
		Calendar date4 = Calendar.getInstance();
		date3.set(2019, 4, 12);
		date4.set(2019, 1, 12);

		Lending testLending1 = new Lending(date1, date2, testUser1, testArticle2);
		Lending testLending2 = new Lending(date3, date4, testUser2, testArticle1);
		testLending2.setConflict(true);
		testLending1.getLendedArticle().setOwner(testUser3);
		lendingRepo.save(testLending1);
		lendingRepo.save(testLending2);


		Optional<User> oUser1 = Optional.of(testUser1);
		Optional<User> oUser2 = Optional.of(testUser2);
		Optional<User> oUser3 = Optional.of(testUser3);
		Optional<Lending> oLending1 = Optional.of(testLending1);
		Optional<Lending> oLending2 = Optional.of(testLending2);

		Mockito.when(userRepo.findByUsername("jeff"))
				.thenReturn(oUser1);
		Mockito.when(userRepo.findByUsername("george"))
				.thenReturn(oUser2);
		Mockito.when(userRepo.findByUsername("franz"))
				.thenReturn(oUser3);
		Mockito.when(userRepo.findUserByuserID(1)).thenReturn(oUser1);
		Mockito.when(userRepo.findUserByuserID(2)).thenReturn(oUser2);
		Mockito.when(userRepo.findUserByuserID(3)).thenReturn(oUser3);
		Mockito.when(lendingRepo.findAllByIsConflict(true)).thenReturn(null);
		Mockito.when(lendingRepo.findLendingBylendingID(7)).thenReturn(oLending1);
		Mockito.when(lendingRepo.save(testLending1)).thenReturn(testLending1);
		Mockito.when(lendingRepo.findLendingBylendingID(8)).thenReturn(oLending2);

		p = Mockito.mock(Principal.class);
		Mockito.when(p.getName()).thenReturn("jeff");


	}

	@Test
	public void contexLoads() throws Exception {
		assertNotEquals(null, controller);
	}

	@Test
	public void openConflictTest() {
		assertEquals("redirect:/openConflict", controller.openConflictpost(m, "open", 7, ""));
		assertEquals("redirect:/", controller.openConflictpost(m, "notOpen", 7, ""));
		assertEquals("redirect:/", controller.openConflictpost(m, "open", 7, "testkgzgjkg"));
	}

	@Ignore
	@Test
	public void testPostMappingConflictSolved() {

		assertEquals("redirect:/borrowerWin", controller.conflictSolved("winBorrower", 7));
		assertEquals("redirect:/ownerWin", controller.conflictSolved("winOwner", 7));
		assertEquals("redirect:/admin/conflicthandling", controller.conflictSolved("", 7));
	}

	@Test
	public void testMainpage() {
		assertEquals("Article/viewAll", cm.viewAll(m, p));
	}

	@Test
	public void testmain() {
		assertEquals("/conflict/conflictUserOpen", controller.openConflict(m, p, 1));
	}

}



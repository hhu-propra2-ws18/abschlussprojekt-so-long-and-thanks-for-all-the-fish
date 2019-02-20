package de.ProPra.Lending;

import de.ProPra.Lending.Dataaccess.PostProccessor;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.ReservationRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Dataaccess.Representations.LendingRepresentation;
import de.ProPra.Lending.Model.Account;
import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingApplicationTests {


	PostProccessor postProccessor = new PostProccessor();
	APIProcessor apiProcessor = new APIProcessor();
	@Test
	public void UserHasOneLendingWithWarning(){
		//Arrange
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

		Optional <User> testUser = Optional.ofNullable(User.builder().userID(1).name("testUser").build());
		when(userRepository.findUserByuserID(1)).thenReturn(testUser);

		Calendar endDate = Calendar.getInstance();
		endDate.set(2019, 1, 14);
		Lending testLending = Lending.builder().lendingPerson(testUser.get()).endDate(endDate).build();
		List<Lending> testLendingList = new ArrayList<>();
		testLendingList.add(testLending);
		when(lendingRepository.findAllBylendingPersonAndIsAcceptedAndIsReturn(testUser.get(), true, false)).thenReturn(testLendingList);

		//Act
		LendingRepresentation lendingRepresentation = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
		List<Lending> resultLendings = lendingRepresentation.FillLendings(1);
		Lending resultLending = resultLendings.get(0);

		//Assert
		Assert.assertEquals("ATTENTION: YOU HAVE RETURN THIS ARTICLE", resultLending.getWarning());
	}

	@Test
	public void UserHasOneLendingWithoutWarning(){
		//Arrange
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

		Optional <User> testUser = Optional.ofNullable(User.builder().userID(1).name("testUser").build());
		when(userRepository.findUserByuserID(1)).thenReturn(testUser);

		// Set EndDate one year after current Date
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.getInstance().get(Calendar.YEAR)+1, endDate.MONTH, endDate.DATE);
		System.out.println(endDate);
		Lending testLending = Lending.builder().lendingPerson(testUser.get()).endDate(endDate).build();
		List<Lending> testLendingList = new ArrayList<>();
		testLendingList.add(testLending);
		when(lendingRepository.findAllBylendingPersonAndIsAcceptedAndIsReturn(testUser.get(), true, false)).thenReturn(testLendingList);

		Calendar currentDate = Calendar.getInstance();
		long time = endDate.getTime().getTime() - currentDate.getTime().getTime();
		long expectedDays = Math.round( (double)time / (24. * 60.*60.*1000.) );

		//Act
		LendingRepresentation lendingRepresentation = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
		List<Lending> resultLendings = lendingRepresentation.FillLendings(1);
		Lending resultLending = resultLendings.get(0);

		//Assert
		Assert.assertEquals("You can use this article without worries for the next "+expectedDays+" days", resultLending.getWarning());
	}
	@Test
	public void PostBodyWithTwoParas(){
		//Arrange
		String testString = "Para1=Hallo&Para2=Welt";
		HashMap<String, String> expectedPostBody = new HashMap<>();
		expectedPostBody.put("Para1", "Hallo");
		expectedPostBody.put("Para2", "Welt");
		//Act
		HashMap<String, String> resultPostBody = postProccessor.SplitString(testString);
		//Assert
		Assert.assertEquals(expectedPostBody,resultPostBody);
	}
	@Test
	public void PostBodyWithOneParaContainingPlus(){
		//Arrange
		String testString = "Para=Hallo+Welt";
		HashMap<String, String> expectedPostBody = new HashMap<>();
		expectedPostBody.put("Para", "Hallo Welt");
		//Act
		HashMap<String, String> resultPostBody = postProccessor.SplitString(testString);
		//Assert
		Assert.assertEquals(expectedPostBody,resultPostBody);
	}
	@Test
	public void UserAcceptLending(){
		//Arrange
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);
		ReservationRepository reservations = Mockito.mock(ReservationRepository.class);

		Article testArticle = Article.builder().build();
		Optional<Lending> testLending = Optional.ofNullable(Lending.builder().lendingID(1).lendedArticle(testArticle).build());
		when(lendingRepository.findLendingBylendingID(1)).thenReturn(testLending);

		HashMap<String,String> testMap = new HashMap<>();
		testMap.put("choice","accept");
		testMap.put("lendingID", "1");
		//Act
		postProccessor.CheckDecision(testMap, lendingRepository, articleRepository, userRepository, reservations);
		//Assert
		Assert.assertEquals(true,testLending.get().isAccepted());
		Assert.assertEquals(false,testLending.get().getLendedArticle().isRequested());
	}

	/*@Test
	public void CreateNewLendingTest(){
		//Arrange
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

		HashMap<String,String> testMap = new HashMap<>();
		testMap.put("startDate","02-01-2019");
		testMap.put("endDate", "03-01-2019");
		testMap.put("articleID", "1");
		testMap.put("requesterID", "1");

		Calendar startDate = Calendar.getInstance();

		when(Calendar.getInstance()).thenReturn(startDate);
		Calendar endDate = Calendar.getInstance();
		Calendar startDate = Calendar.getInstance();
		String[] datePieces = testMap.get("startDate").split("-");
		startDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));
		Calendar endDate = Calendar.getInstance();
		datePieces = testMap.get("endDate").split("-");
		endDate.set(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));

		Optional<User> testUser = Optional.ofNullable(User.builder().userID(1).build());
		Optional<Article> testArticle = Optional.ofNullable(Article.builder().articleID(1).lendingUser(testUser.get()).isRequested(true).build());
		Optional<User> testUser2 = Optional.ofNullable(User.builder().userID(1).build());
		Optional<Article> testArticle2 = Optional.ofNullable(Article.builder().articleID(1).build());
		Optional<Lending> testLending = Optional.ofNullable(Lending.builder().startDate(startDate).endDate(endDate).lendedArticle(testArticle.get()).lendingID(1).lendingPerson(testUser.get()).build());

		//PostProccessor postProccessor = Mockito.mock(PostProccessor.class);

		when(userRepository.findUserByuserID(1)).thenReturn(testUser2);
		when(articleRepository.findArticleByarticleID(1)).thenReturn(testArticle2);
		//Act
		postProccessor.CreateNewLending(testMap, articleRepository, lendingRepository, userRepository);
		testLending.get().setLendingID(0);

		//Assert
		Mockito.verify(lendingRepository).save(testLending.get());
	}*/
	@Test
	public void hasEnoughMoneyForLendingTest(){
		//Arrange
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);
		Account testAccount = Account.builder().amount(1000).build();
		Article testArticle = Article.builder().deposit(500).build();
		long testId = 1;
		Mockito.when(articleRepository.findArticleByarticleID(1)).thenReturn(Optional.ofNullable(testArticle));
		//Act
		boolean hasEnoughMoneyForDeposit = apiProcessor.hasEnoughMoneyForDeposit(testAccount, testId, articleRepository);
		//Assert
		Assert.assertEquals(true, hasEnoughMoneyForDeposit);
	}
	@Test
	public void hasntEnoughMoneyForLendingTest(){
		//Arrange
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);
		Account testAccount = Account.builder().amount(500).build();
		Article testArticle = Article.builder().deposit(1000).build();
		long testId = 1;
		Mockito.when(articleRepository.findArticleByarticleID(1)).thenReturn(Optional.ofNullable(testArticle));
		//Act
		boolean hasEnoughMoneyForDeposit = apiProcessor.hasEnoughMoneyForDeposit(testAccount, testId, articleRepository);
		//Assert
		Assert.assertEquals(false, hasEnoughMoneyForDeposit);
	}
	@Test
	public void hasLendingForThreeDays(){
		//Arrange
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE)-3);
		Article article = Article.builder().rent(10).build();
		Lending lending = Lending.builder().startDate(startDate).build();

		//Act
		double calculateLendingPrice = postProccessor.CalculateLendingPrice(lending, article);
		//Assert
		Assert.assertEquals(40, calculateLendingPrice, 0.001);
	}
	@Test
	public void hasLendingForSameDay(){
		//Arrange
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
		Article article = Article.builder().rent(25).build();
		Lending lending = Lending.builder().startDate(startDate).build();

		//Act
		double calculateLendingPrice = postProccessor.CalculateLendingPrice(lending, article);
		//Assert
		Assert.assertEquals(25, calculateLendingPrice, 0.001);
	}
	@Test
	public void hasLendingForFiveDaysWithDouble(){
		//Arrange
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE)-5);
		Article article = Article.builder().rent(20.125).build();
		Lending lending = Lending.builder().startDate(startDate).build();

		//Act
		double calculateLendingPrice = postProccessor.CalculateLendingPrice(lending, article);
		//Assert
		Assert.assertEquals(120.75, calculateLendingPrice, 0.001);
	}
}


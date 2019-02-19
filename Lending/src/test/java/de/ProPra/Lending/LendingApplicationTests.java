package de.ProPra.Lending;

import de.ProPra.Lending.Dataaccess.PostProccessor;
import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Dataaccess.Representations.LendingRepresentation;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingApplicationTests {


	PostProccessor postProccessor = new PostProccessor();
	@Test
	public void UserHasOneLendingWithWarning(){
		//Arrange
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

		Optional <User> testUser = Optional.ofNullable(User.builder().userID(1).name("testUser").build());
		Mockito.when(userRepository.findUserByuserID(1)).thenReturn(testUser);

		Calendar endDate = Calendar.getInstance();
		endDate.set(2019, 1, 14);
		Lending testLending = Lending.builder().lendingPerson(testUser.get()).endDate(endDate).build();
		List<Lending> testLendingList = new ArrayList<>();
		testLendingList.add(testLending);
		Mockito.when(lendingRepository.findAllBylendingPersonAndIsAcceptedAndIsReturn(testUser.get(), true, false)).thenReturn(testLendingList);

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
		Mockito.when(userRepository.findUserByuserID(1)).thenReturn(testUser);

		// Set EndDate one year after current Date
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.getInstance().get(Calendar.YEAR)+1, endDate.MONTH, endDate.DATE);
		System.out.println(endDate);
		Lending testLending = Lending.builder().lendingPerson(testUser.get()).endDate(endDate).build();
		List<Lending> testLendingList = new ArrayList<>();
		testLendingList.add(testLending);
		Mockito.when(lendingRepository.findAllBylendingPersonAndIsAcceptedAndIsReturn(testUser.get(), true, false)).thenReturn(testLendingList);

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
	/*@Test
	public void UserAcceptLending(){
		//Arrange
		UserRepository userRepository = Mockito.mock(UserRepository.class);
		LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
		ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

		Article testArticle = Article.builder().build();
		Optional<Lending> testLending = Optional.ofNullable(Lending.builder().lendingID(1).lendedArticle(testArticle).build());
		Mockito.when(lendingRepository.findLendingBylendingID(1)).thenReturn(testLending);

		HashMap<String,String> testMap = new HashMap<>();
		testMap.put("choice","accept");
		testMap.put("lendingID", "1");
		//Act
		postProccessor.CheckDecision(testMap, lendingRepository, articleRepository, userRepository);
		//Assert
		Assert.assertEquals(true,testLending.get().isAccepted());
		Assert.assertEquals(false,testLending.get().getLendedArticle().isRequested());
	}*/
}


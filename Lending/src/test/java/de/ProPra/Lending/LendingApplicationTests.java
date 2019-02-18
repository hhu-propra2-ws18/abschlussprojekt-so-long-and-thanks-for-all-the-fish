package de.ProPra.Lending;

import de.ProPra.Lending.Dataaccess.Repositories.ArticleRepository;
import de.ProPra.Lending.Dataaccess.Repositories.LendingRepository;
import de.ProPra.Lending.Dataaccess.Repositories.UserRepository;
import de.ProPra.Lending.Dataaccess.Representations.LendingRepresentation;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingApplicationTests {


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

		//Act
		LendingRepresentation lendingRepresentation = new LendingRepresentation(lendingRepository, userRepository, articleRepository);
		List<Lending> resultLendings = lendingRepresentation.FillLendings(1);
		Lending resultLending = resultLendings.get(0);

		//Assert
		Assert.assertEquals("You can use this article without worries", resultLending.getWarning());
	}

}


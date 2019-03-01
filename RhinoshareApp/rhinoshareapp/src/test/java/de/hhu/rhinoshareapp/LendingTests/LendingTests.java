package de.hhu.rhinoshareapp.LendingTests;


import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.Representations.LendingProcessor.PostProccessor;
import de.hhu.rhinoshareapp.Representations.LendingRepresentation;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Account;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingTests {


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

    PostProccessor postProccessor = new PostProccessor();
    APIProcessor apiProcessor = new APIProcessor();

    @Test
    public void UserHasOneLendingWithWarning() {
        //Arrange
        UserRepository serviceUserProvider = Mockito.mock(UserRepository.class);
        LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
        ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

        Optional<User> testUser = Optional.ofNullable(User.builder().userID(1).name("testUser").build());
        when(serviceUserProvider.findUserByuserID(1)).thenReturn(testUser);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2019, 1, 14);
        Lending testLending = Lending.builder().lendingPerson(testUser.get()).endDate(endDate).build();
        List<Lending> testLendingList = new ArrayList<>();
        testLendingList.add(testLending);
        when(lendingRepository.findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflictAndIsRequestedForSale(testUser.get(), true, false, false, false)).thenReturn(testLendingList);

        //Act
        LendingRepresentation lendingRepresentation = new LendingRepresentation(lendingRepository, serviceUserProvider, articleRepository);
        List<Lending> resultLendings = lendingRepresentation.fillLendings(1);
        Lending resultLending = resultLendings.get(0);

        //Assert
        Assert.assertEquals("ATTENTION: YOU HAVE RETURN THIS ARTICLE", resultLending.getWarning());
    }

    @Test
    public void UserHasOneLendingWithoutWarning() {
        //Arrange
        UserRepository serviceUserProvider = Mockito.mock(UserRepository.class);
        LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
        ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);

        Optional<User> testUser = Optional.ofNullable(User.builder().userID(1).name("testUser").build());
        when(serviceUserProvider.findUserByuserID(1)).thenReturn(testUser);

        // Set EndDate one year after current Date
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.getInstance().get(Calendar.YEAR) + 1, Calendar.MONTH, Calendar.DATE);
        System.out.println(endDate);
        Lending testLending = Lending.builder().lendingPerson(testUser.get()).endDate(endDate).build();
        List<Lending> testLendingList = new ArrayList<>();
        testLendingList.add(testLending);
        when(lendingRepository.findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflictAndIsRequestedForSale(testUser.get(), true, false, false,false)).thenReturn(testLendingList);

        Calendar currentDate = Calendar.getInstance();
        long time = endDate.getTime().getTime() - currentDate.getTime().getTime();
        long expectedDays = Math.round((double) time / (24. * 60. * 60. * 1000.));

        //Act
        LendingRepresentation lendingRepresentation = new LendingRepresentation(lendingRepository, serviceUserProvider, articleRepository);
        List<Lending> resultLendings = lendingRepresentation.fillLendings(1);
        Lending resultLending = resultLendings.get(0);

        //Assert
        Assert.assertEquals("You can use this article without worries for the next " + expectedDays + " days", resultLending.getWarning());
    }

    @Test
    public void PostBodyWithTwoParas() {
        //Arrange
        String testString = "Para1=Hallo&Para2=Welt";
        HashMap<String, String> expectedPostBody = new HashMap<>();
        expectedPostBody.put("Para1", "Hallo");
        expectedPostBody.put("Para2", "Welt");
        //Act
        HashMap<String, String> resultPostBody = postProccessor.splitString(testString);
        //Assert
        Assert.assertEquals(expectedPostBody, resultPostBody);
    }

    @Test
    public void PostBodyWithOneParaContainingPlus() {
        //Arrange
        String testString = "Para=Hallo+Welt";
        HashMap<String, String> expectedPostBody = new HashMap<>();
        expectedPostBody.put("Para", "Hallo Welt");
        //Act
        HashMap<String, String> resultPostBody = postProccessor.splitString(testString);
        //Assert
        Assert.assertEquals(expectedPostBody, resultPostBody);
    }

    @Test
    public void UserAcceptLending() {
        //Arrange
        UserRepository serviceUserProvider = Mockito.mock(UserRepository.class);
        LendingRepository lendingRepository = Mockito.mock(LendingRepository.class);
        ArticleRepository articleRepository = Mockito.mock(ArticleRepository.class);
        ReservationRepository reservations = Mockito.mock(ReservationRepository.class);
        TransactionRepository transactions = Mockito.mock(TransactionRepository.class);

        Account account = Account.builder().build();
        User user = User.builder().userID(1).name("peter").build();
        Article testArticle = Article.builder().owner(user).build();
        Optional<Lending> testLending = Optional.ofNullable(Lending.builder().lendingID(1).lendedArticle(testArticle).build());
        when(lendingRepository.findLendingBylendingID(1)).thenReturn(testLending);
        APIProcessor apiProcessor2 = Mockito.mock(APIProcessor.class);

        Mockito.when(apiProcessor2.getAccountInformationWithId(user.getUserID(), serviceUserProvider)).thenReturn(account);

        HashMap<String, String> testMap = new HashMap<>();
        testMap.put("choice", "accept");
        testMap.put("lendingID", "1");
        //Act
        postProccessor.proccessPostRequest(apiProcessor, testMap, lendingRepository, articleRepository, serviceUserProvider, reservations, transactions);
        //Assert
        Assert.assertEquals(false, testLending.get().isAccepted());
        Assert.assertEquals(false, testLending.get().getLendedArticle().isRequested());
    }

    @Test
    public void hasEnoughMoneyForLendingTest() {
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
    public void hasntEnoughMoneyForLendingTest() {
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
    public void hasLendingForThreeDays() {
        //Arrange
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE) - 3);
        Article article = Article.builder().rent(10).build();
        Lending lending = Lending.builder().startDate(startDate).build();

        //Act
        double calculateLendingPrice = postProccessor.calculateLendingPrice(lending, article);
        //Assert
        Assert.assertEquals(40, calculateLendingPrice, 0.001);
    }

    @Test
    public void hasLendingForSameDay() {
        //Arrange
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE));
        Article article = Article.builder().rent(25).build();
        Lending lending = Lending.builder().startDate(startDate).build();

        //Act
        double calculateLendingPrice = postProccessor.calculateLendingPrice(lending, article);
        //Assert
        Assert.assertEquals(25, calculateLendingPrice, 0.001);
    }

    @Test
    public void hasLendingForFiveDays() {
        //Arrange
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE) - 5);
        Article article = Article.builder().rent(20).build();
        Lending lending = Lending.builder().startDate(startDate).build();

        //Act
        double calculateLendingPrice = postProccessor.calculateLendingPrice(lending, article);
        //Assert
        Assert.assertEquals(120, calculateLendingPrice, 0.001);
    }

    @Test
    public void overViewTest() {


    }
}

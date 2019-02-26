//package de.hhu.rhinoshareapp.domain.service;
//
//import de.hhu.rhinoshareapp.controller.conflict.ConflictController;
//import de.hhu.rhinoshareapp.domain.mail.MailService;
//import de.hhu.rhinoshareapp.domain.model.Article;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.ui.Model;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class ArticleRepositoryIntegrationTest {
//
//	@Autowired
//	private TestEntityManager entityManager;
//
//	@Autowired
//	private ArticleRepository articleRepository;
//
//	@MockBean
//	Model m;
//
//	@MockBean
//	LendingRepository lendingRepo;
//
//	@MockBean
//	UserRepository userRepo;
//
//	@MockBean
//	ImageRepository imageRepo;
//
//	@MockBean
//	MailService mailService;
//
//	@MockBean
//	ReservationRepository reserveRepo;
//
//	@MockBean
//	TransactionRepository transRepo;
//
//	@Test
//	public void whenFindByArticleID_thenReturnArticle() throws IOException, SQLException {
//		//Arrange
//		Article chainsaw = Article.builder().build();
//		entityManager.persist(chainsaw);
//		entityManager.flush();
//
//		//Act
//		Article found = articleRepository.findById(chainsaw.getArticleID());
//
//		//Assert
//		assertThat(found).isEqualTo(chainsaw);
//	}
//
//	@Test
//	public void whenFindByPersonID_thenReturnAllArticlesWithCorrespondingPersonID() throws IOException, SQLException {
//		//Arrange
//		Article chainsaw = Article.builder().personID(15).build();
//		Article lawnmower = Article.builder().personID(15).build();
//		Article snowplow = Article.builder().personID(1).build();
//		entityManager.persist(chainsaw);
//		entityManager.persist(lawnmower);
//		entityManager.persist(snowplow);
//		entityManager.flush();
//
//		//Act
//		List<Article> found = (List<Article>) articleRepository.findById(15);
//
//		//Assert
//		assertThat(chainsaw).isIn(found);
//		assertThat(lawnmower).isIn(found);
//		assertThat(snowplow).isNotIn(found);
//	}
//}

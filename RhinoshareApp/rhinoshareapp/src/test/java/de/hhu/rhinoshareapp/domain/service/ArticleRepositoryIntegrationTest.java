package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.*;
import de.hhu.rhinoshareapp.domain.model.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	public void whenFindByArticleID_thenReturnArticle() throws IOException, SQLException {
		//Arrange
		Article chainsaw = Article.builder().build();
		entityManager.persist(chainsaw);
		entityManager.flush();

		//Act
		Article found = articleRepository.findById(chainsaw.getArticleID());

		//Assert
		assertThat(found).isEqualTo(chainsaw);
	}

	@Test
	public void whenFindByPersonID_thenReturnAllArticlesWithCorrespondingPersonID() throws IOException, SQLException {
		//Arrange
		Article chainsaw = Article.builder().personID(15).build();
		Article lawnmower = Article.builder().personID(15).build();
		Article snowplow = Article.builder().personID(1).build();
		entityManager.persist(chainsaw);
		entityManager.persist(lawnmower);
		entityManager.persist(snowplow);
		entityManager.flush();

		//Act
		List<Article> found = articleRepository.findByPersonID(15);

		//Assert
		assertThat(chainsaw).isIn(found);
		assertThat(lawnmower).isIn(found);
		assertThat(snowplow).isNotIn(found);
	}
}

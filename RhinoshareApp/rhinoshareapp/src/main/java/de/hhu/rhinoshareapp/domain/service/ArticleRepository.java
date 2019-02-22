package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	Article findById(long id);
	List<Article> findByUserID(long id);
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String nameQuery, String commentQuery);
	List<Article> findAllByownerUser(User user);
	Optional<Article> findArticleByarticleID(long id);
	List<Article> findAllArticleByownerUserAndIsRequested(User user, boolean isRequested);
}

package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	Article findById(long id);
	List<Article> findByOwner(User owner);
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String nameQuery, String commentQuery);
	List<Article> findAllByowner(User user);
	Optional<Article> findArticleByarticleID(long id);
	List<Article> findAllArticleByOwnerAndIsRequested(User user, boolean isRequested);
}

package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	List<Article> findAllByOwner(User owner);
	Optional<Article> findArticleByarticleID(long id);
	List<Article> findAllArticleByOwnerAndIsRequested(User user, boolean isRequested);
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String nameQuery, String commentQuery);
}

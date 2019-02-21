package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	Article findById(long id);
	List<Article> findByPersonID(long id);
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String nameQuery, String commentQuery);
	List<Article> findAllByownerUser(ServiceUser serviceUser);
	Optional<Article> findArticleByarticleID(long id);
	List<Article> findAllArticleByownerUserAndIsRequested(ServiceUser serviceUser, boolean isRequested);
}

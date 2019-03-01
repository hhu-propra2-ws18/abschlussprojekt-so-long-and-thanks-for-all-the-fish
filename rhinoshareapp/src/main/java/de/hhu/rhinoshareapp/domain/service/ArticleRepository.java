package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ArticleRepository extends CrudRepository<Article, Long> {

	List<Article> findAllByOwner(Person owner);
	Optional<Article> findArticleByarticleID(long id);
	List<Article> findAllArticleByOwnerAndIsRequested(Person person, boolean isRequested);
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String nameQuery, String commentQuery);
    List<Article> findAll();
}

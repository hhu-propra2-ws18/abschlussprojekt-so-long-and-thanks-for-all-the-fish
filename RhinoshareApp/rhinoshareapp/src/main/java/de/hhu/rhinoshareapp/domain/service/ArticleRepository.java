package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	Article findById(long id);
	List<Article> findByPersonID(long id);
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String nameQuery, String commentQuery);
}

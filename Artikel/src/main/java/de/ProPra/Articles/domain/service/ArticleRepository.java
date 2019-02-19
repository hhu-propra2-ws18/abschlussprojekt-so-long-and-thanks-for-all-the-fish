package de.ProPra.Articles.domain.service;

import de.ProPra.Articles.domain.model.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

	List<Article> findByPersonID(long id);
}

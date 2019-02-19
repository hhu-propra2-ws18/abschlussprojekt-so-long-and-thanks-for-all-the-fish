package de.hhu.ProPra.conflict.service.Repositorys;

import de.hhu.ProPra.conflict.model.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}

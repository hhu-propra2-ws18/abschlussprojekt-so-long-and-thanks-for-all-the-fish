package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends CrudRepository<Lending, Long> {
    //Iterable<Lending> findAll();
    List<Lending> findAllBylendingPersonAndIsAcceptedAndIsReturn(User user, boolean accepted, boolean isReturn);
    Optional<Lending> findLendingBylendedArticle(Article article);
    Optional<Lending> findLendingBylendedArticleAndIsReturn(Article article, boolean isReturn);
    Optional<Lending> findLendingBylendingID(long id);
}

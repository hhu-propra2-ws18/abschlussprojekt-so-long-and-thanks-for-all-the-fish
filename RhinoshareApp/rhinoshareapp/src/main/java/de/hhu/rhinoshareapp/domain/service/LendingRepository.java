package de.hhu.rhinoshareapp.domain.service;


import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends CrudRepository<Lending, Long> {

    List<Lending> findAllBylendingPersonAndIsAcceptedAndIsReturnAndIsConflictAndIsRequestedForSale(Person person, boolean accepted, boolean isReturn, boolean isConflict, boolean isRequestedForSale);
    Optional<Lending> findLendingBylendedArticle(Article article);
    Optional<Lending> findLendingBylendedArticleAndIsReturn(Article article, boolean isReturn);
    Optional<Lending> findLendingBylendingID(long id);
    List<Lending> findAllLendingBylendingPersonAndIsDummy(Person person, boolean isDummy);
    List<Lending> findAllByIsConflict(boolean conflict);
    List<Lending> findAll();
    Optional<Lending> findBylendedArticleAndIsRequestedForSale(Article article, boolean isRequestedForSale);
}

package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Currency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("select currency from Currency currency where currency.userCreation.login = ?#{principal.username}")
    List<Currency> findByUserCreationIsCurrentUser();

    @Query("select currency from Currency currency where currency.userModification.login = ?#{principal.username}")
    List<Currency> findByUserModificationIsCurrentUser();

}

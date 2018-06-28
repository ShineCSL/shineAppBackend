package com.shine.shineappback.repository;

import com.shine.shineappback.domain.AccountDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AccountDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

    @Query("select account_details from AccountDetails account_details where account_details.userCreation.login = ?#{principal.username}")
    List<AccountDetails> findByUserCreationIsCurrentUser();

    @Query("select account_details from AccountDetails account_details where account_details.userModfication.login = ?#{principal.username}")
    List<AccountDetails> findByUserModficationIsCurrentUser();

}

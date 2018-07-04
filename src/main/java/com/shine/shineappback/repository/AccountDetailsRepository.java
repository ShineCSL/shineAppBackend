package com.shine.shineappback.repository;

import com.shine.shineappback.domain.AccountDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AccountDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

}

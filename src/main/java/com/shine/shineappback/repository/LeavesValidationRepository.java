package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeavesValidation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LeavesValidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesValidationRepository extends JpaRepository<LeavesValidation, Long> {

    @Query("select leaves_validation from LeavesValidation leaves_validation where leaves_validation.user.login = ?#{principal.username}")
    List<LeavesValidation> findByUserIsCurrentUser();

}

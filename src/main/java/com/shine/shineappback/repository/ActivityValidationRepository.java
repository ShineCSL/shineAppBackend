package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivityValidation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ActivityValidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityValidationRepository extends JpaRepository<ActivityValidation, Long> {

    @Query("select activity_validation from ActivityValidation activity_validation where activity_validation.user.login = ?#{principal.username}")
    List<ActivityValidation> findByUserIsCurrentUser();

}

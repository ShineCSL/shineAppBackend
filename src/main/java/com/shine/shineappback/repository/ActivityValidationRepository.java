package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivityValidation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActivityValidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityValidationRepository extends JpaRepository<ActivityValidation, Long> {

}

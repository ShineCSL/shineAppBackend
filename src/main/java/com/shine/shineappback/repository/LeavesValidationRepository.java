package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeavesValidation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LeavesValidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesValidationRepository extends JpaRepository<LeavesValidation, Long> {

}

package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeavesRejection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LeavesRejection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesRejectionRepository extends JpaRepository<LeavesRejection, Long> {

}

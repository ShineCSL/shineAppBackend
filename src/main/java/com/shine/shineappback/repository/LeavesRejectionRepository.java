package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeavesRejection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LeavesRejection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesRejectionRepository extends JpaRepository<LeavesRejection, Long> {

    @Query("select leaves_rejection from LeavesRejection leaves_rejection where leaves_rejection.user.login = ?#{principal.username}")
    List<LeavesRejection> findByUserIsCurrentUser();

}

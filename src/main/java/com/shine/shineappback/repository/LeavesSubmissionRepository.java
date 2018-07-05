package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeavesSubmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LeavesSubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesSubmissionRepository extends JpaRepository<LeavesSubmission, Long> {

    @Query("select leaves_submission from LeavesSubmission leaves_submission where leaves_submission.user.login = ?#{principal.username}")
    List<LeavesSubmission> findByUserIsCurrentUser();

}

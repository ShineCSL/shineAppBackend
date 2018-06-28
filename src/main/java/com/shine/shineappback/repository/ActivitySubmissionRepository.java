package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivitySubmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ActivitySubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivitySubmissionRepository extends JpaRepository<ActivitySubmission, Long> {

    @Query("select activity_submission from ActivitySubmission activity_submission where activity_submission.userModification.login = ?#{principal.username}")
    List<ActivitySubmission> findByUserModificationIsCurrentUser();

    @Query("select activity_submission from ActivitySubmission activity_submission where activity_submission.userCreation.login = ?#{principal.username}")
    List<ActivitySubmission> findByUserCreationIsCurrentUser();

}

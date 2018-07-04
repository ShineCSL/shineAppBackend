package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivitySubmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActivitySubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivitySubmissionRepository extends JpaRepository<ActivitySubmission, Long> {

}

package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeavesSubmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LeavesSubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesSubmissionRepository extends JpaRepository<LeavesSubmission, Long> {

}

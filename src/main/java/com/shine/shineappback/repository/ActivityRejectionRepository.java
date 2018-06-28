package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivityRejection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ActivityRejection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRejectionRepository extends JpaRepository<ActivityRejection, Long> {

    @Query("select activity_rejection from ActivityRejection activity_rejection where activity_rejection.userCreation.login = ?#{principal.username}")
    List<ActivityRejection> findByUserCreationIsCurrentUser();

    @Query("select activity_rejection from ActivityRejection activity_rejection where activity_rejection.userModification.login = ?#{principal.username}")
    List<ActivityRejection> findByUserModificationIsCurrentUser();

}

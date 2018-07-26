package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivityConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ActivityConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityConfigRepository extends JpaRepository<ActivityConfig, Long> {

    @Query("select activity_config from ActivityConfig activity_config where activity_config.user.login = ?#{principal.username}")
    List<ActivityConfig> findByUserIsCurrentUser();

    @Query("select activity_config from ActivityConfig activity_config where activity_config.approver.login = ?#{principal.username}")
    List<ActivityConfig> findByApproverIsCurrentUser();

}

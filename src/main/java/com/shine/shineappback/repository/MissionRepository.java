package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Mission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Mission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Query("select mission from Mission mission where mission.userCreation.login = ?#{principal.username}")
    List<Mission> findByUserCreationIsCurrentUser();

    @Query("select mission from Mission mission where mission.userModification.login = ?#{principal.username}")
    List<Mission> findByUserModificationIsCurrentUser();

}

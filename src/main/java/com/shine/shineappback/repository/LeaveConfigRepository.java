package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeaveConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LeaveConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveConfigRepository extends JpaRepository<LeaveConfig, Long> {

    @Query("select leave_config from LeaveConfig leave_config where leave_config.user.login = ?#{principal.username}")
    List<LeaveConfig> findByUserIsCurrentUser();

}

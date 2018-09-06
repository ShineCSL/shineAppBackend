package com.shine.shineappback.repository;

import com.shine.shineappback.domain.LeaveConfig;
import com.shine.shineappback.domain.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the LeaveConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveConfigRepository extends JpaRepository<LeaveConfig, Long>, JpaSpecificationExecutor<LeaveConfig> {

    @Query("select leave_config from LeaveConfig leave_config where leave_config.user.login = ?#{principal.username}")
    List<LeaveConfig> findByUserIsCurrentUser();

    @Query("select leave_config from LeaveConfig leave_config where leave_config.approver.login = ?#{principal.username}")
    List<LeaveConfig> findByApproverIsCurrentUser();
    
    Optional<LeaveConfig> findOneByUserLogin(String login);
}

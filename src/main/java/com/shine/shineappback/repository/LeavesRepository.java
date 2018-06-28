package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Leaves;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Leaves entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesRepository extends JpaRepository<Leaves, Long> {

    @Query("select leaves from Leaves leaves where leaves.user.login = ?#{principal.username}")
    List<Leaves> findByUserIsCurrentUser();

}

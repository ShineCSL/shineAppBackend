package com.shine.shineappback.repository;

import com.shine.shineappback.domain.ActivityRejection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActivityRejection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRejectionRepository extends JpaRepository<ActivityRejection, Long> {

}

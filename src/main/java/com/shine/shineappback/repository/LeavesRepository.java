package com.shine.shineappback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shine.shineappback.domain.Leaves;

/**
 * Spring Data  repository for the Leaves entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeavesRepository extends JpaRepository<Leaves, Long>, JpaSpecificationExecutor<Leaves> {

    @Query("select leaves from Leaves leaves where leaves.user.login = ?#{principal.username}")
    List<Leaves> findByUserIsCurrentUser();

    
    @Query(value = "SELECT SUM(nbOfHours) FROM Leaves leaves WHERE leaves.year = :year " +
            "AND leaves.user.login = :userLogin " +
            "AND leaves.task.code = :taskCode" +
			"AND leaves.leavesValidation.validated = 1")
    Integer sumHoursByUserYearAndTask(@Param("userLogin") String userLogin,
									  @Param("year") int year,
                                      @Param("taskCode") String taskCode);
}

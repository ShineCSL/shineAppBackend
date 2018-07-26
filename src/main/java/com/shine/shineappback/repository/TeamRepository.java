package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    @Query("select team from Team team where team.supervisor.login = ?#{principal.username}")
    List<Team> findBySupervisorIsCurrentUser();

    @Query(value = "select distinct team from Team team left join fetch team.resources",
        countQuery = "select count(distinct team) from Team team")
    Page<Team> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct team from Team team left join fetch team.resources")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.resources where team.id =:id")
    Optional<Team> findOneWithEagerRelationships(@Param("id") Long id);

}

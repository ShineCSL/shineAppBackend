package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Task;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select task from Task task where task.userCreation.login = ?#{principal.username}")
    List<Task> findByUserCreationIsCurrentUser();

    @Query("select task from Task task where task.userModification.login = ?#{principal.username}")
    List<Task> findByUserModificationIsCurrentUser();

}

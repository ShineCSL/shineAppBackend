package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.TaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Task and its DTO TaskDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    TaskDTO toDto(Task task);

    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
    Task toEntity(TaskDTO taskDTO);

    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}

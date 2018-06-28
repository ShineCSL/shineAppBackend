package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Leaves and its DTO LeavesDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, LeavesSubmissionMapper.class, LeavesValidationMapper.class, LeavesRejectionMapper.class, TaskMapper.class})
public interface LeavesMapper extends EntityMapper<LeavesDTO, Leaves> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "leavesSubmission.id", target = "leavesSubmissionId")
    @Mapping(source = "leavesValidation.id", target = "leavesValidationId")
    @Mapping(source = "leavesRejection.id", target = "leavesRejectionId")
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.code", target = "taskCode")
    LeavesDTO toDto(Leaves leaves);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "leavesSubmissionId", target = "leavesSubmission")
    @Mapping(source = "leavesValidationId", target = "leavesValidation")
    @Mapping(source = "leavesRejectionId", target = "leavesRejection")
    @Mapping(source = "taskId", target = "task")
    Leaves toEntity(LeavesDTO leavesDTO);

    default Leaves fromId(Long id) {
        if (id == null) {
            return null;
        }
        Leaves leaves = new Leaves();
        leaves.setId(id);
        return leaves;
    }
}

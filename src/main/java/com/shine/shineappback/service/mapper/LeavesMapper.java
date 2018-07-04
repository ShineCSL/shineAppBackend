package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Leaves and its DTO LeavesDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskMapper.class, LeavesSubmissionMapper.class, LeavesValidationMapper.class, LeavesRejectionMapper.class})
public interface LeavesMapper extends EntityMapper<LeavesDTO, Leaves> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.code", target = "taskCode")
    @Mapping(source = "leavesSubmission.id", target = "leavesSubmissionId")
    @Mapping(source = "leavesSubmission.submitted", target = "leavesSubmissionSubmitted")
    @Mapping(source = "leavesValidation.id", target = "leavesValidationId")
    @Mapping(source = "leavesValidation.validated", target = "leavesValidationValidated")
    @Mapping(source = "leavesRejection.id", target = "leavesRejectionId")
    @Mapping(source = "leavesRejection.rejected", target = "leavesRejectionRejected")
    LeavesDTO toDto(Leaves leaves);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "leavesSubmissionId", target = "leavesSubmission")
    @Mapping(source = "leavesValidationId", target = "leavesValidation")
    @Mapping(source = "leavesRejectionId", target = "leavesRejection")
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

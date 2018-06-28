package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesSubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeavesSubmission and its DTO LeavesSubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeavesSubmissionMapper extends EntityMapper<LeavesSubmissionDTO, LeavesSubmission> {

    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    LeavesSubmissionDTO toDto(LeavesSubmission leavesSubmission);

    @Mapping(source = "userModificationId", target = "userModification")
    @Mapping(target = "leaves", ignore = true)
    @Mapping(source = "userCreationId", target = "userCreation")
    LeavesSubmission toEntity(LeavesSubmissionDTO leavesSubmissionDTO);

    default LeavesSubmission fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeavesSubmission leavesSubmission = new LeavesSubmission();
        leavesSubmission.setId(id);
        return leavesSubmission;
    }
}

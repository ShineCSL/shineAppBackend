package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesSubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeavesSubmission and its DTO LeavesSubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeavesSubmissionMapper extends EntityMapper<LeavesSubmissionDTO, LeavesSubmission> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    LeavesSubmissionDTO toDto(LeavesSubmission leavesSubmission);

    @Mapping(target = "leaves", ignore = true)
    @Mapping(source = "userId", target = "user")
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

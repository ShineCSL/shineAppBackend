package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivitySubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivitySubmission and its DTO ActivitySubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivitySubmissionMapper extends EntityMapper<ActivitySubmissionDTO, ActivitySubmission> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ActivitySubmissionDTO toDto(ActivitySubmission activitySubmission);

    @Mapping(source = "userId", target = "user")
    ActivitySubmission toEntity(ActivitySubmissionDTO activitySubmissionDTO);

    default ActivitySubmission fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivitySubmission activitySubmission = new ActivitySubmission();
        activitySubmission.setId(id);
        return activitySubmission;
    }
}

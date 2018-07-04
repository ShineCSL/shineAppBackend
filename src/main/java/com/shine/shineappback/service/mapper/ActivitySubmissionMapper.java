package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivitySubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivitySubmission and its DTO ActivitySubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActivitySubmissionMapper extends EntityMapper<ActivitySubmissionDTO, ActivitySubmission> {



    default ActivitySubmission fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivitySubmission activitySubmission = new ActivitySubmission();
        activitySubmission.setId(id);
        return activitySubmission;
    }
}

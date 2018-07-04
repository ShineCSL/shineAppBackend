package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Activity and its DTO ActivityDTO.
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class, UserMapper.class, MissionMapper.class, ActivityRejectionMapper.class, ActivitySubmissionMapper.class, ActivityValidationMapper.class})
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.code", target = "taskCode")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "mission.id", target = "missionId")
    @Mapping(source = "mission.code", target = "missionCode")
    @Mapping(source = "activityRejection.id", target = "activityRejectionId")
    @Mapping(source = "activityRejection.rejected", target = "activityRejectionRejected")
    @Mapping(source = "activitySubmission.id", target = "activitySubmissionId")
    @Mapping(source = "activitySubmission.submitted", target = "activitySubmissionSubmitted")
    @Mapping(source = "activityValidation.id", target = "activityValidationId")
    @Mapping(source = "activityValidation.validated", target = "activityValidationValidated")
    ActivityDTO toDto(Activity activity);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "missionId", target = "mission")
    @Mapping(source = "activityRejectionId", target = "activityRejection")
    @Mapping(source = "activitySubmissionId", target = "activitySubmission")
    @Mapping(source = "activityValidationId", target = "activityValidation")
    Activity toEntity(ActivityDTO activityDTO);

    default Activity fromId(Long id) {
        if (id == null) {
            return null;
        }
        Activity activity = new Activity();
        activity.setId(id);
        return activity;
    }
}

package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Activity and its DTO ActivityDTO.
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class, UserMapper.class, ClientMapper.class, ActivitySubmissionMapper.class, ActivityValidationMapper.class, ActivityRejectionMapper.class})
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.code", target = "taskCode")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.code", target = "clientCode")
    @Mapping(source = "activitySubmission.id", target = "activitySubmissionId")
    @Mapping(source = "activityValidation.id", target = "activityValidationId")
    @Mapping(source = "activityRejection.id", target = "activityRejectionId")
    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    ActivityDTO toDto(Activity activity);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "activitySubmissionId", target = "activitySubmission")
    @Mapping(source = "activityValidationId", target = "activityValidation")
    @Mapping(source = "activityRejectionId", target = "activityRejection")
    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
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
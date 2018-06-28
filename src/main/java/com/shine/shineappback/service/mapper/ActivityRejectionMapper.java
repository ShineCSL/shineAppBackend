package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityRejectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityRejection and its DTO ActivityRejectionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityRejectionMapper extends EntityMapper<ActivityRejectionDTO, ActivityRejection> {

    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    ActivityRejectionDTO toDto(ActivityRejection activityRejection);

    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
    ActivityRejection toEntity(ActivityRejectionDTO activityRejectionDTO);

    default ActivityRejection fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityRejection activityRejection = new ActivityRejection();
        activityRejection.setId(id);
        return activityRejection;
    }
}

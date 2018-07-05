package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityRejectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityRejection and its DTO ActivityRejectionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityRejectionMapper extends EntityMapper<ActivityRejectionDTO, ActivityRejection> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ActivityRejectionDTO toDto(ActivityRejection activityRejection);

    @Mapping(source = "userId", target = "user")
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

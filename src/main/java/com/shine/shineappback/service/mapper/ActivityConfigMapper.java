package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityConfig and its DTO ActivityConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityConfigMapper extends EntityMapper<ActivityConfigDTO, ActivityConfig> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "approver.id", target = "approverId")
    @Mapping(source = "approver.login", target = "approverLogin")
    ActivityConfigDTO toDto(ActivityConfig activityConfig);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "approverId", target = "approver")
    ActivityConfig toEntity(ActivityConfigDTO activityConfigDTO);

    default ActivityConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityConfig activityConfig = new ActivityConfig();
        activityConfig.setId(id);
        return activityConfig;
    }
}

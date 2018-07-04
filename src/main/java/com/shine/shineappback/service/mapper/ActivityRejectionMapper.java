package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityRejectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityRejection and its DTO ActivityRejectionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActivityRejectionMapper extends EntityMapper<ActivityRejectionDTO, ActivityRejection> {



    default ActivityRejection fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityRejection activityRejection = new ActivityRejection();
        activityRejection.setId(id);
        return activityRejection;
    }
}

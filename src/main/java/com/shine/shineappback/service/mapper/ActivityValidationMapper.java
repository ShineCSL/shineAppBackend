package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityValidation and its DTO ActivityValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActivityValidationMapper extends EntityMapper<ActivityValidationDTO, ActivityValidation> {



    default ActivityValidation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityValidation activityValidation = new ActivityValidation();
        activityValidation.setId(id);
        return activityValidation;
    }
}

package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityValidation and its DTO ActivityValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityValidationMapper extends EntityMapper<ActivityValidationDTO, ActivityValidation> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ActivityValidationDTO toDto(ActivityValidation activityValidation);

    @Mapping(source = "userId", target = "user")
    ActivityValidation toEntity(ActivityValidationDTO activityValidationDTO);

    default ActivityValidation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityValidation activityValidation = new ActivityValidation();
        activityValidation.setId(id);
        return activityValidation;
    }
}

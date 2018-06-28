package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.ActivityValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActivityValidation and its DTO ActivityValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityValidationMapper extends EntityMapper<ActivityValidationDTO, ActivityValidation> {

    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    ActivityValidationDTO toDto(ActivityValidation activityValidation);

    @Mapping(source = "userModificationId", target = "userModification")
    @Mapping(source = "userCreationId", target = "userCreation")
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

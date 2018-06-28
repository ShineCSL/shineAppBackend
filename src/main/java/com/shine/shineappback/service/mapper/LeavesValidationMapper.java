package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeavesValidation and its DTO LeavesValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeavesValidationMapper extends EntityMapper<LeavesValidationDTO, LeavesValidation> {

    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    LeavesValidationDTO toDto(LeavesValidation leavesValidation);

    @Mapping(target = "leaves", ignore = true)
    @Mapping(source = "userModificationId", target = "userModification")
    @Mapping(source = "userCreationId", target = "userCreation")
    LeavesValidation toEntity(LeavesValidationDTO leavesValidationDTO);

    default LeavesValidation fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeavesValidation leavesValidation = new LeavesValidation();
        leavesValidation.setId(id);
        return leavesValidation;
    }
}

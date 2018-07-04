package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeavesValidation and its DTO LeavesValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeavesValidationMapper extends EntityMapper<LeavesValidationDTO, LeavesValidation> {


    @Mapping(target = "leaves", ignore = true)
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

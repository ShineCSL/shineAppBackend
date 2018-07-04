package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesRejectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeavesRejection and its DTO LeavesRejectionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeavesRejectionMapper extends EntityMapper<LeavesRejectionDTO, LeavesRejection> {


    @Mapping(target = "leaves", ignore = true)
    LeavesRejection toEntity(LeavesRejectionDTO leavesRejectionDTO);

    default LeavesRejection fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeavesRejection leavesRejection = new LeavesRejection();
        leavesRejection.setId(id);
        return leavesRejection;
    }
}

package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeavesRejectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeavesRejection and its DTO LeavesRejectionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeavesRejectionMapper extends EntityMapper<LeavesRejectionDTO, LeavesRejection> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    LeavesRejectionDTO toDto(LeavesRejection leavesRejection);

    @Mapping(target = "leaves", ignore = true)
    @Mapping(source = "userId", target = "user")
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

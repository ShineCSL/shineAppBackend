package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.LeaveConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeaveConfig and its DTO LeaveConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LeaveConfigMapper extends EntityMapper<LeaveConfigDTO, LeaveConfig> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    LeaveConfigDTO toDto(LeaveConfig leaveConfig);

    @Mapping(source = "userId", target = "user")
    LeaveConfig toEntity(LeaveConfigDTO leaveConfigDTO);

    default LeaveConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveConfig leaveConfig = new LeaveConfig();
        leaveConfig.setId(id);
        return leaveConfig;
    }
}

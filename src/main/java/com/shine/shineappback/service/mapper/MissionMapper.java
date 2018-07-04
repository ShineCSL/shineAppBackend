package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.MissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Mission and its DTO MissionDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface MissionMapper extends EntityMapper<MissionDTO, Mission> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.code", target = "clientCode")
    MissionDTO toDto(Mission mission);

    @Mapping(source = "clientId", target = "client")
    Mission toEntity(MissionDTO missionDTO);

    default Mission fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mission mission = new Mission();
        mission.setId(id);
        return mission;
    }
}

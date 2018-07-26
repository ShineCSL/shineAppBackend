package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.PublicHolidayDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PublicHoliday and its DTO PublicHolidayDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PublicHolidayMapper extends EntityMapper<PublicHolidayDTO, PublicHoliday> {



    default PublicHoliday fromId(Long id) {
        if (id == null) {
            return null;
        }
        PublicHoliday publicHoliday = new PublicHoliday();
        publicHoliday.setId(id);
        return publicHoliday;
    }
}

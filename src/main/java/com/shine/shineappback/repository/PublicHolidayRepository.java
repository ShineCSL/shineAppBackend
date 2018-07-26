package com.shine.shineappback.repository;

import com.shine.shineappback.domain.PublicHoliday;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PublicHoliday entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long>, JpaSpecificationExecutor<PublicHoliday> {

}

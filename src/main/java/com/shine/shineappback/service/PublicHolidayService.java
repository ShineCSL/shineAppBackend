package com.shine.shineappback.service;

import com.shine.shineappback.domain.PublicHoliday;
import com.shine.shineappback.repository.PublicHolidayRepository;
import com.shine.shineappback.service.dto.PublicHolidayDTO;
import com.shine.shineappback.service.mapper.PublicHolidayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing PublicHoliday.
 */
@Service
@Transactional
public class PublicHolidayService {

    private final Logger log = LoggerFactory.getLogger(PublicHolidayService.class);

    private final PublicHolidayRepository publicHolidayRepository;

    private final PublicHolidayMapper publicHolidayMapper;

    public PublicHolidayService(PublicHolidayRepository publicHolidayRepository, PublicHolidayMapper publicHolidayMapper) {
        this.publicHolidayRepository = publicHolidayRepository;
        this.publicHolidayMapper = publicHolidayMapper;
    }

    /**
     * Save a publicHoliday.
     *
     * @param publicHolidayDTO the entity to save
     * @return the persisted entity
     */
    public PublicHolidayDTO save(PublicHolidayDTO publicHolidayDTO) {
        log.debug("Request to save PublicHoliday : {}", publicHolidayDTO);
        PublicHoliday publicHoliday = publicHolidayMapper.toEntity(publicHolidayDTO);
        publicHoliday = publicHolidayRepository.save(publicHoliday);
        return publicHolidayMapper.toDto(publicHoliday);
    }

    /**
     * Get all the publicHolidays.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PublicHolidayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PublicHolidays");
        return publicHolidayRepository.findAll(pageable)
            .map(publicHolidayMapper::toDto);
    }


    /**
     * Get one publicHoliday by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<PublicHolidayDTO> findOne(Long id) {
        log.debug("Request to get PublicHoliday : {}", id);
        return publicHolidayRepository.findById(id)
            .map(publicHolidayMapper::toDto);
    }

    /**
     * Delete the publicHoliday by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PublicHoliday : {}", id);
        publicHolidayRepository.deleteById(id);
    }
}

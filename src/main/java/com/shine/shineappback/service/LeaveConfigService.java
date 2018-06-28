package com.shine.shineappback.service;

import com.shine.shineappback.domain.LeaveConfig;
import com.shine.shineappback.repository.LeaveConfigRepository;
import com.shine.shineappback.service.dto.LeaveConfigDTO;
import com.shine.shineappback.service.mapper.LeaveConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing LeaveConfig.
 */
@Service
@Transactional
public class LeaveConfigService {

    private final Logger log = LoggerFactory.getLogger(LeaveConfigService.class);

    private final LeaveConfigRepository leaveConfigRepository;

    private final LeaveConfigMapper leaveConfigMapper;

    public LeaveConfigService(LeaveConfigRepository leaveConfigRepository, LeaveConfigMapper leaveConfigMapper) {
        this.leaveConfigRepository = leaveConfigRepository;
        this.leaveConfigMapper = leaveConfigMapper;
    }

    /**
     * Save a leaveConfig.
     *
     * @param leaveConfigDTO the entity to save
     * @return the persisted entity
     */
    public LeaveConfigDTO save(LeaveConfigDTO leaveConfigDTO) {
        log.debug("Request to save LeaveConfig : {}", leaveConfigDTO);
        LeaveConfig leaveConfig = leaveConfigMapper.toEntity(leaveConfigDTO);
        leaveConfig = leaveConfigRepository.save(leaveConfig);
        return leaveConfigMapper.toDto(leaveConfig);
    }

    /**
     * Get all the leaveConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LeaveConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveConfigs");
        return leaveConfigRepository.findAll(pageable)
            .map(leaveConfigMapper::toDto);
    }


    /**
     * Get one leaveConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LeaveConfigDTO> findOne(Long id) {
        log.debug("Request to get LeaveConfig : {}", id);
        return leaveConfigRepository.findById(id)
            .map(leaveConfigMapper::toDto);
    }

    /**
     * Delete the leaveConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveConfig : {}", id);
        leaveConfigRepository.deleteById(id);
    }
}

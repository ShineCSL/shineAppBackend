package com.shine.shineappback.service;

import com.shine.shineappback.domain.ActivityConfig;
import com.shine.shineappback.repository.ActivityConfigRepository;
import com.shine.shineappback.service.dto.ActivityConfigDTO;
import com.shine.shineappback.service.mapper.ActivityConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ActivityConfig.
 */
@Service
@Transactional
public class ActivityConfigService {

    private final Logger log = LoggerFactory.getLogger(ActivityConfigService.class);

    private final ActivityConfigRepository activityConfigRepository;

    private final ActivityConfigMapper activityConfigMapper;

    public ActivityConfigService(ActivityConfigRepository activityConfigRepository, ActivityConfigMapper activityConfigMapper) {
        this.activityConfigRepository = activityConfigRepository;
        this.activityConfigMapper = activityConfigMapper;
    }

    /**
     * Save a activityConfig.
     *
     * @param activityConfigDTO the entity to save
     * @return the persisted entity
     */
    public ActivityConfigDTO save(ActivityConfigDTO activityConfigDTO) {
        log.debug("Request to save ActivityConfig : {}", activityConfigDTO);
        ActivityConfig activityConfig = activityConfigMapper.toEntity(activityConfigDTO);
        activityConfig = activityConfigRepository.save(activityConfig);
        return activityConfigMapper.toDto(activityConfig);
    }

    /**
     * Get all the activityConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActivityConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityConfigs");
        return activityConfigRepository.findAll(pageable)
            .map(activityConfigMapper::toDto);
    }


    /**
     * Get one activityConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ActivityConfigDTO> findOne(Long id) {
        log.debug("Request to get ActivityConfig : {}", id);
        return activityConfigRepository.findById(id)
            .map(activityConfigMapper::toDto);
    }

    /**
     * Delete the activityConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivityConfig : {}", id);
        activityConfigRepository.deleteById(id);
    }
}

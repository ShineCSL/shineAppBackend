package com.shine.shineappback.service;

import com.shine.shineappback.domain.ActivityRejection;
import com.shine.shineappback.repository.ActivityRejectionRepository;
import com.shine.shineappback.service.dto.ActivityRejectionDTO;
import com.shine.shineappback.service.mapper.ActivityRejectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ActivityRejection.
 */
@Service
@Transactional
public class ActivityRejectionService {

    private final Logger log = LoggerFactory.getLogger(ActivityRejectionService.class);

    private final ActivityRejectionRepository activityRejectionRepository;

    private final ActivityRejectionMapper activityRejectionMapper;

    public ActivityRejectionService(ActivityRejectionRepository activityRejectionRepository, ActivityRejectionMapper activityRejectionMapper) {
        this.activityRejectionRepository = activityRejectionRepository;
        this.activityRejectionMapper = activityRejectionMapper;
    }

    /**
     * Save a activityRejection.
     *
     * @param activityRejectionDTO the entity to save
     * @return the persisted entity
     */
    public ActivityRejectionDTO save(ActivityRejectionDTO activityRejectionDTO) {
        log.debug("Request to save ActivityRejection : {}", activityRejectionDTO);
        ActivityRejection activityRejection = activityRejectionMapper.toEntity(activityRejectionDTO);
        activityRejection = activityRejectionRepository.save(activityRejection);
        return activityRejectionMapper.toDto(activityRejection);
    }

    /**
     * Get all the activityRejections.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActivityRejectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityRejections");
        return activityRejectionRepository.findAll(pageable)
            .map(activityRejectionMapper::toDto);
    }


    /**
     * Get one activityRejection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ActivityRejectionDTO> findOne(Long id) {
        log.debug("Request to get ActivityRejection : {}", id);
        return activityRejectionRepository.findById(id)
            .map(activityRejectionMapper::toDto);
    }

    /**
     * Delete the activityRejection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivityRejection : {}", id);
        activityRejectionRepository.deleteById(id);
    }
}

package com.shine.shineappback.service;

import com.shine.shineappback.domain.ActivityValidation;
import com.shine.shineappback.repository.ActivityValidationRepository;
import com.shine.shineappback.service.dto.ActivityValidationDTO;
import com.shine.shineappback.service.mapper.ActivityValidationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ActivityValidation.
 */
@Service
@Transactional
public class ActivityValidationService {

    private final Logger log = LoggerFactory.getLogger(ActivityValidationService.class);

    private final ActivityValidationRepository activityValidationRepository;

    private final ActivityValidationMapper activityValidationMapper;

    public ActivityValidationService(ActivityValidationRepository activityValidationRepository, ActivityValidationMapper activityValidationMapper) {
        this.activityValidationRepository = activityValidationRepository;
        this.activityValidationMapper = activityValidationMapper;
    }

    /**
     * Save a activityValidation.
     *
     * @param activityValidationDTO the entity to save
     * @return the persisted entity
     */
    public ActivityValidationDTO save(ActivityValidationDTO activityValidationDTO) {
        log.debug("Request to save ActivityValidation : {}", activityValidationDTO);
        ActivityValidation activityValidation = activityValidationMapper.toEntity(activityValidationDTO);
        activityValidation = activityValidationRepository.save(activityValidation);
        return activityValidationMapper.toDto(activityValidation);
    }

    /**
     * Get all the activityValidations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActivityValidationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityValidations");
        return activityValidationRepository.findAll(pageable)
            .map(activityValidationMapper::toDto);
    }


    /**
     * Get one activityValidation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ActivityValidationDTO> findOne(Long id) {
        log.debug("Request to get ActivityValidation : {}", id);
        return activityValidationRepository.findById(id)
            .map(activityValidationMapper::toDto);
    }

    /**
     * Delete the activityValidation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivityValidation : {}", id);
        activityValidationRepository.deleteById(id);
    }
}

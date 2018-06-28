package com.shine.shineappback.service;

import com.shine.shineappback.domain.ActivitySubmission;
import com.shine.shineappback.repository.ActivitySubmissionRepository;
import com.shine.shineappback.service.dto.ActivitySubmissionDTO;
import com.shine.shineappback.service.mapper.ActivitySubmissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ActivitySubmission.
 */
@Service
@Transactional
public class ActivitySubmissionService {

    private final Logger log = LoggerFactory.getLogger(ActivitySubmissionService.class);

    private final ActivitySubmissionRepository activitySubmissionRepository;

    private final ActivitySubmissionMapper activitySubmissionMapper;

    public ActivitySubmissionService(ActivitySubmissionRepository activitySubmissionRepository, ActivitySubmissionMapper activitySubmissionMapper) {
        this.activitySubmissionRepository = activitySubmissionRepository;
        this.activitySubmissionMapper = activitySubmissionMapper;
    }

    /**
     * Save a activitySubmission.
     *
     * @param activitySubmissionDTO the entity to save
     * @return the persisted entity
     */
    public ActivitySubmissionDTO save(ActivitySubmissionDTO activitySubmissionDTO) {
        log.debug("Request to save ActivitySubmission : {}", activitySubmissionDTO);
        ActivitySubmission activitySubmission = activitySubmissionMapper.toEntity(activitySubmissionDTO);
        activitySubmission = activitySubmissionRepository.save(activitySubmission);
        return activitySubmissionMapper.toDto(activitySubmission);
    }

    /**
     * Get all the activitySubmissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ActivitySubmissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActivitySubmissions");
        return activitySubmissionRepository.findAll(pageable)
            .map(activitySubmissionMapper::toDto);
    }


    /**
     * Get one activitySubmission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ActivitySubmissionDTO> findOne(Long id) {
        log.debug("Request to get ActivitySubmission : {}", id);
        return activitySubmissionRepository.findById(id)
            .map(activitySubmissionMapper::toDto);
    }

    /**
     * Delete the activitySubmission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ActivitySubmission : {}", id);
        activitySubmissionRepository.deleteById(id);
    }
}

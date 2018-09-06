package com.shine.shineappback.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.shine.shineappback.domain.Activity;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.ActivityRepository;
import com.shine.shineappback.service.dto.ActivityCriteria;

import com.shine.shineappback.service.dto.ActivityDTO;
import com.shine.shineappback.service.mapper.ActivityMapper;

/**
 * Service for executing complex queries for Activity entities in the database.
 * The main input is a {@link ActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActivityDTO} or a {@link Page} of {@link ActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActivityQueryService extends QueryService<Activity> {

    private final Logger log = LoggerFactory.getLogger(ActivityQueryService.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityQueryService(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    /**
     * Return a {@link List} of {@link ActivityDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActivityDTO> findByCriteria(ActivityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Activity> specification = createSpecification(criteria);
        return activityMapper.toDto(activityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActivityDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActivityDTO> findByCriteria(ActivityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Activity> specification = createSpecification(criteria);
        return activityRepository.findAll(specification, page)
            .map(activityMapper::toDto);
    }

    /**
     * Function to convert ActivityCriteria to a {@link Specification}
     */
    private Specification<Activity> createSpecification(ActivityCriteria criteria) {
        Specification<Activity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Activity_.id));
            }
            if (criteria.getActivityDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivityDate(), Activity_.activityDate));
            }
            if (criteria.getNbOfHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbOfHours(), Activity_.nbOfHours));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDay(), Activity_.day));
            }
            if (criteria.getWeekNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekNumber(), Activity_.weekNumber));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), Activity_.year));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), Activity_.month));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTaskId(), Activity_.task, Task_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Activity_.user, User_.id));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), Activity_.client, Client_.id));
            }
            if (criteria.getActivityRejectionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getActivityRejectionId(), Activity_.activityRejection, ActivityRejection_.id));
            }
            if (criteria.getActivitySubmissionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getActivitySubmissionId(), Activity_.activitySubmission, ActivitySubmission_.id));
            }
            if (criteria.getActivityValidationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getActivityValidationId(), Activity_.activityValidation, ActivityValidation_.id));
            }
        }
        return specification;
    }

}

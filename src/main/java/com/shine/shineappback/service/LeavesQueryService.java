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

import com.shine.shineappback.domain.Leaves;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.LeavesRepository;
import com.shine.shineappback.service.dto.LeavesCriteria;

import com.shine.shineappback.service.dto.LeavesDTO;
import com.shine.shineappback.service.mapper.LeavesMapper;

/**
 * Service for executing complex queries for Leaves entities in the database.
 * The main input is a {@link LeavesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeavesDTO} or a {@link Page} of {@link LeavesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeavesQueryService extends QueryService<Leaves> {

    private final Logger log = LoggerFactory.getLogger(LeavesQueryService.class);

    private final LeavesRepository leavesRepository;

    private final LeavesMapper leavesMapper;

    public LeavesQueryService(LeavesRepository leavesRepository, LeavesMapper leavesMapper) {
        this.leavesRepository = leavesRepository;
        this.leavesMapper = leavesMapper;
    }

    /**
     * Return a {@link List} of {@link LeavesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeavesDTO> findByCriteria(LeavesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Leaves> specification = createSpecification(criteria);
        return leavesMapper.toDto(leavesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeavesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeavesDTO> findByCriteria(LeavesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Leaves> specification = createSpecification(criteria);
        return leavesRepository.findAll(specification, page)
            .map(leavesMapper::toDto);
    }

    /**
     * Function to convert LeavesCriteria to a {@link Specification}
     */
    private Specification<Leaves> createSpecification(LeavesCriteria criteria) {
        Specification<Leaves> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Leaves_.id));
            }
            if (criteria.getFullDay() != null) {
                specification = specification.and(buildSpecification(criteria.getFullDay(), Leaves_.fullDay));
            }
            if (criteria.getLeavesFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeavesFrom(), Leaves_.leavesFrom));
            }
            if (criteria.getLeavesTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeavesTo(), Leaves_.leavesTo));
            }
            if (criteria.getNbOfHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbOfHours(), Leaves_.nbOfHours));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), Leaves_.year));
            }
            if (criteria.getWeekNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekNumber(), Leaves_.weekNumber));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Leaves_.comment));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDay(), Leaves_.day));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), Leaves_.month));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Leaves_.user, User_.id));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTaskId(), Leaves_.task, Task_.id));
            }
            if (criteria.getLeavesSubmissionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLeavesSubmissionId(), Leaves_.leavesSubmission, LeavesSubmission_.id));
            }
            if (criteria.getLeavesValidationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLeavesValidationId(), Leaves_.leavesValidation, LeavesValidation_.id));
            }
            if (criteria.getLeavesRejectionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLeavesRejectionId(), Leaves_.leavesRejection, LeavesRejection_.id));
            }
        }
        return specification;
    }

}

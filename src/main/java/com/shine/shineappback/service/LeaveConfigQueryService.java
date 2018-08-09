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

import com.shine.shineappback.domain.LeaveConfig;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.LeaveConfigRepository;
import com.shine.shineappback.service.dto.LeaveConfigCriteria;

import com.shine.shineappback.service.dto.LeaveConfigDTO;
import com.shine.shineappback.service.mapper.LeaveConfigMapper;

/**
 * Service for executing complex queries for LeaveConfig entities in the database.
 * The main input is a {@link LeaveConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveConfigDTO} or a {@link Page} of {@link LeaveConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveConfigQueryService extends QueryService<LeaveConfig> {

    private final Logger log = LoggerFactory.getLogger(LeaveConfigQueryService.class);

    private final LeaveConfigRepository leaveConfigRepository;

    private final LeaveConfigMapper leaveConfigMapper;

    public LeaveConfigQueryService(LeaveConfigRepository leaveConfigRepository, LeaveConfigMapper leaveConfigMapper) {
        this.leaveConfigRepository = leaveConfigRepository;
        this.leaveConfigMapper = leaveConfigMapper;
    }

    /**
     * Return a {@link List} of {@link LeaveConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveConfigDTO> findByCriteria(LeaveConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveConfig> specification = createSpecification(criteria);
        return leaveConfigMapper.toDto(leaveConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveConfigDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveConfigDTO> findByCriteria(LeaveConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveConfig> specification = createSpecification(criteria);
        return leaveConfigRepository.findAll(specification, page)
            .map(leaveConfigMapper::toDto);
    }

    /**
     * Function to convert LeaveConfigCriteria to a {@link Specification}
     */
    private Specification<LeaveConfig> createSpecification(LeaveConfigCriteria criteria) {
        Specification<LeaveConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LeaveConfig_.id));
            }
            if (criteria.getNbSickLeaves() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbSickLeaves(), LeaveConfig_.nbSickLeaves));
            }
            if (criteria.getNbAnnualLeaves() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbAnnualLeaves(), LeaveConfig_.nbAnnualLeaves));
            }
            if (criteria.getNbSpecialLeaves() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbSpecialLeaves(), LeaveConfig_.nbSpecialLeaves));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), LeaveConfig_.user, User_.id));
            }
            if (criteria.getApproverId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getApproverId(), LeaveConfig_.approver, User_.id));
            }
        }
        return specification;
    }

}

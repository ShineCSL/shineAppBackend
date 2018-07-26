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

import com.shine.shineappback.domain.Team;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.TeamRepository;
import com.shine.shineappback.service.dto.TeamCriteria;

import com.shine.shineappback.service.dto.TeamDTO;
import com.shine.shineappback.service.mapper.TeamMapper;

/**
 * Service for executing complex queries for Team entities in the database.
 * The main input is a {@link TeamCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamDTO} or a {@link Page} of {@link TeamDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamQueryService extends QueryService<Team> {

    private final Logger log = LoggerFactory.getLogger(TeamQueryService.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    public TeamQueryService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    /**
     * Return a {@link List} of {@link TeamDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamDTO> findByCriteria(TeamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Team> specification = createSpecification(criteria);
        return teamMapper.toDto(teamRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TeamDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamDTO> findByCriteria(TeamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Team> specification = createSpecification(criteria);
        return teamRepository.findAll(specification, page)
            .map(teamMapper::toDto);
    }

    /**
     * Function to convert TeamCriteria to a {@link Specification}
     */
    private Specification<Team> createSpecification(TeamCriteria criteria) {
        Specification<Team> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Team_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Team_.code));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Team_.label));
            }
            if (criteria.getSupervisorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSupervisorId(), Team_.supervisor, User_.id));
            }
            if (criteria.getResourcesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResourcesId(), Team_.resources, User_.id));
            }
        }
        return specification;
    }

}

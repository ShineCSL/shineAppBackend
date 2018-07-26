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

import com.shine.shineappback.domain.PublicHoliday;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.PublicHolidayRepository;
import com.shine.shineappback.service.dto.PublicHolidayCriteria;

import com.shine.shineappback.service.dto.PublicHolidayDTO;
import com.shine.shineappback.service.mapper.PublicHolidayMapper;

/**
 * Service for executing complex queries for PublicHoliday entities in the database.
 * The main input is a {@link PublicHolidayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PublicHolidayDTO} or a {@link Page} of {@link PublicHolidayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PublicHolidayQueryService extends QueryService<PublicHoliday> {

    private final Logger log = LoggerFactory.getLogger(PublicHolidayQueryService.class);

    private final PublicHolidayRepository publicHolidayRepository;

    private final PublicHolidayMapper publicHolidayMapper;

    public PublicHolidayQueryService(PublicHolidayRepository publicHolidayRepository, PublicHolidayMapper publicHolidayMapper) {
        this.publicHolidayRepository = publicHolidayRepository;
        this.publicHolidayMapper = publicHolidayMapper;
    }

    /**
     * Return a {@link List} of {@link PublicHolidayDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PublicHolidayDTO> findByCriteria(PublicHolidayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PublicHoliday> specification = createSpecification(criteria);
        return publicHolidayMapper.toDto(publicHolidayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PublicHolidayDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PublicHolidayDTO> findByCriteria(PublicHolidayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PublicHoliday> specification = createSpecification(criteria);
        return publicHolidayRepository.findAll(specification, page)
            .map(publicHolidayMapper::toDto);
    }

    /**
     * Function to convert PublicHolidayCriteria to a {@link Specification}
     */
    private Specification<PublicHoliday> createSpecification(PublicHolidayCriteria criteria) {
        Specification<PublicHoliday> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PublicHoliday_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), PublicHoliday_.label));
            }
            if (criteria.getDateHoliday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateHoliday(), PublicHoliday_.dateHoliday));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDay(), PublicHoliday_.day));
            }
            if (criteria.getWeekNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekNumber(), PublicHoliday_.weekNumber));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), PublicHoliday_.month));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), PublicHoliday_.year));
            }
        }
        return specification;
    }

}

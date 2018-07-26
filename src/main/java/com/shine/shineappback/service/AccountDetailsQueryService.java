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

import com.shine.shineappback.domain.AccountDetails;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.AccountDetailsRepository;
import com.shine.shineappback.service.dto.AccountDetailsCriteria;

import com.shine.shineappback.service.dto.AccountDetailsDTO;
import com.shine.shineappback.service.mapper.AccountDetailsMapper;

/**
 * Service for executing complex queries for AccountDetails entities in the database.
 * The main input is a {@link AccountDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountDetailsDTO} or a {@link Page} of {@link AccountDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountDetailsQueryService extends QueryService<AccountDetails> {

    private final Logger log = LoggerFactory.getLogger(AccountDetailsQueryService.class);

    private final AccountDetailsRepository accountDetailsRepository;

    private final AccountDetailsMapper accountDetailsMapper;

    public AccountDetailsQueryService(AccountDetailsRepository accountDetailsRepository, AccountDetailsMapper accountDetailsMapper) {
        this.accountDetailsRepository = accountDetailsRepository;
        this.accountDetailsMapper = accountDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link AccountDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountDetailsDTO> findByCriteria(AccountDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountDetails> specification = createSpecification(criteria);
        return accountDetailsMapper.toDto(accountDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccountDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountDetailsDTO> findByCriteria(AccountDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountDetails> specification = createSpecification(criteria);
        return accountDetailsRepository.findAll(specification, page)
            .map(accountDetailsMapper::toDto);
    }

    /**
     * Function to convert AccountDetailsCriteria to a {@link Specification}
     */
    private Specification<AccountDetails> createSpecification(AccountDetailsCriteria criteria) {
        Specification<AccountDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AccountDetails_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), AccountDetails_.amount));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), AccountDetails_.rate));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), AccountDetails_.label));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AccountDetails_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), AccountDetails_.type));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), AccountDetails_.client, Client_.id));
            }
            if (criteria.getInvoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceId(), AccountDetails_.invoice, Invoice_.id));
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCurrencyId(), AccountDetails_.currency, Currency_.id));
            }
        }
        return specification;
    }

}

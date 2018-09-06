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

import com.shine.shineappback.domain.Invoice;
import com.shine.shineappback.domain.*; // for static metamodels
import com.shine.shineappback.repository.InvoiceRepository;
import com.shine.shineappback.service.dto.InvoiceCriteria;

import com.shine.shineappback.service.dto.InvoiceDTO;
import com.shine.shineappback.service.mapper.InvoiceMapper;

/**
 * Service for executing complex queries for Invoice entities in the database.
 * The main input is a {@link InvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceDTO} or a {@link Page} of {@link InvoiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private final Logger log = LoggerFactory.getLogger(InvoiceQueryService.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public InvoiceQueryService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceDTO> findByCriteria(InvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceMapper.toDto(invoiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification, page)
            .map(invoiceMapper::toDto);
    }

    /**
     * Function to convert InvoiceCriteria to a {@link Specification}
     */
    private Specification<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specification<Invoice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Invoice_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Invoice_.label));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Invoice_.description));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Invoice_.amount));
            }
            if (criteria.getDateInvoice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateInvoice(), Invoice_.dateInvoice));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), Invoice_.rate));
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCurrencyId(), Invoice_.currency, Currency_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Invoice_.user, User_.id));
            }
            if (criteria.getInvoiceRejectionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceRejectionId(), Invoice_.invoiceRejection, InvoiceRejection_.id));
            }
            if (criteria.getInvoiceSubmissionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceSubmissionId(), Invoice_.invoiceSubmission, InvoiceSubmission_.id));
            }
            if (criteria.getInvoiceValidationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceValidationId(), Invoice_.invoiceValidation, InvoiceValidation_.id));
            }
            if (criteria.getTypeInvoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTypeInvoiceId(), Invoice_.typeInvoice, TypeInvoice_.id));
            }
        }
        return specification;
    }

}

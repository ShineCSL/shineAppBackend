package com.shine.shineappback.service;

import com.shine.shineappback.domain.InvoiceValidation;
import com.shine.shineappback.repository.InvoiceValidationRepository;
import com.shine.shineappback.service.dto.InvoiceValidationDTO;
import com.shine.shineappback.service.mapper.InvoiceValidationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * Service Implementation for managing InvoiceValidation.
 */
@Service
@Transactional
public class InvoiceValidationService {

    private final Logger log = LoggerFactory.getLogger(InvoiceValidationService.class);

    private final InvoiceValidationRepository invoiceValidationRepository;

    private final InvoiceValidationMapper invoiceValidationMapper;

    public InvoiceValidationService(InvoiceValidationRepository invoiceValidationRepository, InvoiceValidationMapper invoiceValidationMapper) {
        this.invoiceValidationRepository = invoiceValidationRepository;
        this.invoiceValidationMapper = invoiceValidationMapper;
    }

    /**
     * Save a invoiceValidation.
     *
     * @param invoiceValidationDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceValidationDTO save(InvoiceValidationDTO invoiceValidationDTO) {
        log.debug("Request to save InvoiceValidation : {}", invoiceValidationDTO);
        InvoiceValidation invoiceValidation = invoiceValidationMapper.toEntity(invoiceValidationDTO);
        invoiceValidation = invoiceValidationRepository.save(invoiceValidation);
        return invoiceValidationMapper.toDto(invoiceValidation);
    }

    /**
     * Get all the invoiceValidations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceValidationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceValidations");
        return invoiceValidationRepository.findAll(pageable)
            .map(invoiceValidationMapper::toDto);
    }



    /**
     *  get all the invoiceValidations where Invoice is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<InvoiceValidationDTO> findAllWhereInvoiceIsNull() {
        log.debug("Request to get all invoiceValidations where Invoice is null");
        return StreamSupport
            .stream(invoiceValidationRepository.findAll().spliterator(), false)
            .filter(invoiceValidation -> invoiceValidation.getInvoice() == null)
            .map(invoiceValidationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one invoiceValidation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceValidationDTO> findOne(Long id) {
        log.debug("Request to get InvoiceValidation : {}", id);
        return invoiceValidationRepository.findById(id)
            .map(invoiceValidationMapper::toDto);
    }

    /**
     * Delete the invoiceValidation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceValidation : {}", id);
        invoiceValidationRepository.deleteById(id);
    }
}

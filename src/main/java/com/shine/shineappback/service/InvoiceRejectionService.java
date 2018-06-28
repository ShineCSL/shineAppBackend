package com.shine.shineappback.service;

import com.shine.shineappback.domain.InvoiceRejection;
import com.shine.shineappback.repository.InvoiceRejectionRepository;
import com.shine.shineappback.service.dto.InvoiceRejectionDTO;
import com.shine.shineappback.service.mapper.InvoiceRejectionMapper;
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
 * Service Implementation for managing InvoiceRejection.
 */
@Service
@Transactional
public class InvoiceRejectionService {

    private final Logger log = LoggerFactory.getLogger(InvoiceRejectionService.class);

    private final InvoiceRejectionRepository invoiceRejectionRepository;

    private final InvoiceRejectionMapper invoiceRejectionMapper;

    public InvoiceRejectionService(InvoiceRejectionRepository invoiceRejectionRepository, InvoiceRejectionMapper invoiceRejectionMapper) {
        this.invoiceRejectionRepository = invoiceRejectionRepository;
        this.invoiceRejectionMapper = invoiceRejectionMapper;
    }

    /**
     * Save a invoiceRejection.
     *
     * @param invoiceRejectionDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceRejectionDTO save(InvoiceRejectionDTO invoiceRejectionDTO) {
        log.debug("Request to save InvoiceRejection : {}", invoiceRejectionDTO);
        InvoiceRejection invoiceRejection = invoiceRejectionMapper.toEntity(invoiceRejectionDTO);
        invoiceRejection = invoiceRejectionRepository.save(invoiceRejection);
        return invoiceRejectionMapper.toDto(invoiceRejection);
    }

    /**
     * Get all the invoiceRejections.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceRejectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceRejections");
        return invoiceRejectionRepository.findAll(pageable)
            .map(invoiceRejectionMapper::toDto);
    }



    /**
     *  get all the invoiceRejections where Invoice is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<InvoiceRejectionDTO> findAllWhereInvoiceIsNull() {
        log.debug("Request to get all invoiceRejections where Invoice is null");
        return StreamSupport
            .stream(invoiceRejectionRepository.findAll().spliterator(), false)
            .filter(invoiceRejection -> invoiceRejection.getInvoice() == null)
            .map(invoiceRejectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one invoiceRejection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceRejectionDTO> findOne(Long id) {
        log.debug("Request to get InvoiceRejection : {}", id);
        return invoiceRejectionRepository.findById(id)
            .map(invoiceRejectionMapper::toDto);
    }

    /**
     * Delete the invoiceRejection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceRejection : {}", id);
        invoiceRejectionRepository.deleteById(id);
    }
}

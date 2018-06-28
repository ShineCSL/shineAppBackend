package com.shine.shineappback.service;

import com.shine.shineappback.domain.InvoiceSubmission;
import com.shine.shineappback.repository.InvoiceSubmissionRepository;
import com.shine.shineappback.service.dto.InvoiceSubmissionDTO;
import com.shine.shineappback.service.mapper.InvoiceSubmissionMapper;
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
 * Service Implementation for managing InvoiceSubmission.
 */
@Service
@Transactional
public class InvoiceSubmissionService {

    private final Logger log = LoggerFactory.getLogger(InvoiceSubmissionService.class);

    private final InvoiceSubmissionRepository invoiceSubmissionRepository;

    private final InvoiceSubmissionMapper invoiceSubmissionMapper;

    public InvoiceSubmissionService(InvoiceSubmissionRepository invoiceSubmissionRepository, InvoiceSubmissionMapper invoiceSubmissionMapper) {
        this.invoiceSubmissionRepository = invoiceSubmissionRepository;
        this.invoiceSubmissionMapper = invoiceSubmissionMapper;
    }

    /**
     * Save a invoiceSubmission.
     *
     * @param invoiceSubmissionDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceSubmissionDTO save(InvoiceSubmissionDTO invoiceSubmissionDTO) {
        log.debug("Request to save InvoiceSubmission : {}", invoiceSubmissionDTO);
        InvoiceSubmission invoiceSubmission = invoiceSubmissionMapper.toEntity(invoiceSubmissionDTO);
        invoiceSubmission = invoiceSubmissionRepository.save(invoiceSubmission);
        return invoiceSubmissionMapper.toDto(invoiceSubmission);
    }

    /**
     * Get all the invoiceSubmissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceSubmissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceSubmissions");
        return invoiceSubmissionRepository.findAll(pageable)
            .map(invoiceSubmissionMapper::toDto);
    }



    /**
     *  get all the invoiceSubmissions where Invoice is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<InvoiceSubmissionDTO> findAllWhereInvoiceIsNull() {
        log.debug("Request to get all invoiceSubmissions where Invoice is null");
        return StreamSupport
            .stream(invoiceSubmissionRepository.findAll().spliterator(), false)
            .filter(invoiceSubmission -> invoiceSubmission.getInvoice() == null)
            .map(invoiceSubmissionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one invoiceSubmission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceSubmissionDTO> findOne(Long id) {
        log.debug("Request to get InvoiceSubmission : {}", id);
        return invoiceSubmissionRepository.findById(id)
            .map(invoiceSubmissionMapper::toDto);
    }

    /**
     * Delete the invoiceSubmission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceSubmission : {}", id);
        invoiceSubmissionRepository.deleteById(id);
    }
}

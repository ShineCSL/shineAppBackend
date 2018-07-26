package com.shine.shineappback.service;

import com.shine.shineappback.domain.InvoiceConfig;
import com.shine.shineappback.repository.InvoiceConfigRepository;
import com.shine.shineappback.service.dto.InvoiceConfigDTO;
import com.shine.shineappback.service.mapper.InvoiceConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing InvoiceConfig.
 */
@Service
@Transactional
public class InvoiceConfigService {

    private final Logger log = LoggerFactory.getLogger(InvoiceConfigService.class);

    private final InvoiceConfigRepository invoiceConfigRepository;

    private final InvoiceConfigMapper invoiceConfigMapper;

    public InvoiceConfigService(InvoiceConfigRepository invoiceConfigRepository, InvoiceConfigMapper invoiceConfigMapper) {
        this.invoiceConfigRepository = invoiceConfigRepository;
        this.invoiceConfigMapper = invoiceConfigMapper;
    }

    /**
     * Save a invoiceConfig.
     *
     * @param invoiceConfigDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceConfigDTO save(InvoiceConfigDTO invoiceConfigDTO) {
        log.debug("Request to save InvoiceConfig : {}", invoiceConfigDTO);
        InvoiceConfig invoiceConfig = invoiceConfigMapper.toEntity(invoiceConfigDTO);
        invoiceConfig = invoiceConfigRepository.save(invoiceConfig);
        return invoiceConfigMapper.toDto(invoiceConfig);
    }

    /**
     * Get all the invoiceConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceConfigs");
        return invoiceConfigRepository.findAll(pageable)
            .map(invoiceConfigMapper::toDto);
    }


    /**
     * Get one invoiceConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceConfigDTO> findOne(Long id) {
        log.debug("Request to get InvoiceConfig : {}", id);
        return invoiceConfigRepository.findById(id)
            .map(invoiceConfigMapper::toDto);
    }

    /**
     * Delete the invoiceConfig by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceConfig : {}", id);
        invoiceConfigRepository.deleteById(id);
    }
}

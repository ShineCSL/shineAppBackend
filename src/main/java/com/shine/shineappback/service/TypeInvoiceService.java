package com.shine.shineappback.service;

import com.shine.shineappback.domain.TypeInvoice;
import com.shine.shineappback.repository.TypeInvoiceRepository;
import com.shine.shineappback.service.dto.TypeInvoiceDTO;
import com.shine.shineappback.service.mapper.TypeInvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing TypeInvoice.
 */
@Service
@Transactional
public class TypeInvoiceService {

    private final Logger log = LoggerFactory.getLogger(TypeInvoiceService.class);

    private final TypeInvoiceRepository typeInvoiceRepository;

    private final TypeInvoiceMapper typeInvoiceMapper;

    public TypeInvoiceService(TypeInvoiceRepository typeInvoiceRepository, TypeInvoiceMapper typeInvoiceMapper) {
        this.typeInvoiceRepository = typeInvoiceRepository;
        this.typeInvoiceMapper = typeInvoiceMapper;
    }

    /**
     * Save a typeInvoice.
     *
     * @param typeInvoiceDTO the entity to save
     * @return the persisted entity
     */
    public TypeInvoiceDTO save(TypeInvoiceDTO typeInvoiceDTO) {
        log.debug("Request to save TypeInvoice : {}", typeInvoiceDTO);
        TypeInvoice typeInvoice = typeInvoiceMapper.toEntity(typeInvoiceDTO);
        typeInvoice = typeInvoiceRepository.save(typeInvoice);
        return typeInvoiceMapper.toDto(typeInvoice);
    }

    /**
     * Get all the typeInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeInvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeInvoices");
        return typeInvoiceRepository.findAll(pageable)
            .map(typeInvoiceMapper::toDto);
    }


    /**
     * Get one typeInvoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TypeInvoiceDTO> findOne(Long id) {
        log.debug("Request to get TypeInvoice : {}", id);
        return typeInvoiceRepository.findById(id)
            .map(typeInvoiceMapper::toDto);
    }

    /**
     * Delete the typeInvoice by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeInvoice : {}", id);
        typeInvoiceRepository.deleteById(id);
    }
}

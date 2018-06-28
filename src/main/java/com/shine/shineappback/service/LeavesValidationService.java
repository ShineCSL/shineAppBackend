package com.shine.shineappback.service;

import com.shine.shineappback.domain.LeavesValidation;
import com.shine.shineappback.repository.LeavesValidationRepository;
import com.shine.shineappback.service.dto.LeavesValidationDTO;
import com.shine.shineappback.service.mapper.LeavesValidationMapper;
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
 * Service Implementation for managing LeavesValidation.
 */
@Service
@Transactional
public class LeavesValidationService {

    private final Logger log = LoggerFactory.getLogger(LeavesValidationService.class);

    private final LeavesValidationRepository leavesValidationRepository;

    private final LeavesValidationMapper leavesValidationMapper;

    public LeavesValidationService(LeavesValidationRepository leavesValidationRepository, LeavesValidationMapper leavesValidationMapper) {
        this.leavesValidationRepository = leavesValidationRepository;
        this.leavesValidationMapper = leavesValidationMapper;
    }

    /**
     * Save a leavesValidation.
     *
     * @param leavesValidationDTO the entity to save
     * @return the persisted entity
     */
    public LeavesValidationDTO save(LeavesValidationDTO leavesValidationDTO) {
        log.debug("Request to save LeavesValidation : {}", leavesValidationDTO);
        LeavesValidation leavesValidation = leavesValidationMapper.toEntity(leavesValidationDTO);
        leavesValidation = leavesValidationRepository.save(leavesValidation);
        return leavesValidationMapper.toDto(leavesValidation);
    }

    /**
     * Get all the leavesValidations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LeavesValidationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeavesValidations");
        return leavesValidationRepository.findAll(pageable)
            .map(leavesValidationMapper::toDto);
    }



    /**
     *  get all the leavesValidations where Leaves is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<LeavesValidationDTO> findAllWhereLeavesIsNull() {
        log.debug("Request to get all leavesValidations where Leaves is null");
        return StreamSupport
            .stream(leavesValidationRepository.findAll().spliterator(), false)
            .filter(leavesValidation -> leavesValidation.getLeaves() == null)
            .map(leavesValidationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one leavesValidation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LeavesValidationDTO> findOne(Long id) {
        log.debug("Request to get LeavesValidation : {}", id);
        return leavesValidationRepository.findById(id)
            .map(leavesValidationMapper::toDto);
    }

    /**
     * Delete the leavesValidation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LeavesValidation : {}", id);
        leavesValidationRepository.deleteById(id);
    }
}

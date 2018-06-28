package com.shine.shineappback.service;

import com.shine.shineappback.domain.LeavesRejection;
import com.shine.shineappback.repository.LeavesRejectionRepository;
import com.shine.shineappback.service.dto.LeavesRejectionDTO;
import com.shine.shineappback.service.mapper.LeavesRejectionMapper;
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
 * Service Implementation for managing LeavesRejection.
 */
@Service
@Transactional
public class LeavesRejectionService {

    private final Logger log = LoggerFactory.getLogger(LeavesRejectionService.class);

    private final LeavesRejectionRepository leavesRejectionRepository;

    private final LeavesRejectionMapper leavesRejectionMapper;

    public LeavesRejectionService(LeavesRejectionRepository leavesRejectionRepository, LeavesRejectionMapper leavesRejectionMapper) {
        this.leavesRejectionRepository = leavesRejectionRepository;
        this.leavesRejectionMapper = leavesRejectionMapper;
    }

    /**
     * Save a leavesRejection.
     *
     * @param leavesRejectionDTO the entity to save
     * @return the persisted entity
     */
    public LeavesRejectionDTO save(LeavesRejectionDTO leavesRejectionDTO) {
        log.debug("Request to save LeavesRejection : {}", leavesRejectionDTO);
        LeavesRejection leavesRejection = leavesRejectionMapper.toEntity(leavesRejectionDTO);
        leavesRejection = leavesRejectionRepository.save(leavesRejection);
        return leavesRejectionMapper.toDto(leavesRejection);
    }

    /**
     * Get all the leavesRejections.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LeavesRejectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeavesRejections");
        return leavesRejectionRepository.findAll(pageable)
            .map(leavesRejectionMapper::toDto);
    }



    /**
     *  get all the leavesRejections where Leaves is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<LeavesRejectionDTO> findAllWhereLeavesIsNull() {
        log.debug("Request to get all leavesRejections where Leaves is null");
        return StreamSupport
            .stream(leavesRejectionRepository.findAll().spliterator(), false)
            .filter(leavesRejection -> leavesRejection.getLeaves() == null)
            .map(leavesRejectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one leavesRejection by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LeavesRejectionDTO> findOne(Long id) {
        log.debug("Request to get LeavesRejection : {}", id);
        return leavesRejectionRepository.findById(id)
            .map(leavesRejectionMapper::toDto);
    }

    /**
     * Delete the leavesRejection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LeavesRejection : {}", id);
        leavesRejectionRepository.deleteById(id);
    }
}

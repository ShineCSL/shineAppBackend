package com.shine.shineappback.service;

import com.shine.shineappback.domain.Leaves;
import com.shine.shineappback.repository.LeavesRepository;
import com.shine.shineappback.service.dto.LeavesDTO;
import com.shine.shineappback.service.mapper.LeavesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Leaves.
 */
@Service
@Transactional
public class LeavesService {

    private final Logger log = LoggerFactory.getLogger(LeavesService.class);

    private final LeavesRepository leavesRepository;

    private final LeavesMapper leavesMapper;

    public LeavesService(LeavesRepository leavesRepository, LeavesMapper leavesMapper) {
        this.leavesRepository = leavesRepository;
        this.leavesMapper = leavesMapper;
    }

    /**
     * Save a leaves.
     *
     * @param leavesDTO the entity to save
     * @return the persisted entity
     */
    public LeavesDTO save(LeavesDTO leavesDTO) {
        log.debug("Request to save Leaves : {}", leavesDTO);
        Leaves leaves = leavesMapper.toEntity(leavesDTO);
        leaves = leavesRepository.save(leaves);
        return leavesMapper.toDto(leaves);
    }

    /**
     * Get all the leaves.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LeavesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Leaves");
        return leavesRepository.findAll(pageable)
            .map(leavesMapper::toDto);
    }


    /**
     * Get one leaves by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LeavesDTO> findOne(Long id) {
        log.debug("Request to get Leaves : {}", id);
        return leavesRepository.findById(id)
            .map(leavesMapper::toDto);
    }

    /**
     * Delete the leaves by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Leaves : {}", id);
        leavesRepository.deleteById(id);
    }
}

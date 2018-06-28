package com.shine.shineappback.service;

import com.shine.shineappback.domain.LeavesSubmission;
import com.shine.shineappback.repository.LeavesSubmissionRepository;
import com.shine.shineappback.service.dto.LeavesSubmissionDTO;
import com.shine.shineappback.service.mapper.LeavesSubmissionMapper;
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
 * Service Implementation for managing LeavesSubmission.
 */
@Service
@Transactional
public class LeavesSubmissionService {

    private final Logger log = LoggerFactory.getLogger(LeavesSubmissionService.class);

    private final LeavesSubmissionRepository leavesSubmissionRepository;

    private final LeavesSubmissionMapper leavesSubmissionMapper;

    public LeavesSubmissionService(LeavesSubmissionRepository leavesSubmissionRepository, LeavesSubmissionMapper leavesSubmissionMapper) {
        this.leavesSubmissionRepository = leavesSubmissionRepository;
        this.leavesSubmissionMapper = leavesSubmissionMapper;
    }

    /**
     * Save a leavesSubmission.
     *
     * @param leavesSubmissionDTO the entity to save
     * @return the persisted entity
     */
    public LeavesSubmissionDTO save(LeavesSubmissionDTO leavesSubmissionDTO) {
        log.debug("Request to save LeavesSubmission : {}", leavesSubmissionDTO);
        LeavesSubmission leavesSubmission = leavesSubmissionMapper.toEntity(leavesSubmissionDTO);
        leavesSubmission = leavesSubmissionRepository.save(leavesSubmission);
        return leavesSubmissionMapper.toDto(leavesSubmission);
    }

    /**
     * Get all the leavesSubmissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LeavesSubmissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeavesSubmissions");
        return leavesSubmissionRepository.findAll(pageable)
            .map(leavesSubmissionMapper::toDto);
    }



    /**
     *  get all the leavesSubmissions where Leaves is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<LeavesSubmissionDTO> findAllWhereLeavesIsNull() {
        log.debug("Request to get all leavesSubmissions where Leaves is null");
        return StreamSupport
            .stream(leavesSubmissionRepository.findAll().spliterator(), false)
            .filter(leavesSubmission -> leavesSubmission.getLeaves() == null)
            .map(leavesSubmissionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one leavesSubmission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LeavesSubmissionDTO> findOne(Long id) {
        log.debug("Request to get LeavesSubmission : {}", id);
        return leavesSubmissionRepository.findById(id)
            .map(leavesSubmissionMapper::toDto);
    }

    /**
     * Delete the leavesSubmission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LeavesSubmission : {}", id);
        leavesSubmissionRepository.deleteById(id);
    }
}

package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.LeavesService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.LeavesDTO;
import com.shine.shineappback.service.dto.LeavesCriteria;
import com.shine.shineappback.repository.LeavesRepository;
import com.shine.shineappback.service.LeavesQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Leaves.
 */
@RestController
@RequestMapping("/api")
public class LeavesResource {

    private final Logger log = LoggerFactory.getLogger(LeavesResource.class);

    private static final String ENTITY_NAME = "leaves";

    private final LeavesService leavesService;

    private final LeavesQueryService leavesQueryService;
    
    private final LeavesRepository leavesRepository;

    public LeavesResource(LeavesService leavesService, LeavesQueryService leavesQueryService, 
    		LeavesRepository leavesRepository) {
        this.leavesService = leavesService;
        this.leavesQueryService = leavesQueryService;
        this.leavesRepository = leavesRepository;
    }

    /**
     * POST  /leaves : Create a new leaves.
     *
     * @param leavesDTO the leavesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leavesDTO, or with status 400 (Bad Request) if the leaves has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leaves")
    @Timed
    public ResponseEntity<LeavesDTO> createLeaves(@Valid @RequestBody LeavesDTO leavesDTO) throws URISyntaxException {
        log.debug("REST request to save Leaves : {}", leavesDTO);
        if (leavesDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaves cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeavesDTO result = leavesService.save(leavesDTO);
        return ResponseEntity.created(new URI("/api/leaves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leaves : Updates an existing leaves.
     *
     * @param leavesDTO the leavesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leavesDTO,
     * or with status 400 (Bad Request) if the leavesDTO is not valid,
     * or with status 500 (Internal Server Error) if the leavesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leaves")
    @Timed
    public ResponseEntity<LeavesDTO> updateLeaves(@Valid @RequestBody LeavesDTO leavesDTO) throws URISyntaxException {
        log.debug("REST request to update Leaves : {}", leavesDTO);
        if (leavesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeavesDTO result = leavesService.save(leavesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leavesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leaves : get all the leaves.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of leaves in body
     */
    @GetMapping("/leaves")
    @Timed
    public ResponseEntity<List<LeavesDTO>> getAllLeaves(LeavesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Leaves by criteria: {}", criteria);
        Page<LeavesDTO> page = leavesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaves");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leaves/:id : get the "id" leaves.
     *
     * @param id the id of the leavesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leavesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leaves/{id}")
    @Timed
    public ResponseEntity<LeavesDTO> getLeaves(@PathVariable Long id) {
        log.debug("REST request to get Leaves : {}", id);
        Optional<LeavesDTO> leavesDTO = leavesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leavesDTO);
    }

    /**
     * DELETE  /leaves/:id : delete the "id" leaves.
     *
     * @param id the id of the leavesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leaves/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeaves(@PathVariable Long id) {
        log.debug("REST request to delete Leaves : {}", id);
        leavesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /leaves : get all the leaves.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of leaves in body
     */
    @GetMapping("/leaves/sumhours/{userLogin}/{year}/{taskCode}")
    @Timed
    public ResponseEntity<Integer> getSumHoursByUserYearAndTask(@PathVariable String userLogin, @PathVariable int year, 
    		@PathVariable String taskCode) {
        log.debug("REST request to get sum nb of hours: {}");
        Integer sumNbOfHours = leavesRepository.sumHoursByUserYearAndTask(userLogin, year, taskCode);
        return ResponseEntity.ok().body(sumNbOfHours);
    }
}

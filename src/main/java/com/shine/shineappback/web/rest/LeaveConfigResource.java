package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.LeaveConfigService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.LeaveConfigDTO;
import com.shine.shineappback.service.dto.LeaveConfigCriteria;
import com.shine.shineappback.service.LeaveConfigQueryService;
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
 * REST controller for managing LeaveConfig.
 */
@RestController
@RequestMapping("/api")
public class LeaveConfigResource {

    private final Logger log = LoggerFactory.getLogger(LeaveConfigResource.class);

    private static final String ENTITY_NAME = "leaveConfig";

    private final LeaveConfigService leaveConfigService;

    private final LeaveConfigQueryService leaveConfigQueryService;

    public LeaveConfigResource(LeaveConfigService leaveConfigService, LeaveConfigQueryService leaveConfigQueryService) {
        this.leaveConfigService = leaveConfigService;
        this.leaveConfigQueryService = leaveConfigQueryService;
    }

    /**
     * POST  /leave-configs : Create a new leaveConfig.
     *
     * @param leaveConfigDTO the leaveConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leaveConfigDTO, or with status 400 (Bad Request) if the leaveConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leave-configs")
    @Timed
    public ResponseEntity<LeaveConfigDTO> createLeaveConfig(@Valid @RequestBody LeaveConfigDTO leaveConfigDTO) throws URISyntaxException {
        log.debug("REST request to save LeaveConfig : {}", leaveConfigDTO);
        if (leaveConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveConfigDTO result = leaveConfigService.save(leaveConfigDTO);
        return ResponseEntity.created(new URI("/api/leave-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leave-configs : Updates an existing leaveConfig.
     *
     * @param leaveConfigDTO the leaveConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leaveConfigDTO,
     * or with status 400 (Bad Request) if the leaveConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the leaveConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leave-configs")
    @Timed
    public ResponseEntity<LeaveConfigDTO> updateLeaveConfig(@Valid @RequestBody LeaveConfigDTO leaveConfigDTO) throws URISyntaxException {
        log.debug("REST request to update LeaveConfig : {}", leaveConfigDTO);
        if (leaveConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeaveConfigDTO result = leaveConfigService.save(leaveConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leaveConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leave-configs : get all the leaveConfigs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of leaveConfigs in body
     */
    @GetMapping("/leave-configs")
    @Timed
    public ResponseEntity<List<LeaveConfigDTO>> getAllLeaveConfigs(LeaveConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaveConfigs by criteria: {}", criteria);
        Page<LeaveConfigDTO> page = leaveConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leave-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leave-configs/:id : get the "id" leaveConfig.
     *
     * @param id the id of the leaveConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leaveConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leave-configs/{id}")
    @Timed
    public ResponseEntity<LeaveConfigDTO> getLeaveConfig(@PathVariable Long id) {
        log.debug("REST request to get LeaveConfig : {}", id);
        Optional<LeaveConfigDTO> leaveConfigDTO = leaveConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveConfigDTO);
    }

    /**
     * DELETE  /leave-configs/:id : delete the "id" leaveConfig.
     *
     * @param id the id of the leaveConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leave-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeaveConfig(@PathVariable Long id) {
        log.debug("REST request to delete LeaveConfig : {}", id);
        leaveConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

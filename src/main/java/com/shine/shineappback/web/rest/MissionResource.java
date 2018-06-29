package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.MissionService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.MissionDTO;
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
 * REST controller for managing Mission.
 */
@RestController
@RequestMapping("/api")
public class MissionResource {

    private final Logger log = LoggerFactory.getLogger(MissionResource.class);

    private static final String ENTITY_NAME = "mission";

    private final MissionService missionService;

    public MissionResource(MissionService missionService) {
        this.missionService = missionService;
    }

    /**
     * POST  /missions : Create a new mission.
     *
     * @param missionDTO the missionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new missionDTO, or with status 400 (Bad Request) if the mission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/missions")
    @Timed
    public ResponseEntity<MissionDTO> createMission(@Valid @RequestBody MissionDTO missionDTO) throws URISyntaxException {
        log.debug("REST request to save Mission : {}", missionDTO);
        if (missionDTO.getId() != null) {
            throw new BadRequestAlertException("A new mission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MissionDTO result = missionService.save(missionDTO);
        return ResponseEntity.created(new URI("/api/missions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /missions : Updates an existing mission.
     *
     * @param missionDTO the missionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated missionDTO,
     * or with status 400 (Bad Request) if the missionDTO is not valid,
     * or with status 500 (Internal Server Error) if the missionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/missions")
    @Timed
    public ResponseEntity<MissionDTO> updateMission(@Valid @RequestBody MissionDTO missionDTO) throws URISyntaxException {
        log.debug("REST request to update Mission : {}", missionDTO);
        if (missionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MissionDTO result = missionService.save(missionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, missionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /missions : get all the missions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of missions in body
     */
    @GetMapping("/missions")
    @Timed
    public ResponseEntity<List<MissionDTO>> getAllMissions(Pageable pageable) {
        log.debug("REST request to get a page of Missions");
        Page<MissionDTO> page = missionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/missions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /missions/:id : get the "id" mission.
     *
     * @param id the id of the missionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the missionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/missions/{id}")
    @Timed
    public ResponseEntity<MissionDTO> getMission(@PathVariable Long id) {
        log.debug("REST request to get Mission : {}", id);
        Optional<MissionDTO> missionDTO = missionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(missionDTO);
    }

    /**
     * DELETE  /missions/:id : delete the "id" mission.
     *
     * @param id the id of the missionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/missions/{id}")
    @Timed
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        log.debug("REST request to delete Mission : {}", id);
        missionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

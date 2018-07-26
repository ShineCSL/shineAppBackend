package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.PublicHolidayService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.PublicHolidayDTO;
import com.shine.shineappback.service.dto.PublicHolidayCriteria;
import com.shine.shineappback.service.PublicHolidayQueryService;
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
 * REST controller for managing PublicHoliday.
 */
@RestController
@RequestMapping("/api")
public class PublicHolidayResource {

    private final Logger log = LoggerFactory.getLogger(PublicHolidayResource.class);

    private static final String ENTITY_NAME = "publicHoliday";

    private final PublicHolidayService publicHolidayService;

    private final PublicHolidayQueryService publicHolidayQueryService;

    public PublicHolidayResource(PublicHolidayService publicHolidayService, PublicHolidayQueryService publicHolidayQueryService) {
        this.publicHolidayService = publicHolidayService;
        this.publicHolidayQueryService = publicHolidayQueryService;
    }

    /**
     * POST  /public-holidays : Create a new publicHoliday.
     *
     * @param publicHolidayDTO the publicHolidayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publicHolidayDTO, or with status 400 (Bad Request) if the publicHoliday has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/public-holidays")
    @Timed
    public ResponseEntity<PublicHolidayDTO> createPublicHoliday(@Valid @RequestBody PublicHolidayDTO publicHolidayDTO) throws URISyntaxException {
        log.debug("REST request to save PublicHoliday : {}", publicHolidayDTO);
        if (publicHolidayDTO.getId() != null) {
            throw new BadRequestAlertException("A new publicHoliday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PublicHolidayDTO result = publicHolidayService.save(publicHolidayDTO);
        return ResponseEntity.created(new URI("/api/public-holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /public-holidays : Updates an existing publicHoliday.
     *
     * @param publicHolidayDTO the publicHolidayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publicHolidayDTO,
     * or with status 400 (Bad Request) if the publicHolidayDTO is not valid,
     * or with status 500 (Internal Server Error) if the publicHolidayDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/public-holidays")
    @Timed
    public ResponseEntity<PublicHolidayDTO> updatePublicHoliday(@Valid @RequestBody PublicHolidayDTO publicHolidayDTO) throws URISyntaxException {
        log.debug("REST request to update PublicHoliday : {}", publicHolidayDTO);
        if (publicHolidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PublicHolidayDTO result = publicHolidayService.save(publicHolidayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, publicHolidayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /public-holidays : get all the publicHolidays.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of publicHolidays in body
     */
    @GetMapping("/public-holidays")
    @Timed
    public ResponseEntity<List<PublicHolidayDTO>> getAllPublicHolidays(PublicHolidayCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PublicHolidays by criteria: {}", criteria);
        Page<PublicHolidayDTO> page = publicHolidayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/public-holidays");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /public-holidays/:id : get the "id" publicHoliday.
     *
     * @param id the id of the publicHolidayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publicHolidayDTO, or with status 404 (Not Found)
     */
    @GetMapping("/public-holidays/{id}")
    @Timed
    public ResponseEntity<PublicHolidayDTO> getPublicHoliday(@PathVariable Long id) {
        log.debug("REST request to get PublicHoliday : {}", id);
        Optional<PublicHolidayDTO> publicHolidayDTO = publicHolidayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publicHolidayDTO);
    }

    /**
     * DELETE  /public-holidays/:id : delete the "id" publicHoliday.
     *
     * @param id the id of the publicHolidayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/public-holidays/{id}")
    @Timed
    public ResponseEntity<Void> deletePublicHoliday(@PathVariable Long id) {
        log.debug("REST request to delete PublicHoliday : {}", id);
        publicHolidayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

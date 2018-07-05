package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.LeavesValidationService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.LeavesValidationDTO;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing LeavesValidation.
 */
@RestController
@RequestMapping("/api")
public class LeavesValidationResource {

    private final Logger log = LoggerFactory.getLogger(LeavesValidationResource.class);

    private static final String ENTITY_NAME = "leavesValidation";

    private final LeavesValidationService leavesValidationService;

    public LeavesValidationResource(LeavesValidationService leavesValidationService) {
        this.leavesValidationService = leavesValidationService;
    }

    /**
     * POST  /leaves-validations : Create a new leavesValidation.
     *
     * @param leavesValidationDTO the leavesValidationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leavesValidationDTO, or with status 400 (Bad Request) if the leavesValidation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leaves-validations")
    @Timed
    public ResponseEntity<LeavesValidationDTO> createLeavesValidation(@Valid @RequestBody LeavesValidationDTO leavesValidationDTO) throws URISyntaxException {
        log.debug("REST request to save LeavesValidation : {}", leavesValidationDTO);
        if (leavesValidationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leavesValidation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeavesValidationDTO result = leavesValidationService.save(leavesValidationDTO);
        return ResponseEntity.created(new URI("/api/leaves-validations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leaves-validations : Updates an existing leavesValidation.
     *
     * @param leavesValidationDTO the leavesValidationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leavesValidationDTO,
     * or with status 400 (Bad Request) if the leavesValidationDTO is not valid,
     * or with status 500 (Internal Server Error) if the leavesValidationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leaves-validations")
    @Timed
    public ResponseEntity<LeavesValidationDTO> updateLeavesValidation(@Valid @RequestBody LeavesValidationDTO leavesValidationDTO) throws URISyntaxException {
        log.debug("REST request to update LeavesValidation : {}", leavesValidationDTO);
        if (leavesValidationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeavesValidationDTO result = leavesValidationService.save(leavesValidationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leavesValidationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leaves-validations : get all the leavesValidations.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of leavesValidations in body
     */
    @GetMapping("/leaves-validations")
    @Timed
    public ResponseEntity<List<LeavesValidationDTO>> getAllLeavesValidations(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("leaves-is-null".equals(filter)) {
            log.debug("REST request to get all LeavesValidations where leaves is null");
            return new ResponseEntity<>(leavesValidationService.findAllWhereLeavesIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of LeavesValidations");
        Page<LeavesValidationDTO> page = leavesValidationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaves-validations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leaves-validations/:id : get the "id" leavesValidation.
     *
     * @param id the id of the leavesValidationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leavesValidationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leaves-validations/{id}")
    @Timed
    public ResponseEntity<LeavesValidationDTO> getLeavesValidation(@PathVariable Long id) {
        log.debug("REST request to get LeavesValidation : {}", id);
        Optional<LeavesValidationDTO> leavesValidationDTO = leavesValidationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leavesValidationDTO);
    }

    /**
     * DELETE  /leaves-validations/:id : delete the "id" leavesValidation.
     *
     * @param id the id of the leavesValidationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leaves-validations/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeavesValidation(@PathVariable Long id) {
        log.debug("REST request to delete LeavesValidation : {}", id);
        leavesValidationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

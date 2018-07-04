package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.ActivityValidationService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.ActivityValidationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ActivityValidation.
 */
@RestController
@RequestMapping("/api")
public class ActivityValidationResource {

    private final Logger log = LoggerFactory.getLogger(ActivityValidationResource.class);

    private static final String ENTITY_NAME = "activityValidation";

    private final ActivityValidationService activityValidationService;

    public ActivityValidationResource(ActivityValidationService activityValidationService) {
        this.activityValidationService = activityValidationService;
    }

    /**
     * POST  /activity-validations : Create a new activityValidation.
     *
     * @param activityValidationDTO the activityValidationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityValidationDTO, or with status 400 (Bad Request) if the activityValidation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-validations")
    @Timed
    public ResponseEntity<ActivityValidationDTO> createActivityValidation(@RequestBody ActivityValidationDTO activityValidationDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityValidation : {}", activityValidationDTO);
        if (activityValidationDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityValidation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityValidationDTO result = activityValidationService.save(activityValidationDTO);
        return ResponseEntity.created(new URI("/api/activity-validations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-validations : Updates an existing activityValidation.
     *
     * @param activityValidationDTO the activityValidationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityValidationDTO,
     * or with status 400 (Bad Request) if the activityValidationDTO is not valid,
     * or with status 500 (Internal Server Error) if the activityValidationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-validations")
    @Timed
    public ResponseEntity<ActivityValidationDTO> updateActivityValidation(@RequestBody ActivityValidationDTO activityValidationDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityValidation : {}", activityValidationDTO);
        if (activityValidationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityValidationDTO result = activityValidationService.save(activityValidationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityValidationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-validations : get all the activityValidations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activityValidations in body
     */
    @GetMapping("/activity-validations")
    @Timed
    public ResponseEntity<List<ActivityValidationDTO>> getAllActivityValidations(Pageable pageable) {
        log.debug("REST request to get a page of ActivityValidations");
        Page<ActivityValidationDTO> page = activityValidationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-validations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activity-validations/:id : get the "id" activityValidation.
     *
     * @param id the id of the activityValidationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityValidationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activity-validations/{id}")
    @Timed
    public ResponseEntity<ActivityValidationDTO> getActivityValidation(@PathVariable Long id) {
        log.debug("REST request to get ActivityValidation : {}", id);
        Optional<ActivityValidationDTO> activityValidationDTO = activityValidationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityValidationDTO);
    }

    /**
     * DELETE  /activity-validations/:id : delete the "id" activityValidation.
     *
     * @param id the id of the activityValidationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-validations/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityValidation(@PathVariable Long id) {
        log.debug("REST request to delete ActivityValidation : {}", id);
        activityValidationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

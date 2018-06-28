package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.ActivitySubmissionService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.ActivitySubmissionDTO;
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
 * REST controller for managing ActivitySubmission.
 */
@RestController
@RequestMapping("/api")
public class ActivitySubmissionResource {

    private final Logger log = LoggerFactory.getLogger(ActivitySubmissionResource.class);

    private static final String ENTITY_NAME = "activitySubmission";

    private final ActivitySubmissionService activitySubmissionService;

    public ActivitySubmissionResource(ActivitySubmissionService activitySubmissionService) {
        this.activitySubmissionService = activitySubmissionService;
    }

    /**
     * POST  /activity-submissions : Create a new activitySubmission.
     *
     * @param activitySubmissionDTO the activitySubmissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activitySubmissionDTO, or with status 400 (Bad Request) if the activitySubmission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-submissions")
    @Timed
    public ResponseEntity<ActivitySubmissionDTO> createActivitySubmission(@Valid @RequestBody ActivitySubmissionDTO activitySubmissionDTO) throws URISyntaxException {
        log.debug("REST request to save ActivitySubmission : {}", activitySubmissionDTO);
        if (activitySubmissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new activitySubmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivitySubmissionDTO result = activitySubmissionService.save(activitySubmissionDTO);
        return ResponseEntity.created(new URI("/api/activity-submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-submissions : Updates an existing activitySubmission.
     *
     * @param activitySubmissionDTO the activitySubmissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activitySubmissionDTO,
     * or with status 400 (Bad Request) if the activitySubmissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the activitySubmissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-submissions")
    @Timed
    public ResponseEntity<ActivitySubmissionDTO> updateActivitySubmission(@Valid @RequestBody ActivitySubmissionDTO activitySubmissionDTO) throws URISyntaxException {
        log.debug("REST request to update ActivitySubmission : {}", activitySubmissionDTO);
        if (activitySubmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivitySubmissionDTO result = activitySubmissionService.save(activitySubmissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activitySubmissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-submissions : get all the activitySubmissions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activitySubmissions in body
     */
    @GetMapping("/activity-submissions")
    @Timed
    public ResponseEntity<List<ActivitySubmissionDTO>> getAllActivitySubmissions(Pageable pageable) {
        log.debug("REST request to get a page of ActivitySubmissions");
        Page<ActivitySubmissionDTO> page = activitySubmissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-submissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activity-submissions/:id : get the "id" activitySubmission.
     *
     * @param id the id of the activitySubmissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activitySubmissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activity-submissions/{id}")
    @Timed
    public ResponseEntity<ActivitySubmissionDTO> getActivitySubmission(@PathVariable Long id) {
        log.debug("REST request to get ActivitySubmission : {}", id);
        Optional<ActivitySubmissionDTO> activitySubmissionDTO = activitySubmissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activitySubmissionDTO);
    }

    /**
     * DELETE  /activity-submissions/:id : delete the "id" activitySubmission.
     *
     * @param id the id of the activitySubmissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-submissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivitySubmission(@PathVariable Long id) {
        log.debug("REST request to delete ActivitySubmission : {}", id);
        activitySubmissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

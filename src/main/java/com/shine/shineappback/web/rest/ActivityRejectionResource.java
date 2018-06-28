package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.ActivityRejectionService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.ActivityRejectionDTO;
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
 * REST controller for managing ActivityRejection.
 */
@RestController
@RequestMapping("/api")
public class ActivityRejectionResource {

    private final Logger log = LoggerFactory.getLogger(ActivityRejectionResource.class);

    private static final String ENTITY_NAME = "activityRejection";

    private final ActivityRejectionService activityRejectionService;

    public ActivityRejectionResource(ActivityRejectionService activityRejectionService) {
        this.activityRejectionService = activityRejectionService;
    }

    /**
     * POST  /activity-rejections : Create a new activityRejection.
     *
     * @param activityRejectionDTO the activityRejectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityRejectionDTO, or with status 400 (Bad Request) if the activityRejection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-rejections")
    @Timed
    public ResponseEntity<ActivityRejectionDTO> createActivityRejection(@Valid @RequestBody ActivityRejectionDTO activityRejectionDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityRejection : {}", activityRejectionDTO);
        if (activityRejectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityRejection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityRejectionDTO result = activityRejectionService.save(activityRejectionDTO);
        return ResponseEntity.created(new URI("/api/activity-rejections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-rejections : Updates an existing activityRejection.
     *
     * @param activityRejectionDTO the activityRejectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityRejectionDTO,
     * or with status 400 (Bad Request) if the activityRejectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the activityRejectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-rejections")
    @Timed
    public ResponseEntity<ActivityRejectionDTO> updateActivityRejection(@Valid @RequestBody ActivityRejectionDTO activityRejectionDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityRejection : {}", activityRejectionDTO);
        if (activityRejectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityRejectionDTO result = activityRejectionService.save(activityRejectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityRejectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-rejections : get all the activityRejections.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activityRejections in body
     */
    @GetMapping("/activity-rejections")
    @Timed
    public ResponseEntity<List<ActivityRejectionDTO>> getAllActivityRejections(Pageable pageable) {
        log.debug("REST request to get a page of ActivityRejections");
        Page<ActivityRejectionDTO> page = activityRejectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-rejections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activity-rejections/:id : get the "id" activityRejection.
     *
     * @param id the id of the activityRejectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityRejectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activity-rejections/{id}")
    @Timed
    public ResponseEntity<ActivityRejectionDTO> getActivityRejection(@PathVariable Long id) {
        log.debug("REST request to get ActivityRejection : {}", id);
        Optional<ActivityRejectionDTO> activityRejectionDTO = activityRejectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityRejectionDTO);
    }

    /**
     * DELETE  /activity-rejections/:id : delete the "id" activityRejection.
     *
     * @param id the id of the activityRejectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-rejections/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityRejection(@PathVariable Long id) {
        log.debug("REST request to delete ActivityRejection : {}", id);
        activityRejectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

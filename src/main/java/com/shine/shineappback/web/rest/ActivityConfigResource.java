package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.ActivityConfigService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.ActivityConfigDTO;
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
 * REST controller for managing ActivityConfig.
 */
@RestController
@RequestMapping("/api")
public class ActivityConfigResource {

    private final Logger log = LoggerFactory.getLogger(ActivityConfigResource.class);

    private static final String ENTITY_NAME = "activityConfig";

    private final ActivityConfigService activityConfigService;

    public ActivityConfigResource(ActivityConfigService activityConfigService) {
        this.activityConfigService = activityConfigService;
    }

    /**
     * POST  /activity-configs : Create a new activityConfig.
     *
     * @param activityConfigDTO the activityConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityConfigDTO, or with status 400 (Bad Request) if the activityConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activity-configs")
    @Timed
    public ResponseEntity<ActivityConfigDTO> createActivityConfig(@Valid @RequestBody ActivityConfigDTO activityConfigDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityConfig : {}", activityConfigDTO);
        if (activityConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityConfigDTO result = activityConfigService.save(activityConfigDTO);
        return ResponseEntity.created(new URI("/api/activity-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-configs : Updates an existing activityConfig.
     *
     * @param activityConfigDTO the activityConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityConfigDTO,
     * or with status 400 (Bad Request) if the activityConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the activityConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activity-configs")
    @Timed
    public ResponseEntity<ActivityConfigDTO> updateActivityConfig(@Valid @RequestBody ActivityConfigDTO activityConfigDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityConfig : {}", activityConfigDTO);
        if (activityConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityConfigDTO result = activityConfigService.save(activityConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-configs : get all the activityConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activityConfigs in body
     */
    @GetMapping("/activity-configs")
    @Timed
    public ResponseEntity<List<ActivityConfigDTO>> getAllActivityConfigs(Pageable pageable) {
        log.debug("REST request to get a page of ActivityConfigs");
        Page<ActivityConfigDTO> page = activityConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activity-configs/:id : get the "id" activityConfig.
     *
     * @param id the id of the activityConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/activity-configs/{id}")
    @Timed
    public ResponseEntity<ActivityConfigDTO> getActivityConfig(@PathVariable Long id) {
        log.debug("REST request to get ActivityConfig : {}", id);
        Optional<ActivityConfigDTO> activityConfigDTO = activityConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityConfigDTO);
    }

    /**
     * DELETE  /activity-configs/:id : delete the "id" activityConfig.
     *
     * @param id the id of the activityConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activity-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivityConfig(@PathVariable Long id) {
        log.debug("REST request to delete ActivityConfig : {}", id);
        activityConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

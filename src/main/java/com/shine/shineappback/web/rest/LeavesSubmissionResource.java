package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.LeavesSubmissionService;
import com.shine.shineappback.service.MailService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.LeavesSubmissionDTO;
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
 * REST controller for managing LeavesSubmission.
 */
@RestController
@RequestMapping("/api")
public class LeavesSubmissionResource {

    private final Logger log = LoggerFactory.getLogger(LeavesSubmissionResource.class);

    private static final String ENTITY_NAME = "leavesSubmission";

    private final LeavesSubmissionService leavesSubmissionService;
    
    private final MailService mailService;

    public LeavesSubmissionResource(LeavesSubmissionService leavesSubmissionService, 
    		MailService mailService) {
        this.leavesSubmissionService = leavesSubmissionService;
        this.mailService = mailService;
    }

    /**
     * POST  /leaves-submissions : Create a new leavesSubmission.
     *
     * @param leavesSubmissionDTO the leavesSubmissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leavesSubmissionDTO, or with status 400 (Bad Request) if the leavesSubmission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leaves-submissions")
    @Timed
    public ResponseEntity<LeavesSubmissionDTO> createLeavesSubmission(@Valid @RequestBody LeavesSubmissionDTO leavesSubmissionDTO) throws URISyntaxException {
        log.debug("REST request to save LeavesSubmission : {}", leavesSubmissionDTO);
        if (leavesSubmissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new leavesSubmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeavesSubmissionDTO result = leavesSubmissionService.save(leavesSubmissionDTO);
        mailService.sendSubmitMail("Leave submitted", leavesSubmissionDTO.getUserLogin(), leavesSubmissionDTO.getLeavesDate());
        return ResponseEntity.created(new URI("/api/leaves-submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leaves-submissions : Updates an existing leavesSubmission.
     *
     * @param leavesSubmissionDTO the leavesSubmissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leavesSubmissionDTO,
     * or with status 400 (Bad Request) if the leavesSubmissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the leavesSubmissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leaves-submissions")
    @Timed
    public ResponseEntity<LeavesSubmissionDTO> updateLeavesSubmission(@Valid @RequestBody LeavesSubmissionDTO leavesSubmissionDTO) throws URISyntaxException {
        log.debug("REST request to update LeavesSubmission : {}", leavesSubmissionDTO);
        if (leavesSubmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeavesSubmissionDTO result = leavesSubmissionService.save(leavesSubmissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leavesSubmissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leaves-submissions : get all the leavesSubmissions.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of leavesSubmissions in body
     */
    @GetMapping("/leaves-submissions")
    @Timed
    public ResponseEntity<List<LeavesSubmissionDTO>> getAllLeavesSubmissions(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("leaves-is-null".equals(filter)) {
            log.debug("REST request to get all LeavesSubmissions where leaves is null");
            return new ResponseEntity<>(leavesSubmissionService.findAllWhereLeavesIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of LeavesSubmissions");
        Page<LeavesSubmissionDTO> page = leavesSubmissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaves-submissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leaves-submissions/:id : get the "id" leavesSubmission.
     *
     * @param id the id of the leavesSubmissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leavesSubmissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leaves-submissions/{id}")
    @Timed
    public ResponseEntity<LeavesSubmissionDTO> getLeavesSubmission(@PathVariable Long id) {
        log.debug("REST request to get LeavesSubmission : {}", id);
        Optional<LeavesSubmissionDTO> leavesSubmissionDTO = leavesSubmissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leavesSubmissionDTO);
    }

    /**
     * DELETE  /leaves-submissions/:id : delete the "id" leavesSubmission.
     *
     * @param id the id of the leavesSubmissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leaves-submissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeavesSubmission(@PathVariable Long id) {
        log.debug("REST request to delete LeavesSubmission : {}", id);
        leavesSubmissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

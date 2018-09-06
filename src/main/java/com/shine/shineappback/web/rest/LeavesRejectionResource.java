package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.repository.UserRepository;
import com.shine.shineappback.service.LeavesRejectionService;
import com.shine.shineappback.service.MailService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.LeavesRejectionDTO;
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
 * REST controller for managing LeavesRejection.
 */
@RestController
@RequestMapping("/api")
public class LeavesRejectionResource {

    private final Logger log = LoggerFactory.getLogger(LeavesRejectionResource.class);

    private static final String ENTITY_NAME = "leavesRejection";

    private final LeavesRejectionService leavesRejectionService;
        
    private final MailService mailService;

    public LeavesRejectionResource(LeavesRejectionService leavesRejectionService, 
    		MailService mailService) {
        this.leavesRejectionService = leavesRejectionService;
        this.mailService = mailService;
    }

    /**
     * POST  /leaves-rejections : Create a new leavesRejection.
     *
     * @param leavesRejectionDTO the leavesRejectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leavesRejectionDTO, or with status 400 (Bad Request) if the leavesRejection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leaves-rejections")
    @Timed
    public ResponseEntity<LeavesRejectionDTO> createLeavesRejection(@Valid @RequestBody LeavesRejectionDTO leavesRejectionDTO) throws URISyntaxException {
        log.debug("REST request to save LeavesRejection : {}", leavesRejectionDTO);
        if (leavesRejectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new leavesRejection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeavesRejectionDTO result = leavesRejectionService.save(leavesRejectionDTO);      
        mailService.sendValidateOrRejectMail("Leave rejected", leavesRejectionDTO.getUserLogin(), leavesRejectionDTO.getLeavesDate());
        return ResponseEntity.created(new URI("/api/leaves-rejections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leaves-rejections : Updates an existing leavesRejection.
     *
     * @param leavesRejectionDTO the leavesRejectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leavesRejectionDTO,
     * or with status 400 (Bad Request) if the leavesRejectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the leavesRejectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leaves-rejections")
    @Timed
    public ResponseEntity<LeavesRejectionDTO> updateLeavesRejection(@Valid @RequestBody LeavesRejectionDTO leavesRejectionDTO) throws URISyntaxException {
        log.debug("REST request to update LeavesRejection : {}", leavesRejectionDTO);
        if (leavesRejectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeavesRejectionDTO result = leavesRejectionService.save(leavesRejectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leavesRejectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leaves-rejections : get all the leavesRejections.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of leavesRejections in body
     */
    @GetMapping("/leaves-rejections")
    @Timed
    public ResponseEntity<List<LeavesRejectionDTO>> getAllLeavesRejections(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("leaves-is-null".equals(filter)) {
            log.debug("REST request to get all LeavesRejections where leaves is null");
            return new ResponseEntity<>(leavesRejectionService.findAllWhereLeavesIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of LeavesRejections");
        Page<LeavesRejectionDTO> page = leavesRejectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaves-rejections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leaves-rejections/:id : get the "id" leavesRejection.
     *
     * @param id the id of the leavesRejectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leavesRejectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leaves-rejections/{id}")
    @Timed
    public ResponseEntity<LeavesRejectionDTO> getLeavesRejection(@PathVariable Long id) {
        log.debug("REST request to get LeavesRejection : {}", id);
        Optional<LeavesRejectionDTO> leavesRejectionDTO = leavesRejectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leavesRejectionDTO);
    }

    /**
     * DELETE  /leaves-rejections/:id : delete the "id" leavesRejection.
     *
     * @param id the id of the leavesRejectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leaves-rejections/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeavesRejection(@PathVariable Long id) {
        log.debug("REST request to delete LeavesRejection : {}", id);
        leavesRejectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

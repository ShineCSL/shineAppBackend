package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.InvoiceSubmissionService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.InvoiceSubmissionDTO;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing InvoiceSubmission.
 */
@RestController
@RequestMapping("/api")
public class InvoiceSubmissionResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceSubmissionResource.class);

    private static final String ENTITY_NAME = "invoiceSubmission";

    private final InvoiceSubmissionService invoiceSubmissionService;

    public InvoiceSubmissionResource(InvoiceSubmissionService invoiceSubmissionService) {
        this.invoiceSubmissionService = invoiceSubmissionService;
    }

    /**
     * POST  /invoice-submissions : Create a new invoiceSubmission.
     *
     * @param invoiceSubmissionDTO the invoiceSubmissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceSubmissionDTO, or with status 400 (Bad Request) if the invoiceSubmission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-submissions")
    @Timed
    public ResponseEntity<InvoiceSubmissionDTO> createInvoiceSubmission(@RequestBody InvoiceSubmissionDTO invoiceSubmissionDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceSubmission : {}", invoiceSubmissionDTO);
        if (invoiceSubmissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceSubmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceSubmissionDTO result = invoiceSubmissionService.save(invoiceSubmissionDTO);
        return ResponseEntity.created(new URI("/api/invoice-submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-submissions : Updates an existing invoiceSubmission.
     *
     * @param invoiceSubmissionDTO the invoiceSubmissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceSubmissionDTO,
     * or with status 400 (Bad Request) if the invoiceSubmissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceSubmissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-submissions")
    @Timed
    public ResponseEntity<InvoiceSubmissionDTO> updateInvoiceSubmission(@RequestBody InvoiceSubmissionDTO invoiceSubmissionDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceSubmission : {}", invoiceSubmissionDTO);
        if (invoiceSubmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceSubmissionDTO result = invoiceSubmissionService.save(invoiceSubmissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceSubmissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-submissions : get all the invoiceSubmissions.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceSubmissions in body
     */
    @GetMapping("/invoice-submissions")
    @Timed
    public ResponseEntity<List<InvoiceSubmissionDTO>> getAllInvoiceSubmissions(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("invoice-is-null".equals(filter)) {
            log.debug("REST request to get all InvoiceSubmissions where invoice is null");
            return new ResponseEntity<>(invoiceSubmissionService.findAllWhereInvoiceIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of InvoiceSubmissions");
        Page<InvoiceSubmissionDTO> page = invoiceSubmissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-submissions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-submissions/:id : get the "id" invoiceSubmission.
     *
     * @param id the id of the invoiceSubmissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceSubmissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-submissions/{id}")
    @Timed
    public ResponseEntity<InvoiceSubmissionDTO> getInvoiceSubmission(@PathVariable Long id) {
        log.debug("REST request to get InvoiceSubmission : {}", id);
        Optional<InvoiceSubmissionDTO> invoiceSubmissionDTO = invoiceSubmissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceSubmissionDTO);
    }

    /**
     * DELETE  /invoice-submissions/:id : delete the "id" invoiceSubmission.
     *
     * @param id the id of the invoiceSubmissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-submissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceSubmission(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceSubmission : {}", id);
        invoiceSubmissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

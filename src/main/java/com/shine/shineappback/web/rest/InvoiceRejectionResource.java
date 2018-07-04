package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.InvoiceRejectionService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.InvoiceRejectionDTO;
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
 * REST controller for managing InvoiceRejection.
 */
@RestController
@RequestMapping("/api")
public class InvoiceRejectionResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceRejectionResource.class);

    private static final String ENTITY_NAME = "invoiceRejection";

    private final InvoiceRejectionService invoiceRejectionService;

    public InvoiceRejectionResource(InvoiceRejectionService invoiceRejectionService) {
        this.invoiceRejectionService = invoiceRejectionService;
    }

    /**
     * POST  /invoice-rejections : Create a new invoiceRejection.
     *
     * @param invoiceRejectionDTO the invoiceRejectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceRejectionDTO, or with status 400 (Bad Request) if the invoiceRejection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-rejections")
    @Timed
    public ResponseEntity<InvoiceRejectionDTO> createInvoiceRejection(@RequestBody InvoiceRejectionDTO invoiceRejectionDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceRejection : {}", invoiceRejectionDTO);
        if (invoiceRejectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceRejection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceRejectionDTO result = invoiceRejectionService.save(invoiceRejectionDTO);
        return ResponseEntity.created(new URI("/api/invoice-rejections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-rejections : Updates an existing invoiceRejection.
     *
     * @param invoiceRejectionDTO the invoiceRejectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceRejectionDTO,
     * or with status 400 (Bad Request) if the invoiceRejectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceRejectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-rejections")
    @Timed
    public ResponseEntity<InvoiceRejectionDTO> updateInvoiceRejection(@RequestBody InvoiceRejectionDTO invoiceRejectionDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceRejection : {}", invoiceRejectionDTO);
        if (invoiceRejectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceRejectionDTO result = invoiceRejectionService.save(invoiceRejectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceRejectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-rejections : get all the invoiceRejections.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceRejections in body
     */
    @GetMapping("/invoice-rejections")
    @Timed
    public ResponseEntity<List<InvoiceRejectionDTO>> getAllInvoiceRejections(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("invoice-is-null".equals(filter)) {
            log.debug("REST request to get all InvoiceRejections where invoice is null");
            return new ResponseEntity<>(invoiceRejectionService.findAllWhereInvoiceIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of InvoiceRejections");
        Page<InvoiceRejectionDTO> page = invoiceRejectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-rejections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-rejections/:id : get the "id" invoiceRejection.
     *
     * @param id the id of the invoiceRejectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceRejectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-rejections/{id}")
    @Timed
    public ResponseEntity<InvoiceRejectionDTO> getInvoiceRejection(@PathVariable Long id) {
        log.debug("REST request to get InvoiceRejection : {}", id);
        Optional<InvoiceRejectionDTO> invoiceRejectionDTO = invoiceRejectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceRejectionDTO);
    }

    /**
     * DELETE  /invoice-rejections/:id : delete the "id" invoiceRejection.
     *
     * @param id the id of the invoiceRejectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-rejections/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceRejection(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceRejection : {}", id);
        invoiceRejectionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

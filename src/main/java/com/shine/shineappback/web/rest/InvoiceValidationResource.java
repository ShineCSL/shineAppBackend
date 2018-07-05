package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.InvoiceValidationService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.InvoiceValidationDTO;
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
 * REST controller for managing InvoiceValidation.
 */
@RestController
@RequestMapping("/api")
public class InvoiceValidationResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceValidationResource.class);

    private static final String ENTITY_NAME = "invoiceValidation";

    private final InvoiceValidationService invoiceValidationService;

    public InvoiceValidationResource(InvoiceValidationService invoiceValidationService) {
        this.invoiceValidationService = invoiceValidationService;
    }

    /**
     * POST  /invoice-validations : Create a new invoiceValidation.
     *
     * @param invoiceValidationDTO the invoiceValidationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceValidationDTO, or with status 400 (Bad Request) if the invoiceValidation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-validations")
    @Timed
    public ResponseEntity<InvoiceValidationDTO> createInvoiceValidation(@Valid @RequestBody InvoiceValidationDTO invoiceValidationDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceValidation : {}", invoiceValidationDTO);
        if (invoiceValidationDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceValidation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceValidationDTO result = invoiceValidationService.save(invoiceValidationDTO);
        return ResponseEntity.created(new URI("/api/invoice-validations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-validations : Updates an existing invoiceValidation.
     *
     * @param invoiceValidationDTO the invoiceValidationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceValidationDTO,
     * or with status 400 (Bad Request) if the invoiceValidationDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceValidationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-validations")
    @Timed
    public ResponseEntity<InvoiceValidationDTO> updateInvoiceValidation(@Valid @RequestBody InvoiceValidationDTO invoiceValidationDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceValidation : {}", invoiceValidationDTO);
        if (invoiceValidationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceValidationDTO result = invoiceValidationService.save(invoiceValidationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceValidationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-validations : get all the invoiceValidations.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceValidations in body
     */
    @GetMapping("/invoice-validations")
    @Timed
    public ResponseEntity<List<InvoiceValidationDTO>> getAllInvoiceValidations(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("invoice-is-null".equals(filter)) {
            log.debug("REST request to get all InvoiceValidations where invoice is null");
            return new ResponseEntity<>(invoiceValidationService.findAllWhereInvoiceIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of InvoiceValidations");
        Page<InvoiceValidationDTO> page = invoiceValidationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-validations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-validations/:id : get the "id" invoiceValidation.
     *
     * @param id the id of the invoiceValidationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceValidationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-validations/{id}")
    @Timed
    public ResponseEntity<InvoiceValidationDTO> getInvoiceValidation(@PathVariable Long id) {
        log.debug("REST request to get InvoiceValidation : {}", id);
        Optional<InvoiceValidationDTO> invoiceValidationDTO = invoiceValidationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceValidationDTO);
    }

    /**
     * DELETE  /invoice-validations/:id : delete the "id" invoiceValidation.
     *
     * @param id the id of the invoiceValidationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-validations/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceValidation(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceValidation : {}", id);
        invoiceValidationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

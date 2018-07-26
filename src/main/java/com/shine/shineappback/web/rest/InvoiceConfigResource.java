package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.InvoiceConfigService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.InvoiceConfigDTO;
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
 * REST controller for managing InvoiceConfig.
 */
@RestController
@RequestMapping("/api")
public class InvoiceConfigResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceConfigResource.class);

    private static final String ENTITY_NAME = "invoiceConfig";

    private final InvoiceConfigService invoiceConfigService;

    public InvoiceConfigResource(InvoiceConfigService invoiceConfigService) {
        this.invoiceConfigService = invoiceConfigService;
    }

    /**
     * POST  /invoice-configs : Create a new invoiceConfig.
     *
     * @param invoiceConfigDTO the invoiceConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceConfigDTO, or with status 400 (Bad Request) if the invoiceConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-configs")
    @Timed
    public ResponseEntity<InvoiceConfigDTO> createInvoiceConfig(@Valid @RequestBody InvoiceConfigDTO invoiceConfigDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceConfig : {}", invoiceConfigDTO);
        if (invoiceConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceConfigDTO result = invoiceConfigService.save(invoiceConfigDTO);
        return ResponseEntity.created(new URI("/api/invoice-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-configs : Updates an existing invoiceConfig.
     *
     * @param invoiceConfigDTO the invoiceConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceConfigDTO,
     * or with status 400 (Bad Request) if the invoiceConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-configs")
    @Timed
    public ResponseEntity<InvoiceConfigDTO> updateInvoiceConfig(@Valid @RequestBody InvoiceConfigDTO invoiceConfigDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceConfig : {}", invoiceConfigDTO);
        if (invoiceConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceConfigDTO result = invoiceConfigService.save(invoiceConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-configs : get all the invoiceConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceConfigs in body
     */
    @GetMapping("/invoice-configs")
    @Timed
    public ResponseEntity<List<InvoiceConfigDTO>> getAllInvoiceConfigs(Pageable pageable) {
        log.debug("REST request to get a page of InvoiceConfigs");
        Page<InvoiceConfigDTO> page = invoiceConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-configs/:id : get the "id" invoiceConfig.
     *
     * @param id the id of the invoiceConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-configs/{id}")
    @Timed
    public ResponseEntity<InvoiceConfigDTO> getInvoiceConfig(@PathVariable Long id) {
        log.debug("REST request to get InvoiceConfig : {}", id);
        Optional<InvoiceConfigDTO> invoiceConfigDTO = invoiceConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceConfigDTO);
    }

    /**
     * DELETE  /invoice-configs/:id : delete the "id" invoiceConfig.
     *
     * @param id the id of the invoiceConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceConfig(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceConfig : {}", id);
        invoiceConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

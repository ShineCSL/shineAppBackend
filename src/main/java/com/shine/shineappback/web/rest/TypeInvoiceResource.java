package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.TypeInvoiceService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.TypeInvoiceDTO;
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
 * REST controller for managing TypeInvoice.
 */
@RestController
@RequestMapping("/api")
public class TypeInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(TypeInvoiceResource.class);

    private static final String ENTITY_NAME = "typeInvoice";

    private final TypeInvoiceService typeInvoiceService;

    public TypeInvoiceResource(TypeInvoiceService typeInvoiceService) {
        this.typeInvoiceService = typeInvoiceService;
    }

    /**
     * POST  /type-invoices : Create a new typeInvoice.
     *
     * @param typeInvoiceDTO the typeInvoiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeInvoiceDTO, or with status 400 (Bad Request) if the typeInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-invoices")
    @Timed
    public ResponseEntity<TypeInvoiceDTO> createTypeInvoice(@Valid @RequestBody TypeInvoiceDTO typeInvoiceDTO) throws URISyntaxException {
        log.debug("REST request to save TypeInvoice : {}", typeInvoiceDTO);
        if (typeInvoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeInvoiceDTO result = typeInvoiceService.save(typeInvoiceDTO);
        return ResponseEntity.created(new URI("/api/type-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-invoices : Updates an existing typeInvoice.
     *
     * @param typeInvoiceDTO the typeInvoiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeInvoiceDTO,
     * or with status 400 (Bad Request) if the typeInvoiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the typeInvoiceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-invoices")
    @Timed
    public ResponseEntity<TypeInvoiceDTO> updateTypeInvoice(@Valid @RequestBody TypeInvoiceDTO typeInvoiceDTO) throws URISyntaxException {
        log.debug("REST request to update TypeInvoice : {}", typeInvoiceDTO);
        if (typeInvoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeInvoiceDTO result = typeInvoiceService.save(typeInvoiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeInvoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-invoices : get all the typeInvoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeInvoices in body
     */
    @GetMapping("/type-invoices")
    @Timed
    public ResponseEntity<List<TypeInvoiceDTO>> getAllTypeInvoices(Pageable pageable) {
        log.debug("REST request to get a page of TypeInvoices");
        Page<TypeInvoiceDTO> page = typeInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-invoices/:id : get the "id" typeInvoice.
     *
     * @param id the id of the typeInvoiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeInvoiceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/type-invoices/{id}")
    @Timed
    public ResponseEntity<TypeInvoiceDTO> getTypeInvoice(@PathVariable Long id) {
        log.debug("REST request to get TypeInvoice : {}", id);
        Optional<TypeInvoiceDTO> typeInvoiceDTO = typeInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeInvoiceDTO);
    }

    /**
     * DELETE  /type-invoices/:id : delete the "id" typeInvoice.
     *
     * @param id the id of the typeInvoiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-invoices/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeInvoice(@PathVariable Long id) {
        log.debug("REST request to delete TypeInvoice : {}", id);
        typeInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

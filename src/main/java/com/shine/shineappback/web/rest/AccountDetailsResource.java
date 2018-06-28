package com.shine.shineappback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.shine.shineappback.service.AccountDetailsService;
import com.shine.shineappback.web.rest.errors.BadRequestAlertException;
import com.shine.shineappback.web.rest.util.HeaderUtil;
import com.shine.shineappback.web.rest.util.PaginationUtil;
import com.shine.shineappback.service.dto.AccountDetailsDTO;
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
 * REST controller for managing AccountDetails.
 */
@RestController
@RequestMapping("/api")
public class AccountDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AccountDetailsResource.class);

    private static final String ENTITY_NAME = "accountDetails";

    private final AccountDetailsService accountDetailsService;

    public AccountDetailsResource(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }

    /**
     * POST  /account-details : Create a new accountDetails.
     *
     * @param accountDetailsDTO the accountDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountDetailsDTO, or with status 400 (Bad Request) if the accountDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/account-details")
    @Timed
    public ResponseEntity<AccountDetailsDTO> createAccountDetails(@Valid @RequestBody AccountDetailsDTO accountDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save AccountDetails : {}", accountDetailsDTO);
        if (accountDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountDetailsDTO result = accountDetailsService.save(accountDetailsDTO);
        return ResponseEntity.created(new URI("/api/account-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /account-details : Updates an existing accountDetails.
     *
     * @param accountDetailsDTO the accountDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountDetailsDTO,
     * or with status 400 (Bad Request) if the accountDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the accountDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/account-details")
    @Timed
    public ResponseEntity<AccountDetailsDTO> updateAccountDetails(@Valid @RequestBody AccountDetailsDTO accountDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update AccountDetails : {}", accountDetailsDTO);
        if (accountDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccountDetailsDTO result = accountDetailsService.save(accountDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-details : get all the accountDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountDetails in body
     */
    @GetMapping("/account-details")
    @Timed
    public ResponseEntity<List<AccountDetailsDTO>> getAllAccountDetails(Pageable pageable) {
        log.debug("REST request to get a page of AccountDetails");
        Page<AccountDetailsDTO> page = accountDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-details/:id : get the "id" accountDetails.
     *
     * @param id the id of the accountDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/account-details/{id}")
    @Timed
    public ResponseEntity<AccountDetailsDTO> getAccountDetails(@PathVariable Long id) {
        log.debug("REST request to get AccountDetails : {}", id);
        Optional<AccountDetailsDTO> accountDetailsDTO = accountDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountDetailsDTO);
    }

    /**
     * DELETE  /account-details/:id : delete the "id" accountDetails.
     *
     * @param id the id of the accountDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/account-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountDetails(@PathVariable Long id) {
        log.debug("REST request to delete AccountDetails : {}", id);
        accountDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

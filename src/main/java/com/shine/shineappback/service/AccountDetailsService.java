package com.shine.shineappback.service;

import com.shine.shineappback.domain.AccountDetails;
import com.shine.shineappback.repository.AccountDetailsRepository;
import com.shine.shineappback.service.dto.AccountDetailsDTO;
import com.shine.shineappback.service.mapper.AccountDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing AccountDetails.
 */
@Service
@Transactional
public class AccountDetailsService {

    private final Logger log = LoggerFactory.getLogger(AccountDetailsService.class);

    private final AccountDetailsRepository accountDetailsRepository;

    private final AccountDetailsMapper accountDetailsMapper;

    public AccountDetailsService(AccountDetailsRepository accountDetailsRepository, AccountDetailsMapper accountDetailsMapper) {
        this.accountDetailsRepository = accountDetailsRepository;
        this.accountDetailsMapper = accountDetailsMapper;
    }

    /**
     * Save a accountDetails.
     *
     * @param accountDetailsDTO the entity to save
     * @return the persisted entity
     */
    public AccountDetailsDTO save(AccountDetailsDTO accountDetailsDTO) {
        log.debug("Request to save AccountDetails : {}", accountDetailsDTO);
        AccountDetails accountDetails = accountDetailsMapper.toEntity(accountDetailsDTO);
        accountDetails = accountDetailsRepository.save(accountDetails);
        return accountDetailsMapper.toDto(accountDetails);
    }

    /**
     * Get all the accountDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AccountDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountDetails");
        return accountDetailsRepository.findAll(pageable)
            .map(accountDetailsMapper::toDto);
    }


    /**
     * Get one accountDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AccountDetailsDTO> findOne(Long id) {
        log.debug("Request to get AccountDetails : {}", id);
        return accountDetailsRepository.findById(id)
            .map(accountDetailsMapper::toDto);
    }

    /**
     * Delete the accountDetails by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccountDetails : {}", id);
        accountDetailsRepository.deleteById(id);
    }
}

package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.AccountDetails;
import com.shine.shineappback.domain.Client;
import com.shine.shineappback.domain.Invoice;
import com.shine.shineappback.domain.Currency;
import com.shine.shineappback.repository.AccountDetailsRepository;
import com.shine.shineappback.service.AccountDetailsService;
import com.shine.shineappback.service.dto.AccountDetailsDTO;
import com.shine.shineappback.service.mapper.AccountDetailsMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.AccountDetailsCriteria;
import com.shine.shineappback.service.AccountDetailsQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.shine.shineappback.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountDetailsResource REST controller.
 *
 * @see AccountDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class AccountDetailsResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;


    @Autowired
    private AccountDetailsMapper accountDetailsMapper;
    

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private AccountDetailsQueryService accountDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountDetailsMockMvc;

    private AccountDetails accountDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountDetailsResource accountDetailsResource = new AccountDetailsResource(accountDetailsService, accountDetailsQueryService);
        this.restAccountDetailsMockMvc = MockMvcBuilders.standaloneSetup(accountDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountDetails createEntity(EntityManager em) {
        AccountDetails accountDetails = new AccountDetails()
            .amount(DEFAULT_AMOUNT)
            .rate(DEFAULT_RATE)
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE);
        // Add required entity
        Currency currency = CurrencyResourceIntTest.createEntity(em);
        em.persist(currency);
        em.flush();
        accountDetails.setCurrency(currency);
        return accountDetails;
    }

    @Before
    public void initTest() {
        accountDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountDetails() throws Exception {
        int databaseSizeBeforeCreate = accountDetailsRepository.findAll().size();

        // Create the AccountDetails
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(accountDetails);
        restAccountDetailsMockMvc.perform(post("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountDetails in the database
        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        AccountDetails testAccountDetails = accountDetailsList.get(accountDetailsList.size() - 1);
        assertThat(testAccountDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAccountDetails.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testAccountDetails.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testAccountDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccountDetails.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createAccountDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountDetailsRepository.findAll().size();

        // Create the AccountDetails with an existing ID
        accountDetails.setId(1L);
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(accountDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountDetailsMockMvc.perform(post("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccountDetails in the database
        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountDetailsRepository.findAll().size();
        // set the field null
        accountDetails.setAmount(null);

        // Create the AccountDetails, which fails.
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(accountDetails);

        restAccountDetailsMockMvc.perform(post("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountDetailsRepository.findAll().size();
        // set the field null
        accountDetails.setLabel(null);

        // Create the AccountDetails, which fails.
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(accountDetails);

        restAccountDetailsMockMvc.perform(post("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountDetailsRepository.findAll().size();
        // set the field null
        accountDetails.setType(null);

        // Create the AccountDetails, which fails.
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(accountDetails);

        restAccountDetailsMockMvc.perform(post("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountDetails() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList
        restAccountDetailsMockMvc.perform(get("/api/account-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getAccountDetails() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get the accountDetails
        restAccountDetailsMockMvc.perform(get("/api/account-details/{id}", accountDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountDetails.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where amount equals to DEFAULT_AMOUNT
        defaultAccountDetailsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the accountDetailsList where amount equals to UPDATED_AMOUNT
        defaultAccountDetailsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultAccountDetailsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the accountDetailsList where amount equals to UPDATED_AMOUNT
        defaultAccountDetailsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where amount is not null
        defaultAccountDetailsShouldBeFound("amount.specified=true");

        // Get all the accountDetailsList where amount is null
        defaultAccountDetailsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where rate equals to DEFAULT_RATE
        defaultAccountDetailsShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the accountDetailsList where rate equals to UPDATED_RATE
        defaultAccountDetailsShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByRateIsInShouldWork() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultAccountDetailsShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the accountDetailsList where rate equals to UPDATED_RATE
        defaultAccountDetailsShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where rate is not null
        defaultAccountDetailsShouldBeFound("rate.specified=true");

        // Get all the accountDetailsList where rate is null
        defaultAccountDetailsShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where label equals to DEFAULT_LABEL
        defaultAccountDetailsShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the accountDetailsList where label equals to UPDATED_LABEL
        defaultAccountDetailsShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultAccountDetailsShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the accountDetailsList where label equals to UPDATED_LABEL
        defaultAccountDetailsShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where label is not null
        defaultAccountDetailsShouldBeFound("label.specified=true");

        // Get all the accountDetailsList where label is null
        defaultAccountDetailsShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultAccountDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the accountDetailsList where description equals to UPDATED_DESCRIPTION
        defaultAccountDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAccountDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the accountDetailsList where description equals to UPDATED_DESCRIPTION
        defaultAccountDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where description is not null
        defaultAccountDetailsShouldBeFound("description.specified=true");

        // Get all the accountDetailsList where description is null
        defaultAccountDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where type equals to DEFAULT_TYPE
        defaultAccountDetailsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the accountDetailsList where type equals to UPDATED_TYPE
        defaultAccountDetailsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAccountDetailsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the accountDetailsList where type equals to UPDATED_TYPE
        defaultAccountDetailsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        // Get all the accountDetailsList where type is not null
        defaultAccountDetailsShouldBeFound("type.specified=true");

        // Get all the accountDetailsList where type is null
        defaultAccountDetailsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllAccountDetailsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        accountDetails.setClient(client);
        accountDetailsRepository.saveAndFlush(accountDetails);
        Long clientId = client.getId();

        // Get all the accountDetailsList where client equals to clientId
        defaultAccountDetailsShouldBeFound("clientId.equals=" + clientId);

        // Get all the accountDetailsList where client equals to clientId + 1
        defaultAccountDetailsShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllAccountDetailsByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        accountDetails.setInvoice(invoice);
        accountDetailsRepository.saveAndFlush(accountDetails);
        Long invoiceId = invoice.getId();

        // Get all the accountDetailsList where invoice equals to invoiceId
        defaultAccountDetailsShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the accountDetailsList where invoice equals to invoiceId + 1
        defaultAccountDetailsShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }


    @Test
    @Transactional
    public void getAllAccountDetailsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        Currency currency = CurrencyResourceIntTest.createEntity(em);
        em.persist(currency);
        em.flush();
        accountDetails.setCurrency(currency);
        accountDetailsRepository.saveAndFlush(accountDetails);
        Long currencyId = currency.getId();

        // Get all the accountDetailsList where currency equals to currencyId
        defaultAccountDetailsShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the accountDetailsList where currency equals to currencyId + 1
        defaultAccountDetailsShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAccountDetailsShouldBeFound(String filter) throws Exception {
        restAccountDetailsMockMvc.perform(get("/api/account-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAccountDetailsShouldNotBeFound(String filter) throws Exception {
        restAccountDetailsMockMvc.perform(get("/api/account-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingAccountDetails() throws Exception {
        // Get the accountDetails
        restAccountDetailsMockMvc.perform(get("/api/account-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountDetails() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        int databaseSizeBeforeUpdate = accountDetailsRepository.findAll().size();

        // Update the accountDetails
        AccountDetails updatedAccountDetails = accountDetailsRepository.findById(accountDetails.getId()).get();
        // Disconnect from session so that the updates on updatedAccountDetails are not directly saved in db
        em.detach(updatedAccountDetails);
        updatedAccountDetails
            .amount(UPDATED_AMOUNT)
            .rate(UPDATED_RATE)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE);
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(updatedAccountDetails);

        restAccountDetailsMockMvc.perform(put("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the AccountDetails in the database
        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeUpdate);
        AccountDetails testAccountDetails = accountDetailsList.get(accountDetailsList.size() - 1);
        assertThat(testAccountDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAccountDetails.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testAccountDetails.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testAccountDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccountDetails.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountDetails() throws Exception {
        int databaseSizeBeforeUpdate = accountDetailsRepository.findAll().size();

        // Create the AccountDetails
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(accountDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountDetailsMockMvc.perform(put("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccountDetails in the database
        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAccountDetails() throws Exception {
        // Initialize the database
        accountDetailsRepository.saveAndFlush(accountDetails);

        int databaseSizeBeforeDelete = accountDetailsRepository.findAll().size();

        // Get the accountDetails
        restAccountDetailsMockMvc.perform(delete("/api/account-details/{id}", accountDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountDetails.class);
        AccountDetails accountDetails1 = new AccountDetails();
        accountDetails1.setId(1L);
        AccountDetails accountDetails2 = new AccountDetails();
        accountDetails2.setId(accountDetails1.getId());
        assertThat(accountDetails1).isEqualTo(accountDetails2);
        accountDetails2.setId(2L);
        assertThat(accountDetails1).isNotEqualTo(accountDetails2);
        accountDetails1.setId(null);
        assertThat(accountDetails1).isNotEqualTo(accountDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountDetailsDTO.class);
        AccountDetailsDTO accountDetailsDTO1 = new AccountDetailsDTO();
        accountDetailsDTO1.setId(1L);
        AccountDetailsDTO accountDetailsDTO2 = new AccountDetailsDTO();
        assertThat(accountDetailsDTO1).isNotEqualTo(accountDetailsDTO2);
        accountDetailsDTO2.setId(accountDetailsDTO1.getId());
        assertThat(accountDetailsDTO1).isEqualTo(accountDetailsDTO2);
        accountDetailsDTO2.setId(2L);
        assertThat(accountDetailsDTO1).isNotEqualTo(accountDetailsDTO2);
        accountDetailsDTO1.setId(null);
        assertThat(accountDetailsDTO1).isNotEqualTo(accountDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(accountDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(accountDetailsMapper.fromId(null)).isNull();
    }
}

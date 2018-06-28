package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.AccountDetails;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.Currency;
import com.shine.shineappback.repository.AccountDetailsRepository;
import com.shine.shineappback.service.AccountDetailsService;
import com.shine.shineappback.service.dto.AccountDetailsDTO;
import com.shine.shineappback.service.mapper.AccountDetailsMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.shine.shineappback.web.rest.TestUtil.sameInstant;
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

    private static final String DEFAULT_CODE = "P-/%]";
    private static final String UPDATED_CODE = "S-_/%";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_MODIFICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;


    @Autowired
    private AccountDetailsMapper accountDetailsMapper;
    

    @Autowired
    private AccountDetailsService accountDetailsService;

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
        final AccountDetailsResource accountDetailsResource = new AccountDetailsResource(accountDetailsService);
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
            .code(DEFAULT_CODE)
            .amount(DEFAULT_AMOUNT)
            .type(DEFAULT_TYPE)
            .rate(DEFAULT_RATE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        accountDetails.setUserCreation(user);
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
        assertThat(testAccountDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAccountDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAccountDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAccountDetails.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testAccountDetails.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAccountDetails.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
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
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountDetailsRepository.findAll().size();
        // set the field null
        accountDetails.setCode(null);

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
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountDetailsRepository.findAll().size();
        // set the field null
        accountDetails.setDateCreation(null);

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
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(sameInstant(DEFAULT_DATE_MODIFICATION))));
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
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.dateModification").value(sameInstant(DEFAULT_DATE_MODIFICATION)));
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
            .code(UPDATED_CODE)
            .amount(UPDATED_AMOUNT)
            .type(UPDATED_TYPE)
            .rate(UPDATED_RATE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        AccountDetailsDTO accountDetailsDTO = accountDetailsMapper.toDto(updatedAccountDetails);

        restAccountDetailsMockMvc.perform(put("/api/account-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the AccountDetails in the database
        List<AccountDetails> accountDetailsList = accountDetailsRepository.findAll();
        assertThat(accountDetailsList).hasSize(databaseSizeBeforeUpdate);
        AccountDetails testAccountDetails = accountDetailsList.get(accountDetailsList.size() - 1);
        assertThat(testAccountDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAccountDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAccountDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccountDetails.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testAccountDetails.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAccountDetails.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
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

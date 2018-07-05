package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.InvoiceValidation;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.InvoiceValidationRepository;
import com.shine.shineappback.service.InvoiceValidationService;
import com.shine.shineappback.service.dto.InvoiceValidationDTO;
import com.shine.shineappback.service.mapper.InvoiceValidationMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.shine.shineappback.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoiceValidationResource REST controller.
 *
 * @see InvoiceValidationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class InvoiceValidationResourceIntTest {

    private static final Boolean DEFAULT_VALIDATED = false;
    private static final Boolean UPDATED_VALIDATED = true;

    private static final LocalDate DEFAULT_DATE_INVOICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INVOICE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private InvoiceValidationRepository invoiceValidationRepository;


    @Autowired
    private InvoiceValidationMapper invoiceValidationMapper;
    

    @Autowired
    private InvoiceValidationService invoiceValidationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceValidationMockMvc;

    private InvoiceValidation invoiceValidation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceValidationResource invoiceValidationResource = new InvoiceValidationResource(invoiceValidationService);
        this.restInvoiceValidationMockMvc = MockMvcBuilders.standaloneSetup(invoiceValidationResource)
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
    public static InvoiceValidation createEntity(EntityManager em) {
        InvoiceValidation invoiceValidation = new InvoiceValidation()
            .validated(DEFAULT_VALIDATED)
            .dateInvoice(DEFAULT_DATE_INVOICE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        invoiceValidation.setUser(user);
        return invoiceValidation;
    }

    @Before
    public void initTest() {
        invoiceValidation = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceValidation() throws Exception {
        int databaseSizeBeforeCreate = invoiceValidationRepository.findAll().size();

        // Create the InvoiceValidation
        InvoiceValidationDTO invoiceValidationDTO = invoiceValidationMapper.toDto(invoiceValidation);
        restInvoiceValidationMockMvc.perform(post("/api/invoice-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceValidationDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceValidation in the database
        List<InvoiceValidation> invoiceValidationList = invoiceValidationRepository.findAll();
        assertThat(invoiceValidationList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceValidation testInvoiceValidation = invoiceValidationList.get(invoiceValidationList.size() - 1);
        assertThat(testInvoiceValidation.isValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testInvoiceValidation.getDateInvoice()).isEqualTo(DEFAULT_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void createInvoiceValidationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceValidationRepository.findAll().size();

        // Create the InvoiceValidation with an existing ID
        invoiceValidation.setId(1L);
        InvoiceValidationDTO invoiceValidationDTO = invoiceValidationMapper.toDto(invoiceValidation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceValidationMockMvc.perform(post("/api/invoice-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceValidationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceValidation in the database
        List<InvoiceValidation> invoiceValidationList = invoiceValidationRepository.findAll();
        assertThat(invoiceValidationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateInvoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceValidationRepository.findAll().size();
        // set the field null
        invoiceValidation.setDateInvoice(null);

        // Create the InvoiceValidation, which fails.
        InvoiceValidationDTO invoiceValidationDTO = invoiceValidationMapper.toDto(invoiceValidation);

        restInvoiceValidationMockMvc.perform(post("/api/invoice-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceValidationDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceValidation> invoiceValidationList = invoiceValidationRepository.findAll();
        assertThat(invoiceValidationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceValidations() throws Exception {
        // Initialize the database
        invoiceValidationRepository.saveAndFlush(invoiceValidation);

        // Get all the invoiceValidationList
        restInvoiceValidationMockMvc.perform(get("/api/invoice-validations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceValidation.getId().intValue())))
            .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateInvoice").value(hasItem(DEFAULT_DATE_INVOICE.toString())));
    }
    

    @Test
    @Transactional
    public void getInvoiceValidation() throws Exception {
        // Initialize the database
        invoiceValidationRepository.saveAndFlush(invoiceValidation);

        // Get the invoiceValidation
        restInvoiceValidationMockMvc.perform(get("/api/invoice-validations/{id}", invoiceValidation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceValidation.getId().intValue()))
            .andExpect(jsonPath("$.validated").value(DEFAULT_VALIDATED.booleanValue()))
            .andExpect(jsonPath("$.dateInvoice").value(DEFAULT_DATE_INVOICE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingInvoiceValidation() throws Exception {
        // Get the invoiceValidation
        restInvoiceValidationMockMvc.perform(get("/api/invoice-validations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceValidation() throws Exception {
        // Initialize the database
        invoiceValidationRepository.saveAndFlush(invoiceValidation);

        int databaseSizeBeforeUpdate = invoiceValidationRepository.findAll().size();

        // Update the invoiceValidation
        InvoiceValidation updatedInvoiceValidation = invoiceValidationRepository.findById(invoiceValidation.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceValidation are not directly saved in db
        em.detach(updatedInvoiceValidation);
        updatedInvoiceValidation
            .validated(UPDATED_VALIDATED)
            .dateInvoice(UPDATED_DATE_INVOICE);
        InvoiceValidationDTO invoiceValidationDTO = invoiceValidationMapper.toDto(updatedInvoiceValidation);

        restInvoiceValidationMockMvc.perform(put("/api/invoice-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceValidationDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceValidation in the database
        List<InvoiceValidation> invoiceValidationList = invoiceValidationRepository.findAll();
        assertThat(invoiceValidationList).hasSize(databaseSizeBeforeUpdate);
        InvoiceValidation testInvoiceValidation = invoiceValidationList.get(invoiceValidationList.size() - 1);
        assertThat(testInvoiceValidation.isValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testInvoiceValidation.getDateInvoice()).isEqualTo(UPDATED_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceValidation() throws Exception {
        int databaseSizeBeforeUpdate = invoiceValidationRepository.findAll().size();

        // Create the InvoiceValidation
        InvoiceValidationDTO invoiceValidationDTO = invoiceValidationMapper.toDto(invoiceValidation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceValidationMockMvc.perform(put("/api/invoice-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceValidationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceValidation in the database
        List<InvoiceValidation> invoiceValidationList = invoiceValidationRepository.findAll();
        assertThat(invoiceValidationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceValidation() throws Exception {
        // Initialize the database
        invoiceValidationRepository.saveAndFlush(invoiceValidation);

        int databaseSizeBeforeDelete = invoiceValidationRepository.findAll().size();

        // Get the invoiceValidation
        restInvoiceValidationMockMvc.perform(delete("/api/invoice-validations/{id}", invoiceValidation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceValidation> invoiceValidationList = invoiceValidationRepository.findAll();
        assertThat(invoiceValidationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceValidation.class);
        InvoiceValidation invoiceValidation1 = new InvoiceValidation();
        invoiceValidation1.setId(1L);
        InvoiceValidation invoiceValidation2 = new InvoiceValidation();
        invoiceValidation2.setId(invoiceValidation1.getId());
        assertThat(invoiceValidation1).isEqualTo(invoiceValidation2);
        invoiceValidation2.setId(2L);
        assertThat(invoiceValidation1).isNotEqualTo(invoiceValidation2);
        invoiceValidation1.setId(null);
        assertThat(invoiceValidation1).isNotEqualTo(invoiceValidation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceValidationDTO.class);
        InvoiceValidationDTO invoiceValidationDTO1 = new InvoiceValidationDTO();
        invoiceValidationDTO1.setId(1L);
        InvoiceValidationDTO invoiceValidationDTO2 = new InvoiceValidationDTO();
        assertThat(invoiceValidationDTO1).isNotEqualTo(invoiceValidationDTO2);
        invoiceValidationDTO2.setId(invoiceValidationDTO1.getId());
        assertThat(invoiceValidationDTO1).isEqualTo(invoiceValidationDTO2);
        invoiceValidationDTO2.setId(2L);
        assertThat(invoiceValidationDTO1).isNotEqualTo(invoiceValidationDTO2);
        invoiceValidationDTO1.setId(null);
        assertThat(invoiceValidationDTO1).isNotEqualTo(invoiceValidationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceValidationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceValidationMapper.fromId(null)).isNull();
    }
}

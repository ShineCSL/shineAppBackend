package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.InvoiceSubmission;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.InvoiceSubmissionRepository;
import com.shine.shineappback.service.InvoiceSubmissionService;
import com.shine.shineappback.service.dto.InvoiceSubmissionDTO;
import com.shine.shineappback.service.mapper.InvoiceSubmissionMapper;
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
 * Test class for the InvoiceSubmissionResource REST controller.
 *
 * @see InvoiceSubmissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class InvoiceSubmissionResourceIntTest {

    private static final Boolean DEFAULT_SUBMITTED = false;
    private static final Boolean UPDATED_SUBMITTED = true;

    private static final LocalDate DEFAULT_DATE_INVOICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INVOICE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private InvoiceSubmissionRepository invoiceSubmissionRepository;


    @Autowired
    private InvoiceSubmissionMapper invoiceSubmissionMapper;
    

    @Autowired
    private InvoiceSubmissionService invoiceSubmissionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceSubmissionMockMvc;

    private InvoiceSubmission invoiceSubmission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceSubmissionResource invoiceSubmissionResource = new InvoiceSubmissionResource(invoiceSubmissionService);
        this.restInvoiceSubmissionMockMvc = MockMvcBuilders.standaloneSetup(invoiceSubmissionResource)
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
    public static InvoiceSubmission createEntity(EntityManager em) {
        InvoiceSubmission invoiceSubmission = new InvoiceSubmission()
            .submitted(DEFAULT_SUBMITTED)
            .dateInvoice(DEFAULT_DATE_INVOICE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        invoiceSubmission.setUser(user);
        return invoiceSubmission;
    }

    @Before
    public void initTest() {
        invoiceSubmission = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceSubmission() throws Exception {
        int databaseSizeBeforeCreate = invoiceSubmissionRepository.findAll().size();

        // Create the InvoiceSubmission
        InvoiceSubmissionDTO invoiceSubmissionDTO = invoiceSubmissionMapper.toDto(invoiceSubmission);
        restInvoiceSubmissionMockMvc.perform(post("/api/invoice-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceSubmissionDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceSubmission in the database
        List<InvoiceSubmission> invoiceSubmissionList = invoiceSubmissionRepository.findAll();
        assertThat(invoiceSubmissionList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceSubmission testInvoiceSubmission = invoiceSubmissionList.get(invoiceSubmissionList.size() - 1);
        assertThat(testInvoiceSubmission.isSubmitted()).isEqualTo(DEFAULT_SUBMITTED);
        assertThat(testInvoiceSubmission.getDateInvoice()).isEqualTo(DEFAULT_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void createInvoiceSubmissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceSubmissionRepository.findAll().size();

        // Create the InvoiceSubmission with an existing ID
        invoiceSubmission.setId(1L);
        InvoiceSubmissionDTO invoiceSubmissionDTO = invoiceSubmissionMapper.toDto(invoiceSubmission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceSubmissionMockMvc.perform(post("/api/invoice-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceSubmissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSubmission in the database
        List<InvoiceSubmission> invoiceSubmissionList = invoiceSubmissionRepository.findAll();
        assertThat(invoiceSubmissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateInvoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceSubmissionRepository.findAll().size();
        // set the field null
        invoiceSubmission.setDateInvoice(null);

        // Create the InvoiceSubmission, which fails.
        InvoiceSubmissionDTO invoiceSubmissionDTO = invoiceSubmissionMapper.toDto(invoiceSubmission);

        restInvoiceSubmissionMockMvc.perform(post("/api/invoice-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceSubmissionDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceSubmission> invoiceSubmissionList = invoiceSubmissionRepository.findAll();
        assertThat(invoiceSubmissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceSubmissions() throws Exception {
        // Initialize the database
        invoiceSubmissionRepository.saveAndFlush(invoiceSubmission);

        // Get all the invoiceSubmissionList
        restInvoiceSubmissionMockMvc.perform(get("/api/invoice-submissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceSubmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].submitted").value(hasItem(DEFAULT_SUBMITTED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateInvoice").value(hasItem(DEFAULT_DATE_INVOICE.toString())));
    }
    

    @Test
    @Transactional
    public void getInvoiceSubmission() throws Exception {
        // Initialize the database
        invoiceSubmissionRepository.saveAndFlush(invoiceSubmission);

        // Get the invoiceSubmission
        restInvoiceSubmissionMockMvc.perform(get("/api/invoice-submissions/{id}", invoiceSubmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceSubmission.getId().intValue()))
            .andExpect(jsonPath("$.submitted").value(DEFAULT_SUBMITTED.booleanValue()))
            .andExpect(jsonPath("$.dateInvoice").value(DEFAULT_DATE_INVOICE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingInvoiceSubmission() throws Exception {
        // Get the invoiceSubmission
        restInvoiceSubmissionMockMvc.perform(get("/api/invoice-submissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceSubmission() throws Exception {
        // Initialize the database
        invoiceSubmissionRepository.saveAndFlush(invoiceSubmission);

        int databaseSizeBeforeUpdate = invoiceSubmissionRepository.findAll().size();

        // Update the invoiceSubmission
        InvoiceSubmission updatedInvoiceSubmission = invoiceSubmissionRepository.findById(invoiceSubmission.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceSubmission are not directly saved in db
        em.detach(updatedInvoiceSubmission);
        updatedInvoiceSubmission
            .submitted(UPDATED_SUBMITTED)
            .dateInvoice(UPDATED_DATE_INVOICE);
        InvoiceSubmissionDTO invoiceSubmissionDTO = invoiceSubmissionMapper.toDto(updatedInvoiceSubmission);

        restInvoiceSubmissionMockMvc.perform(put("/api/invoice-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceSubmissionDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceSubmission in the database
        List<InvoiceSubmission> invoiceSubmissionList = invoiceSubmissionRepository.findAll();
        assertThat(invoiceSubmissionList).hasSize(databaseSizeBeforeUpdate);
        InvoiceSubmission testInvoiceSubmission = invoiceSubmissionList.get(invoiceSubmissionList.size() - 1);
        assertThat(testInvoiceSubmission.isSubmitted()).isEqualTo(UPDATED_SUBMITTED);
        assertThat(testInvoiceSubmission.getDateInvoice()).isEqualTo(UPDATED_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceSubmission() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSubmissionRepository.findAll().size();

        // Create the InvoiceSubmission
        InvoiceSubmissionDTO invoiceSubmissionDTO = invoiceSubmissionMapper.toDto(invoiceSubmission);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceSubmissionMockMvc.perform(put("/api/invoice-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceSubmissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSubmission in the database
        List<InvoiceSubmission> invoiceSubmissionList = invoiceSubmissionRepository.findAll();
        assertThat(invoiceSubmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceSubmission() throws Exception {
        // Initialize the database
        invoiceSubmissionRepository.saveAndFlush(invoiceSubmission);

        int databaseSizeBeforeDelete = invoiceSubmissionRepository.findAll().size();

        // Get the invoiceSubmission
        restInvoiceSubmissionMockMvc.perform(delete("/api/invoice-submissions/{id}", invoiceSubmission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceSubmission> invoiceSubmissionList = invoiceSubmissionRepository.findAll();
        assertThat(invoiceSubmissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSubmission.class);
        InvoiceSubmission invoiceSubmission1 = new InvoiceSubmission();
        invoiceSubmission1.setId(1L);
        InvoiceSubmission invoiceSubmission2 = new InvoiceSubmission();
        invoiceSubmission2.setId(invoiceSubmission1.getId());
        assertThat(invoiceSubmission1).isEqualTo(invoiceSubmission2);
        invoiceSubmission2.setId(2L);
        assertThat(invoiceSubmission1).isNotEqualTo(invoiceSubmission2);
        invoiceSubmission1.setId(null);
        assertThat(invoiceSubmission1).isNotEqualTo(invoiceSubmission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSubmissionDTO.class);
        InvoiceSubmissionDTO invoiceSubmissionDTO1 = new InvoiceSubmissionDTO();
        invoiceSubmissionDTO1.setId(1L);
        InvoiceSubmissionDTO invoiceSubmissionDTO2 = new InvoiceSubmissionDTO();
        assertThat(invoiceSubmissionDTO1).isNotEqualTo(invoiceSubmissionDTO2);
        invoiceSubmissionDTO2.setId(invoiceSubmissionDTO1.getId());
        assertThat(invoiceSubmissionDTO1).isEqualTo(invoiceSubmissionDTO2);
        invoiceSubmissionDTO2.setId(2L);
        assertThat(invoiceSubmissionDTO1).isNotEqualTo(invoiceSubmissionDTO2);
        invoiceSubmissionDTO1.setId(null);
        assertThat(invoiceSubmissionDTO1).isNotEqualTo(invoiceSubmissionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceSubmissionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceSubmissionMapper.fromId(null)).isNull();
    }
}

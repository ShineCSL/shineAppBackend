package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.InvoiceRejection;
import com.shine.shineappback.repository.InvoiceRejectionRepository;
import com.shine.shineappback.service.InvoiceRejectionService;
import com.shine.shineappback.service.dto.InvoiceRejectionDTO;
import com.shine.shineappback.service.mapper.InvoiceRejectionMapper;
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
import java.util.List;


import static com.shine.shineappback.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InvoiceRejectionResource REST controller.
 *
 * @see InvoiceRejectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class InvoiceRejectionResourceIntTest {

    private static final Boolean DEFAULT_REJECTED = false;
    private static final Boolean UPDATED_REJECTED = true;

    @Autowired
    private InvoiceRejectionRepository invoiceRejectionRepository;


    @Autowired
    private InvoiceRejectionMapper invoiceRejectionMapper;
    

    @Autowired
    private InvoiceRejectionService invoiceRejectionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceRejectionMockMvc;

    private InvoiceRejection invoiceRejection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceRejectionResource invoiceRejectionResource = new InvoiceRejectionResource(invoiceRejectionService);
        this.restInvoiceRejectionMockMvc = MockMvcBuilders.standaloneSetup(invoiceRejectionResource)
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
    public static InvoiceRejection createEntity(EntityManager em) {
        InvoiceRejection invoiceRejection = new InvoiceRejection()
            .rejected(DEFAULT_REJECTED);
        return invoiceRejection;
    }

    @Before
    public void initTest() {
        invoiceRejection = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceRejection() throws Exception {
        int databaseSizeBeforeCreate = invoiceRejectionRepository.findAll().size();

        // Create the InvoiceRejection
        InvoiceRejectionDTO invoiceRejectionDTO = invoiceRejectionMapper.toDto(invoiceRejection);
        restInvoiceRejectionMockMvc.perform(post("/api/invoice-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceRejectionDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceRejection in the database
        List<InvoiceRejection> invoiceRejectionList = invoiceRejectionRepository.findAll();
        assertThat(invoiceRejectionList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceRejection testInvoiceRejection = invoiceRejectionList.get(invoiceRejectionList.size() - 1);
        assertThat(testInvoiceRejection.isRejected()).isEqualTo(DEFAULT_REJECTED);
    }

    @Test
    @Transactional
    public void createInvoiceRejectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRejectionRepository.findAll().size();

        // Create the InvoiceRejection with an existing ID
        invoiceRejection.setId(1L);
        InvoiceRejectionDTO invoiceRejectionDTO = invoiceRejectionMapper.toDto(invoiceRejection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceRejectionMockMvc.perform(post("/api/invoice-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceRejectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRejection in the database
        List<InvoiceRejection> invoiceRejectionList = invoiceRejectionRepository.findAll();
        assertThat(invoiceRejectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvoiceRejections() throws Exception {
        // Initialize the database
        invoiceRejectionRepository.saveAndFlush(invoiceRejection);

        // Get all the invoiceRejectionList
        restInvoiceRejectionMockMvc.perform(get("/api/invoice-rejections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceRejection.getId().intValue())))
            .andExpect(jsonPath("$.[*].rejected").value(hasItem(DEFAULT_REJECTED.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getInvoiceRejection() throws Exception {
        // Initialize the database
        invoiceRejectionRepository.saveAndFlush(invoiceRejection);

        // Get the invoiceRejection
        restInvoiceRejectionMockMvc.perform(get("/api/invoice-rejections/{id}", invoiceRejection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceRejection.getId().intValue()))
            .andExpect(jsonPath("$.rejected").value(DEFAULT_REJECTED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingInvoiceRejection() throws Exception {
        // Get the invoiceRejection
        restInvoiceRejectionMockMvc.perform(get("/api/invoice-rejections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceRejection() throws Exception {
        // Initialize the database
        invoiceRejectionRepository.saveAndFlush(invoiceRejection);

        int databaseSizeBeforeUpdate = invoiceRejectionRepository.findAll().size();

        // Update the invoiceRejection
        InvoiceRejection updatedInvoiceRejection = invoiceRejectionRepository.findById(invoiceRejection.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceRejection are not directly saved in db
        em.detach(updatedInvoiceRejection);
        updatedInvoiceRejection
            .rejected(UPDATED_REJECTED);
        InvoiceRejectionDTO invoiceRejectionDTO = invoiceRejectionMapper.toDto(updatedInvoiceRejection);

        restInvoiceRejectionMockMvc.perform(put("/api/invoice-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceRejectionDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceRejection in the database
        List<InvoiceRejection> invoiceRejectionList = invoiceRejectionRepository.findAll();
        assertThat(invoiceRejectionList).hasSize(databaseSizeBeforeUpdate);
        InvoiceRejection testInvoiceRejection = invoiceRejectionList.get(invoiceRejectionList.size() - 1);
        assertThat(testInvoiceRejection.isRejected()).isEqualTo(UPDATED_REJECTED);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceRejection() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRejectionRepository.findAll().size();

        // Create the InvoiceRejection
        InvoiceRejectionDTO invoiceRejectionDTO = invoiceRejectionMapper.toDto(invoiceRejection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceRejectionMockMvc.perform(put("/api/invoice-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceRejectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceRejection in the database
        List<InvoiceRejection> invoiceRejectionList = invoiceRejectionRepository.findAll();
        assertThat(invoiceRejectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceRejection() throws Exception {
        // Initialize the database
        invoiceRejectionRepository.saveAndFlush(invoiceRejection);

        int databaseSizeBeforeDelete = invoiceRejectionRepository.findAll().size();

        // Get the invoiceRejection
        restInvoiceRejectionMockMvc.perform(delete("/api/invoice-rejections/{id}", invoiceRejection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceRejection> invoiceRejectionList = invoiceRejectionRepository.findAll();
        assertThat(invoiceRejectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceRejection.class);
        InvoiceRejection invoiceRejection1 = new InvoiceRejection();
        invoiceRejection1.setId(1L);
        InvoiceRejection invoiceRejection2 = new InvoiceRejection();
        invoiceRejection2.setId(invoiceRejection1.getId());
        assertThat(invoiceRejection1).isEqualTo(invoiceRejection2);
        invoiceRejection2.setId(2L);
        assertThat(invoiceRejection1).isNotEqualTo(invoiceRejection2);
        invoiceRejection1.setId(null);
        assertThat(invoiceRejection1).isNotEqualTo(invoiceRejection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceRejectionDTO.class);
        InvoiceRejectionDTO invoiceRejectionDTO1 = new InvoiceRejectionDTO();
        invoiceRejectionDTO1.setId(1L);
        InvoiceRejectionDTO invoiceRejectionDTO2 = new InvoiceRejectionDTO();
        assertThat(invoiceRejectionDTO1).isNotEqualTo(invoiceRejectionDTO2);
        invoiceRejectionDTO2.setId(invoiceRejectionDTO1.getId());
        assertThat(invoiceRejectionDTO1).isEqualTo(invoiceRejectionDTO2);
        invoiceRejectionDTO2.setId(2L);
        assertThat(invoiceRejectionDTO1).isNotEqualTo(invoiceRejectionDTO2);
        invoiceRejectionDTO1.setId(null);
        assertThat(invoiceRejectionDTO1).isNotEqualTo(invoiceRejectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceRejectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceRejectionMapper.fromId(null)).isNull();
    }
}

package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.InvoiceConfig;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.InvoiceConfigRepository;
import com.shine.shineappback.service.InvoiceConfigService;
import com.shine.shineappback.service.dto.InvoiceConfigDTO;
import com.shine.shineappback.service.mapper.InvoiceConfigMapper;
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
 * Test class for the InvoiceConfigResource REST controller.
 *
 * @see InvoiceConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class InvoiceConfigResourceIntTest {

    @Autowired
    private InvoiceConfigRepository invoiceConfigRepository;


    @Autowired
    private InvoiceConfigMapper invoiceConfigMapper;
    

    @Autowired
    private InvoiceConfigService invoiceConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceConfigMockMvc;

    private InvoiceConfig invoiceConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceConfigResource invoiceConfigResource = new InvoiceConfigResource(invoiceConfigService);
        this.restInvoiceConfigMockMvc = MockMvcBuilders.standaloneSetup(invoiceConfigResource)
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
    public static InvoiceConfig createEntity(EntityManager em) {
        InvoiceConfig invoiceConfig = new InvoiceConfig();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        invoiceConfig.setUser(user);
        return invoiceConfig;
    }

    @Before
    public void initTest() {
        invoiceConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceConfig() throws Exception {
        int databaseSizeBeforeCreate = invoiceConfigRepository.findAll().size();

        // Create the InvoiceConfig
        InvoiceConfigDTO invoiceConfigDTO = invoiceConfigMapper.toDto(invoiceConfig);
        restInvoiceConfigMockMvc.perform(post("/api/invoice-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceConfig in the database
        List<InvoiceConfig> invoiceConfigList = invoiceConfigRepository.findAll();
        assertThat(invoiceConfigList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceConfig testInvoiceConfig = invoiceConfigList.get(invoiceConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void createInvoiceConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceConfigRepository.findAll().size();

        // Create the InvoiceConfig with an existing ID
        invoiceConfig.setId(1L);
        InvoiceConfigDTO invoiceConfigDTO = invoiceConfigMapper.toDto(invoiceConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceConfigMockMvc.perform(post("/api/invoice-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceConfig in the database
        List<InvoiceConfig> invoiceConfigList = invoiceConfigRepository.findAll();
        assertThat(invoiceConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvoiceConfigs() throws Exception {
        // Initialize the database
        invoiceConfigRepository.saveAndFlush(invoiceConfig);

        // Get all the invoiceConfigList
        restInvoiceConfigMockMvc.perform(get("/api/invoice-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceConfig.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getInvoiceConfig() throws Exception {
        // Initialize the database
        invoiceConfigRepository.saveAndFlush(invoiceConfig);

        // Get the invoiceConfig
        restInvoiceConfigMockMvc.perform(get("/api/invoice-configs/{id}", invoiceConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceConfig.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingInvoiceConfig() throws Exception {
        // Get the invoiceConfig
        restInvoiceConfigMockMvc.perform(get("/api/invoice-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceConfig() throws Exception {
        // Initialize the database
        invoiceConfigRepository.saveAndFlush(invoiceConfig);

        int databaseSizeBeforeUpdate = invoiceConfigRepository.findAll().size();

        // Update the invoiceConfig
        InvoiceConfig updatedInvoiceConfig = invoiceConfigRepository.findById(invoiceConfig.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceConfig are not directly saved in db
        em.detach(updatedInvoiceConfig);
        InvoiceConfigDTO invoiceConfigDTO = invoiceConfigMapper.toDto(updatedInvoiceConfig);

        restInvoiceConfigMockMvc.perform(put("/api/invoice-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceConfigDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceConfig in the database
        List<InvoiceConfig> invoiceConfigList = invoiceConfigRepository.findAll();
        assertThat(invoiceConfigList).hasSize(databaseSizeBeforeUpdate);
        InvoiceConfig testInvoiceConfig = invoiceConfigList.get(invoiceConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceConfig() throws Exception {
        int databaseSizeBeforeUpdate = invoiceConfigRepository.findAll().size();

        // Create the InvoiceConfig
        InvoiceConfigDTO invoiceConfigDTO = invoiceConfigMapper.toDto(invoiceConfig);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceConfigMockMvc.perform(put("/api/invoice-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceConfig in the database
        List<InvoiceConfig> invoiceConfigList = invoiceConfigRepository.findAll();
        assertThat(invoiceConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceConfig() throws Exception {
        // Initialize the database
        invoiceConfigRepository.saveAndFlush(invoiceConfig);

        int databaseSizeBeforeDelete = invoiceConfigRepository.findAll().size();

        // Get the invoiceConfig
        restInvoiceConfigMockMvc.perform(delete("/api/invoice-configs/{id}", invoiceConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceConfig> invoiceConfigList = invoiceConfigRepository.findAll();
        assertThat(invoiceConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceConfig.class);
        InvoiceConfig invoiceConfig1 = new InvoiceConfig();
        invoiceConfig1.setId(1L);
        InvoiceConfig invoiceConfig2 = new InvoiceConfig();
        invoiceConfig2.setId(invoiceConfig1.getId());
        assertThat(invoiceConfig1).isEqualTo(invoiceConfig2);
        invoiceConfig2.setId(2L);
        assertThat(invoiceConfig1).isNotEqualTo(invoiceConfig2);
        invoiceConfig1.setId(null);
        assertThat(invoiceConfig1).isNotEqualTo(invoiceConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceConfigDTO.class);
        InvoiceConfigDTO invoiceConfigDTO1 = new InvoiceConfigDTO();
        invoiceConfigDTO1.setId(1L);
        InvoiceConfigDTO invoiceConfigDTO2 = new InvoiceConfigDTO();
        assertThat(invoiceConfigDTO1).isNotEqualTo(invoiceConfigDTO2);
        invoiceConfigDTO2.setId(invoiceConfigDTO1.getId());
        assertThat(invoiceConfigDTO1).isEqualTo(invoiceConfigDTO2);
        invoiceConfigDTO2.setId(2L);
        assertThat(invoiceConfigDTO1).isNotEqualTo(invoiceConfigDTO2);
        invoiceConfigDTO1.setId(null);
        assertThat(invoiceConfigDTO1).isNotEqualTo(invoiceConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceConfigMapper.fromId(null)).isNull();
    }
}

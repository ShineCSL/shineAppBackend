package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.TypeInvoice;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.TypeInvoiceRepository;
import com.shine.shineappback.service.TypeInvoiceService;
import com.shine.shineappback.service.dto.TypeInvoiceDTO;
import com.shine.shineappback.service.mapper.TypeInvoiceMapper;
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
 * Test class for the TypeInvoiceResource REST controller.
 *
 * @see TypeInvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class TypeInvoiceResourceIntTest {

    private static final String DEFAULT_CODE = "D-_/%]";
    private static final String UPDATED_CODE = "S-_/%]";

    private static final String DEFAULT_LABEL_EN = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_EN = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL_FR = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_FR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_MODIFICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TypeInvoiceRepository typeInvoiceRepository;


    @Autowired
    private TypeInvoiceMapper typeInvoiceMapper;
    

    @Autowired
    private TypeInvoiceService typeInvoiceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeInvoiceMockMvc;

    private TypeInvoice typeInvoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeInvoiceResource typeInvoiceResource = new TypeInvoiceResource(typeInvoiceService);
        this.restTypeInvoiceMockMvc = MockMvcBuilders.standaloneSetup(typeInvoiceResource)
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
    public static TypeInvoice createEntity(EntityManager em) {
        TypeInvoice typeInvoice = new TypeInvoice()
            .code(DEFAULT_CODE)
            .labelEn(DEFAULT_LABEL_EN)
            .labelFr(DEFAULT_LABEL_FR)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        typeInvoice.setUserCreation(user);
        return typeInvoice;
    }

    @Before
    public void initTest() {
        typeInvoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeInvoice() throws Exception {
        int databaseSizeBeforeCreate = typeInvoiceRepository.findAll().size();

        // Create the TypeInvoice
        TypeInvoiceDTO typeInvoiceDTO = typeInvoiceMapper.toDto(typeInvoice);
        restTypeInvoiceMockMvc.perform(post("/api/type-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeInvoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeInvoice in the database
        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        TypeInvoice testTypeInvoice = typeInvoiceList.get(typeInvoiceList.size() - 1);
        assertThat(testTypeInvoice.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeInvoice.getLabelEn()).isEqualTo(DEFAULT_LABEL_EN);
        assertThat(testTypeInvoice.getLabelFr()).isEqualTo(DEFAULT_LABEL_FR);
        assertThat(testTypeInvoice.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testTypeInvoice.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    public void createTypeInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeInvoiceRepository.findAll().size();

        // Create the TypeInvoice with an existing ID
        typeInvoice.setId(1L);
        TypeInvoiceDTO typeInvoiceDTO = typeInvoiceMapper.toDto(typeInvoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeInvoiceMockMvc.perform(post("/api/type-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeInvoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeInvoice in the database
        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeInvoiceRepository.findAll().size();
        // set the field null
        typeInvoice.setCode(null);

        // Create the TypeInvoice, which fails.
        TypeInvoiceDTO typeInvoiceDTO = typeInvoiceMapper.toDto(typeInvoice);

        restTypeInvoiceMockMvc.perform(post("/api/type-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeInvoiceDTO)))
            .andExpect(status().isBadRequest());

        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeInvoiceRepository.findAll().size();
        // set the field null
        typeInvoice.setDateCreation(null);

        // Create the TypeInvoice, which fails.
        TypeInvoiceDTO typeInvoiceDTO = typeInvoiceMapper.toDto(typeInvoice);

        restTypeInvoiceMockMvc.perform(post("/api/type-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeInvoiceDTO)))
            .andExpect(status().isBadRequest());

        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeInvoices() throws Exception {
        // Initialize the database
        typeInvoiceRepository.saveAndFlush(typeInvoice);

        // Get all the typeInvoiceList
        restTypeInvoiceMockMvc.perform(get("/api/type-invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].labelEn").value(hasItem(DEFAULT_LABEL_EN.toString())))
            .andExpect(jsonPath("$.[*].labelFr").value(hasItem(DEFAULT_LABEL_FR.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(sameInstant(DEFAULT_DATE_MODIFICATION))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }
    

    @Test
    @Transactional
    public void getTypeInvoice() throws Exception {
        // Initialize the database
        typeInvoiceRepository.saveAndFlush(typeInvoice);

        // Get the typeInvoice
        restTypeInvoiceMockMvc.perform(get("/api/type-invoices/{id}", typeInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeInvoice.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.labelEn").value(DEFAULT_LABEL_EN.toString()))
            .andExpect(jsonPath("$.labelFr").value(DEFAULT_LABEL_FR.toString()))
            .andExpect(jsonPath("$.dateModification").value(sameInstant(DEFAULT_DATE_MODIFICATION)))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }
    @Test
    @Transactional
    public void getNonExistingTypeInvoice() throws Exception {
        // Get the typeInvoice
        restTypeInvoiceMockMvc.perform(get("/api/type-invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeInvoice() throws Exception {
        // Initialize the database
        typeInvoiceRepository.saveAndFlush(typeInvoice);

        int databaseSizeBeforeUpdate = typeInvoiceRepository.findAll().size();

        // Update the typeInvoice
        TypeInvoice updatedTypeInvoice = typeInvoiceRepository.findById(typeInvoice.getId()).get();
        // Disconnect from session so that the updates on updatedTypeInvoice are not directly saved in db
        em.detach(updatedTypeInvoice);
        updatedTypeInvoice
            .code(UPDATED_CODE)
            .labelEn(UPDATED_LABEL_EN)
            .labelFr(UPDATED_LABEL_FR)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateCreation(UPDATED_DATE_CREATION);
        TypeInvoiceDTO typeInvoiceDTO = typeInvoiceMapper.toDto(updatedTypeInvoice);

        restTypeInvoiceMockMvc.perform(put("/api/type-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeInvoiceDTO)))
            .andExpect(status().isOk());

        // Validate the TypeInvoice in the database
        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeUpdate);
        TypeInvoice testTypeInvoice = typeInvoiceList.get(typeInvoiceList.size() - 1);
        assertThat(testTypeInvoice.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeInvoice.getLabelEn()).isEqualTo(UPDATED_LABEL_EN);
        assertThat(testTypeInvoice.getLabelFr()).isEqualTo(UPDATED_LABEL_FR);
        assertThat(testTypeInvoice.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testTypeInvoice.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeInvoice() throws Exception {
        int databaseSizeBeforeUpdate = typeInvoiceRepository.findAll().size();

        // Create the TypeInvoice
        TypeInvoiceDTO typeInvoiceDTO = typeInvoiceMapper.toDto(typeInvoice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeInvoiceMockMvc.perform(put("/api/type-invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeInvoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeInvoice in the database
        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeInvoice() throws Exception {
        // Initialize the database
        typeInvoiceRepository.saveAndFlush(typeInvoice);

        int databaseSizeBeforeDelete = typeInvoiceRepository.findAll().size();

        // Get the typeInvoice
        restTypeInvoiceMockMvc.perform(delete("/api/type-invoices/{id}", typeInvoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TypeInvoice> typeInvoiceList = typeInvoiceRepository.findAll();
        assertThat(typeInvoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeInvoice.class);
        TypeInvoice typeInvoice1 = new TypeInvoice();
        typeInvoice1.setId(1L);
        TypeInvoice typeInvoice2 = new TypeInvoice();
        typeInvoice2.setId(typeInvoice1.getId());
        assertThat(typeInvoice1).isEqualTo(typeInvoice2);
        typeInvoice2.setId(2L);
        assertThat(typeInvoice1).isNotEqualTo(typeInvoice2);
        typeInvoice1.setId(null);
        assertThat(typeInvoice1).isNotEqualTo(typeInvoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeInvoiceDTO.class);
        TypeInvoiceDTO typeInvoiceDTO1 = new TypeInvoiceDTO();
        typeInvoiceDTO1.setId(1L);
        TypeInvoiceDTO typeInvoiceDTO2 = new TypeInvoiceDTO();
        assertThat(typeInvoiceDTO1).isNotEqualTo(typeInvoiceDTO2);
        typeInvoiceDTO2.setId(typeInvoiceDTO1.getId());
        assertThat(typeInvoiceDTO1).isEqualTo(typeInvoiceDTO2);
        typeInvoiceDTO2.setId(2L);
        assertThat(typeInvoiceDTO1).isNotEqualTo(typeInvoiceDTO2);
        typeInvoiceDTO1.setId(null);
        assertThat(typeInvoiceDTO1).isNotEqualTo(typeInvoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeInvoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeInvoiceMapper.fromId(null)).isNull();
    }
}

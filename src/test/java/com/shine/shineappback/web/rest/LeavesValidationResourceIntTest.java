package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.LeavesValidation;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.LeavesValidationRepository;
import com.shine.shineappback.service.LeavesValidationService;
import com.shine.shineappback.service.dto.LeavesValidationDTO;
import com.shine.shineappback.service.mapper.LeavesValidationMapper;
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
 * Test class for the LeavesValidationResource REST controller.
 *
 * @see LeavesValidationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class LeavesValidationResourceIntTest {

    private static final Boolean DEFAULT_VALIDATED = false;
    private static final Boolean UPDATED_VALIDATED = true;

    private static final ZonedDateTime DEFAULT_DATE_MODIFICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LeavesValidationRepository leavesValidationRepository;


    @Autowired
    private LeavesValidationMapper leavesValidationMapper;
    

    @Autowired
    private LeavesValidationService leavesValidationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeavesValidationMockMvc;

    private LeavesValidation leavesValidation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeavesValidationResource leavesValidationResource = new LeavesValidationResource(leavesValidationService);
        this.restLeavesValidationMockMvc = MockMvcBuilders.standaloneSetup(leavesValidationResource)
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
    public static LeavesValidation createEntity(EntityManager em) {
        LeavesValidation leavesValidation = new LeavesValidation()
            .validated(DEFAULT_VALIDATED)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leavesValidation.setUserCreation(user);
        return leavesValidation;
    }

    @Before
    public void initTest() {
        leavesValidation = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeavesValidation() throws Exception {
        int databaseSizeBeforeCreate = leavesValidationRepository.findAll().size();

        // Create the LeavesValidation
        LeavesValidationDTO leavesValidationDTO = leavesValidationMapper.toDto(leavesValidation);
        restLeavesValidationMockMvc.perform(post("/api/leaves-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesValidationDTO)))
            .andExpect(status().isCreated());

        // Validate the LeavesValidation in the database
        List<LeavesValidation> leavesValidationList = leavesValidationRepository.findAll();
        assertThat(leavesValidationList).hasSize(databaseSizeBeforeCreate + 1);
        LeavesValidation testLeavesValidation = leavesValidationList.get(leavesValidationList.size() - 1);
        assertThat(testLeavesValidation.isValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testLeavesValidation.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testLeavesValidation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    public void createLeavesValidationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leavesValidationRepository.findAll().size();

        // Create the LeavesValidation with an existing ID
        leavesValidation.setId(1L);
        LeavesValidationDTO leavesValidationDTO = leavesValidationMapper.toDto(leavesValidation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeavesValidationMockMvc.perform(post("/api/leaves-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesValidationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeavesValidation in the database
        List<LeavesValidation> leavesValidationList = leavesValidationRepository.findAll();
        assertThat(leavesValidationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesValidationRepository.findAll().size();
        // set the field null
        leavesValidation.setDateCreation(null);

        // Create the LeavesValidation, which fails.
        LeavesValidationDTO leavesValidationDTO = leavesValidationMapper.toDto(leavesValidation);

        restLeavesValidationMockMvc.perform(post("/api/leaves-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesValidationDTO)))
            .andExpect(status().isBadRequest());

        List<LeavesValidation> leavesValidationList = leavesValidationRepository.findAll();
        assertThat(leavesValidationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeavesValidations() throws Exception {
        // Initialize the database
        leavesValidationRepository.saveAndFlush(leavesValidation);

        // Get all the leavesValidationList
        restLeavesValidationMockMvc.perform(get("/api/leaves-validations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leavesValidation.getId().intValue())))
            .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(sameInstant(DEFAULT_DATE_MODIFICATION))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }
    

    @Test
    @Transactional
    public void getLeavesValidation() throws Exception {
        // Initialize the database
        leavesValidationRepository.saveAndFlush(leavesValidation);

        // Get the leavesValidation
        restLeavesValidationMockMvc.perform(get("/api/leaves-validations/{id}", leavesValidation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leavesValidation.getId().intValue()))
            .andExpect(jsonPath("$.validated").value(DEFAULT_VALIDATED.booleanValue()))
            .andExpect(jsonPath("$.dateModification").value(sameInstant(DEFAULT_DATE_MODIFICATION)))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }
    @Test
    @Transactional
    public void getNonExistingLeavesValidation() throws Exception {
        // Get the leavesValidation
        restLeavesValidationMockMvc.perform(get("/api/leaves-validations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeavesValidation() throws Exception {
        // Initialize the database
        leavesValidationRepository.saveAndFlush(leavesValidation);

        int databaseSizeBeforeUpdate = leavesValidationRepository.findAll().size();

        // Update the leavesValidation
        LeavesValidation updatedLeavesValidation = leavesValidationRepository.findById(leavesValidation.getId()).get();
        // Disconnect from session so that the updates on updatedLeavesValidation are not directly saved in db
        em.detach(updatedLeavesValidation);
        updatedLeavesValidation
            .validated(UPDATED_VALIDATED)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateCreation(UPDATED_DATE_CREATION);
        LeavesValidationDTO leavesValidationDTO = leavesValidationMapper.toDto(updatedLeavesValidation);

        restLeavesValidationMockMvc.perform(put("/api/leaves-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesValidationDTO)))
            .andExpect(status().isOk());

        // Validate the LeavesValidation in the database
        List<LeavesValidation> leavesValidationList = leavesValidationRepository.findAll();
        assertThat(leavesValidationList).hasSize(databaseSizeBeforeUpdate);
        LeavesValidation testLeavesValidation = leavesValidationList.get(leavesValidationList.size() - 1);
        assertThat(testLeavesValidation.isValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testLeavesValidation.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testLeavesValidation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void updateNonExistingLeavesValidation() throws Exception {
        int databaseSizeBeforeUpdate = leavesValidationRepository.findAll().size();

        // Create the LeavesValidation
        LeavesValidationDTO leavesValidationDTO = leavesValidationMapper.toDto(leavesValidation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeavesValidationMockMvc.perform(put("/api/leaves-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesValidationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeavesValidation in the database
        List<LeavesValidation> leavesValidationList = leavesValidationRepository.findAll();
        assertThat(leavesValidationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeavesValidation() throws Exception {
        // Initialize the database
        leavesValidationRepository.saveAndFlush(leavesValidation);

        int databaseSizeBeforeDelete = leavesValidationRepository.findAll().size();

        // Get the leavesValidation
        restLeavesValidationMockMvc.perform(delete("/api/leaves-validations/{id}", leavesValidation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LeavesValidation> leavesValidationList = leavesValidationRepository.findAll();
        assertThat(leavesValidationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesValidation.class);
        LeavesValidation leavesValidation1 = new LeavesValidation();
        leavesValidation1.setId(1L);
        LeavesValidation leavesValidation2 = new LeavesValidation();
        leavesValidation2.setId(leavesValidation1.getId());
        assertThat(leavesValidation1).isEqualTo(leavesValidation2);
        leavesValidation2.setId(2L);
        assertThat(leavesValidation1).isNotEqualTo(leavesValidation2);
        leavesValidation1.setId(null);
        assertThat(leavesValidation1).isNotEqualTo(leavesValidation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesValidationDTO.class);
        LeavesValidationDTO leavesValidationDTO1 = new LeavesValidationDTO();
        leavesValidationDTO1.setId(1L);
        LeavesValidationDTO leavesValidationDTO2 = new LeavesValidationDTO();
        assertThat(leavesValidationDTO1).isNotEqualTo(leavesValidationDTO2);
        leavesValidationDTO2.setId(leavesValidationDTO1.getId());
        assertThat(leavesValidationDTO1).isEqualTo(leavesValidationDTO2);
        leavesValidationDTO2.setId(2L);
        assertThat(leavesValidationDTO1).isNotEqualTo(leavesValidationDTO2);
        leavesValidationDTO1.setId(null);
        assertThat(leavesValidationDTO1).isNotEqualTo(leavesValidationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leavesValidationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leavesValidationMapper.fromId(null)).isNull();
    }
}

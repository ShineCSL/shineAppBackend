package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.ActivityValidation;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.ActivityValidationRepository;
import com.shine.shineappback.service.ActivityValidationService;
import com.shine.shineappback.service.dto.ActivityValidationDTO;
import com.shine.shineappback.service.mapper.ActivityValidationMapper;
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
 * Test class for the ActivityValidationResource REST controller.
 *
 * @see ActivityValidationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class ActivityValidationResourceIntTest {

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Boolean DEFAULT_VALIDATED = false;
    private static final Boolean UPDATED_VALIDATED = true;

    @Autowired
    private ActivityValidationRepository activityValidationRepository;


    @Autowired
    private ActivityValidationMapper activityValidationMapper;
    

    @Autowired
    private ActivityValidationService activityValidationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityValidationMockMvc;

    private ActivityValidation activityValidation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityValidationResource activityValidationResource = new ActivityValidationResource(activityValidationService);
        this.restActivityValidationMockMvc = MockMvcBuilders.standaloneSetup(activityValidationResource)
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
    public static ActivityValidation createEntity(EntityManager em) {
        ActivityValidation activityValidation = new ActivityValidation()
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .year(DEFAULT_YEAR)
            .validated(DEFAULT_VALIDATED);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        activityValidation.setUser(user);
        return activityValidation;
    }

    @Before
    public void initTest() {
        activityValidation = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityValidation() throws Exception {
        int databaseSizeBeforeCreate = activityValidationRepository.findAll().size();

        // Create the ActivityValidation
        ActivityValidationDTO activityValidationDTO = activityValidationMapper.toDto(activityValidation);
        restActivityValidationMockMvc.perform(post("/api/activity-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityValidationDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityValidation in the database
        List<ActivityValidation> activityValidationList = activityValidationRepository.findAll();
        assertThat(activityValidationList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityValidation testActivityValidation = activityValidationList.get(activityValidationList.size() - 1);
        assertThat(testActivityValidation.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testActivityValidation.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testActivityValidation.isValidated()).isEqualTo(DEFAULT_VALIDATED);
    }

    @Test
    @Transactional
    public void createActivityValidationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityValidationRepository.findAll().size();

        // Create the ActivityValidation with an existing ID
        activityValidation.setId(1L);
        ActivityValidationDTO activityValidationDTO = activityValidationMapper.toDto(activityValidation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityValidationMockMvc.perform(post("/api/activity-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityValidationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityValidation in the database
        List<ActivityValidation> activityValidationList = activityValidationRepository.findAll();
        assertThat(activityValidationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActivityValidations() throws Exception {
        // Initialize the database
        activityValidationRepository.saveAndFlush(activityValidation);

        // Get all the activityValidationList
        restActivityValidationMockMvc.perform(get("/api/activity-validations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityValidation.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getActivityValidation() throws Exception {
        // Initialize the database
        activityValidationRepository.saveAndFlush(activityValidation);

        // Get the activityValidation
        restActivityValidationMockMvc.perform(get("/api/activity-validations/{id}", activityValidation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityValidation.getId().intValue()))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.validated").value(DEFAULT_VALIDATED.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingActivityValidation() throws Exception {
        // Get the activityValidation
        restActivityValidationMockMvc.perform(get("/api/activity-validations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityValidation() throws Exception {
        // Initialize the database
        activityValidationRepository.saveAndFlush(activityValidation);

        int databaseSizeBeforeUpdate = activityValidationRepository.findAll().size();

        // Update the activityValidation
        ActivityValidation updatedActivityValidation = activityValidationRepository.findById(activityValidation.getId()).get();
        // Disconnect from session so that the updates on updatedActivityValidation are not directly saved in db
        em.detach(updatedActivityValidation);
        updatedActivityValidation
            .weekNumber(UPDATED_WEEK_NUMBER)
            .year(UPDATED_YEAR)
            .validated(UPDATED_VALIDATED);
        ActivityValidationDTO activityValidationDTO = activityValidationMapper.toDto(updatedActivityValidation);

        restActivityValidationMockMvc.perform(put("/api/activity-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityValidationDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityValidation in the database
        List<ActivityValidation> activityValidationList = activityValidationRepository.findAll();
        assertThat(activityValidationList).hasSize(databaseSizeBeforeUpdate);
        ActivityValidation testActivityValidation = activityValidationList.get(activityValidationList.size() - 1);
        assertThat(testActivityValidation.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testActivityValidation.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testActivityValidation.isValidated()).isEqualTo(UPDATED_VALIDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityValidation() throws Exception {
        int databaseSizeBeforeUpdate = activityValidationRepository.findAll().size();

        // Create the ActivityValidation
        ActivityValidationDTO activityValidationDTO = activityValidationMapper.toDto(activityValidation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityValidationMockMvc.perform(put("/api/activity-validations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityValidationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityValidation in the database
        List<ActivityValidation> activityValidationList = activityValidationRepository.findAll();
        assertThat(activityValidationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityValidation() throws Exception {
        // Initialize the database
        activityValidationRepository.saveAndFlush(activityValidation);

        int databaseSizeBeforeDelete = activityValidationRepository.findAll().size();

        // Get the activityValidation
        restActivityValidationMockMvc.perform(delete("/api/activity-validations/{id}", activityValidation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityValidation> activityValidationList = activityValidationRepository.findAll();
        assertThat(activityValidationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityValidation.class);
        ActivityValidation activityValidation1 = new ActivityValidation();
        activityValidation1.setId(1L);
        ActivityValidation activityValidation2 = new ActivityValidation();
        activityValidation2.setId(activityValidation1.getId());
        assertThat(activityValidation1).isEqualTo(activityValidation2);
        activityValidation2.setId(2L);
        assertThat(activityValidation1).isNotEqualTo(activityValidation2);
        activityValidation1.setId(null);
        assertThat(activityValidation1).isNotEqualTo(activityValidation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityValidationDTO.class);
        ActivityValidationDTO activityValidationDTO1 = new ActivityValidationDTO();
        activityValidationDTO1.setId(1L);
        ActivityValidationDTO activityValidationDTO2 = new ActivityValidationDTO();
        assertThat(activityValidationDTO1).isNotEqualTo(activityValidationDTO2);
        activityValidationDTO2.setId(activityValidationDTO1.getId());
        assertThat(activityValidationDTO1).isEqualTo(activityValidationDTO2);
        activityValidationDTO2.setId(2L);
        assertThat(activityValidationDTO1).isNotEqualTo(activityValidationDTO2);
        activityValidationDTO1.setId(null);
        assertThat(activityValidationDTO1).isNotEqualTo(activityValidationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityValidationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityValidationMapper.fromId(null)).isNull();
    }
}

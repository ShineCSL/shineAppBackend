package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.ActivitySubmission;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.ActivitySubmissionRepository;
import com.shine.shineappback.service.ActivitySubmissionService;
import com.shine.shineappback.service.dto.ActivitySubmissionDTO;
import com.shine.shineappback.service.mapper.ActivitySubmissionMapper;
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
 * Test class for the ActivitySubmissionResource REST controller.
 *
 * @see ActivitySubmissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class ActivitySubmissionResourceIntTest {

    private static final Boolean DEFAULT_SUBMITTED = false;
    private static final Boolean UPDATED_SUBMITTED = true;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_MODIFICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ActivitySubmissionRepository activitySubmissionRepository;


    @Autowired
    private ActivitySubmissionMapper activitySubmissionMapper;
    

    @Autowired
    private ActivitySubmissionService activitySubmissionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivitySubmissionMockMvc;

    private ActivitySubmission activitySubmission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivitySubmissionResource activitySubmissionResource = new ActivitySubmissionResource(activitySubmissionService);
        this.restActivitySubmissionMockMvc = MockMvcBuilders.standaloneSetup(activitySubmissionResource)
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
    public static ActivitySubmission createEntity(EntityManager em) {
        ActivitySubmission activitySubmission = new ActivitySubmission()
            .submitted(DEFAULT_SUBMITTED)
            .year(DEFAULT_YEAR)
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        activitySubmission.setUserCreation(user);
        return activitySubmission;
    }

    @Before
    public void initTest() {
        activitySubmission = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivitySubmission() throws Exception {
        int databaseSizeBeforeCreate = activitySubmissionRepository.findAll().size();

        // Create the ActivitySubmission
        ActivitySubmissionDTO activitySubmissionDTO = activitySubmissionMapper.toDto(activitySubmission);
        restActivitySubmissionMockMvc.perform(post("/api/activity-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activitySubmissionDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivitySubmission in the database
        List<ActivitySubmission> activitySubmissionList = activitySubmissionRepository.findAll();
        assertThat(activitySubmissionList).hasSize(databaseSizeBeforeCreate + 1);
        ActivitySubmission testActivitySubmission = activitySubmissionList.get(activitySubmissionList.size() - 1);
        assertThat(testActivitySubmission.isSubmitted()).isEqualTo(DEFAULT_SUBMITTED);
        assertThat(testActivitySubmission.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testActivitySubmission.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testActivitySubmission.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testActivitySubmission.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    public void createActivitySubmissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activitySubmissionRepository.findAll().size();

        // Create the ActivitySubmission with an existing ID
        activitySubmission.setId(1L);
        ActivitySubmissionDTO activitySubmissionDTO = activitySubmissionMapper.toDto(activitySubmission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivitySubmissionMockMvc.perform(post("/api/activity-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activitySubmissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivitySubmission in the database
        List<ActivitySubmission> activitySubmissionList = activitySubmissionRepository.findAll();
        assertThat(activitySubmissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = activitySubmissionRepository.findAll().size();
        // set the field null
        activitySubmission.setDateCreation(null);

        // Create the ActivitySubmission, which fails.
        ActivitySubmissionDTO activitySubmissionDTO = activitySubmissionMapper.toDto(activitySubmission);

        restActivitySubmissionMockMvc.perform(post("/api/activity-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activitySubmissionDTO)))
            .andExpect(status().isBadRequest());

        List<ActivitySubmission> activitySubmissionList = activitySubmissionRepository.findAll();
        assertThat(activitySubmissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivitySubmissions() throws Exception {
        // Initialize the database
        activitySubmissionRepository.saveAndFlush(activitySubmission);

        // Get all the activitySubmissionList
        restActivitySubmissionMockMvc.perform(get("/api/activity-submissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activitySubmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].submitted").value(hasItem(DEFAULT_SUBMITTED.booleanValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(sameInstant(DEFAULT_DATE_MODIFICATION))));
    }
    

    @Test
    @Transactional
    public void getActivitySubmission() throws Exception {
        // Initialize the database
        activitySubmissionRepository.saveAndFlush(activitySubmission);

        // Get the activitySubmission
        restActivitySubmissionMockMvc.perform(get("/api/activity-submissions/{id}", activitySubmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activitySubmission.getId().intValue()))
            .andExpect(jsonPath("$.submitted").value(DEFAULT_SUBMITTED.booleanValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.dateModification").value(sameInstant(DEFAULT_DATE_MODIFICATION)));
    }
    @Test
    @Transactional
    public void getNonExistingActivitySubmission() throws Exception {
        // Get the activitySubmission
        restActivitySubmissionMockMvc.perform(get("/api/activity-submissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivitySubmission() throws Exception {
        // Initialize the database
        activitySubmissionRepository.saveAndFlush(activitySubmission);

        int databaseSizeBeforeUpdate = activitySubmissionRepository.findAll().size();

        // Update the activitySubmission
        ActivitySubmission updatedActivitySubmission = activitySubmissionRepository.findById(activitySubmission.getId()).get();
        // Disconnect from session so that the updates on updatedActivitySubmission are not directly saved in db
        em.detach(updatedActivitySubmission);
        updatedActivitySubmission
            .submitted(UPDATED_SUBMITTED)
            .year(UPDATED_YEAR)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        ActivitySubmissionDTO activitySubmissionDTO = activitySubmissionMapper.toDto(updatedActivitySubmission);

        restActivitySubmissionMockMvc.perform(put("/api/activity-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activitySubmissionDTO)))
            .andExpect(status().isOk());

        // Validate the ActivitySubmission in the database
        List<ActivitySubmission> activitySubmissionList = activitySubmissionRepository.findAll();
        assertThat(activitySubmissionList).hasSize(databaseSizeBeforeUpdate);
        ActivitySubmission testActivitySubmission = activitySubmissionList.get(activitySubmissionList.size() - 1);
        assertThat(testActivitySubmission.isSubmitted()).isEqualTo(UPDATED_SUBMITTED);
        assertThat(testActivitySubmission.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testActivitySubmission.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testActivitySubmission.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testActivitySubmission.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingActivitySubmission() throws Exception {
        int databaseSizeBeforeUpdate = activitySubmissionRepository.findAll().size();

        // Create the ActivitySubmission
        ActivitySubmissionDTO activitySubmissionDTO = activitySubmissionMapper.toDto(activitySubmission);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivitySubmissionMockMvc.perform(put("/api/activity-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activitySubmissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivitySubmission in the database
        List<ActivitySubmission> activitySubmissionList = activitySubmissionRepository.findAll();
        assertThat(activitySubmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivitySubmission() throws Exception {
        // Initialize the database
        activitySubmissionRepository.saveAndFlush(activitySubmission);

        int databaseSizeBeforeDelete = activitySubmissionRepository.findAll().size();

        // Get the activitySubmission
        restActivitySubmissionMockMvc.perform(delete("/api/activity-submissions/{id}", activitySubmission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivitySubmission> activitySubmissionList = activitySubmissionRepository.findAll();
        assertThat(activitySubmissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivitySubmission.class);
        ActivitySubmission activitySubmission1 = new ActivitySubmission();
        activitySubmission1.setId(1L);
        ActivitySubmission activitySubmission2 = new ActivitySubmission();
        activitySubmission2.setId(activitySubmission1.getId());
        assertThat(activitySubmission1).isEqualTo(activitySubmission2);
        activitySubmission2.setId(2L);
        assertThat(activitySubmission1).isNotEqualTo(activitySubmission2);
        activitySubmission1.setId(null);
        assertThat(activitySubmission1).isNotEqualTo(activitySubmission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivitySubmissionDTO.class);
        ActivitySubmissionDTO activitySubmissionDTO1 = new ActivitySubmissionDTO();
        activitySubmissionDTO1.setId(1L);
        ActivitySubmissionDTO activitySubmissionDTO2 = new ActivitySubmissionDTO();
        assertThat(activitySubmissionDTO1).isNotEqualTo(activitySubmissionDTO2);
        activitySubmissionDTO2.setId(activitySubmissionDTO1.getId());
        assertThat(activitySubmissionDTO1).isEqualTo(activitySubmissionDTO2);
        activitySubmissionDTO2.setId(2L);
        assertThat(activitySubmissionDTO1).isNotEqualTo(activitySubmissionDTO2);
        activitySubmissionDTO1.setId(null);
        assertThat(activitySubmissionDTO1).isNotEqualTo(activitySubmissionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activitySubmissionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activitySubmissionMapper.fromId(null)).isNull();
    }
}

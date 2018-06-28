package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.LeavesSubmission;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.LeavesSubmissionRepository;
import com.shine.shineappback.service.LeavesSubmissionService;
import com.shine.shineappback.service.dto.LeavesSubmissionDTO;
import com.shine.shineappback.service.mapper.LeavesSubmissionMapper;
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
 * Test class for the LeavesSubmissionResource REST controller.
 *
 * @see LeavesSubmissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class LeavesSubmissionResourceIntTest {

    private static final Boolean DEFAULT_SUBMITTED = false;
    private static final Boolean UPDATED_SUBMITTED = true;

    private static final ZonedDateTime DEFAULT_DATE_MODIFICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LeavesSubmissionRepository leavesSubmissionRepository;


    @Autowired
    private LeavesSubmissionMapper leavesSubmissionMapper;
    

    @Autowired
    private LeavesSubmissionService leavesSubmissionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeavesSubmissionMockMvc;

    private LeavesSubmission leavesSubmission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeavesSubmissionResource leavesSubmissionResource = new LeavesSubmissionResource(leavesSubmissionService);
        this.restLeavesSubmissionMockMvc = MockMvcBuilders.standaloneSetup(leavesSubmissionResource)
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
    public static LeavesSubmission createEntity(EntityManager em) {
        LeavesSubmission leavesSubmission = new LeavesSubmission()
            .submitted(DEFAULT_SUBMITTED)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leavesSubmission.setUserCreation(user);
        return leavesSubmission;
    }

    @Before
    public void initTest() {
        leavesSubmission = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeavesSubmission() throws Exception {
        int databaseSizeBeforeCreate = leavesSubmissionRepository.findAll().size();

        // Create the LeavesSubmission
        LeavesSubmissionDTO leavesSubmissionDTO = leavesSubmissionMapper.toDto(leavesSubmission);
        restLeavesSubmissionMockMvc.perform(post("/api/leaves-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesSubmissionDTO)))
            .andExpect(status().isCreated());

        // Validate the LeavesSubmission in the database
        List<LeavesSubmission> leavesSubmissionList = leavesSubmissionRepository.findAll();
        assertThat(leavesSubmissionList).hasSize(databaseSizeBeforeCreate + 1);
        LeavesSubmission testLeavesSubmission = leavesSubmissionList.get(leavesSubmissionList.size() - 1);
        assertThat(testLeavesSubmission.isSubmitted()).isEqualTo(DEFAULT_SUBMITTED);
        assertThat(testLeavesSubmission.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testLeavesSubmission.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    public void createLeavesSubmissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leavesSubmissionRepository.findAll().size();

        // Create the LeavesSubmission with an existing ID
        leavesSubmission.setId(1L);
        LeavesSubmissionDTO leavesSubmissionDTO = leavesSubmissionMapper.toDto(leavesSubmission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeavesSubmissionMockMvc.perform(post("/api/leaves-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesSubmissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeavesSubmission in the database
        List<LeavesSubmission> leavesSubmissionList = leavesSubmissionRepository.findAll();
        assertThat(leavesSubmissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesSubmissionRepository.findAll().size();
        // set the field null
        leavesSubmission.setDateCreation(null);

        // Create the LeavesSubmission, which fails.
        LeavesSubmissionDTO leavesSubmissionDTO = leavesSubmissionMapper.toDto(leavesSubmission);

        restLeavesSubmissionMockMvc.perform(post("/api/leaves-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesSubmissionDTO)))
            .andExpect(status().isBadRequest());

        List<LeavesSubmission> leavesSubmissionList = leavesSubmissionRepository.findAll();
        assertThat(leavesSubmissionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeavesSubmissions() throws Exception {
        // Initialize the database
        leavesSubmissionRepository.saveAndFlush(leavesSubmission);

        // Get all the leavesSubmissionList
        restLeavesSubmissionMockMvc.perform(get("/api/leaves-submissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leavesSubmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].submitted").value(hasItem(DEFAULT_SUBMITTED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(sameInstant(DEFAULT_DATE_MODIFICATION))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }
    

    @Test
    @Transactional
    public void getLeavesSubmission() throws Exception {
        // Initialize the database
        leavesSubmissionRepository.saveAndFlush(leavesSubmission);

        // Get the leavesSubmission
        restLeavesSubmissionMockMvc.perform(get("/api/leaves-submissions/{id}", leavesSubmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leavesSubmission.getId().intValue()))
            .andExpect(jsonPath("$.submitted").value(DEFAULT_SUBMITTED.booleanValue()))
            .andExpect(jsonPath("$.dateModification").value(sameInstant(DEFAULT_DATE_MODIFICATION)))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }
    @Test
    @Transactional
    public void getNonExistingLeavesSubmission() throws Exception {
        // Get the leavesSubmission
        restLeavesSubmissionMockMvc.perform(get("/api/leaves-submissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeavesSubmission() throws Exception {
        // Initialize the database
        leavesSubmissionRepository.saveAndFlush(leavesSubmission);

        int databaseSizeBeforeUpdate = leavesSubmissionRepository.findAll().size();

        // Update the leavesSubmission
        LeavesSubmission updatedLeavesSubmission = leavesSubmissionRepository.findById(leavesSubmission.getId()).get();
        // Disconnect from session so that the updates on updatedLeavesSubmission are not directly saved in db
        em.detach(updatedLeavesSubmission);
        updatedLeavesSubmission
            .submitted(UPDATED_SUBMITTED)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateCreation(UPDATED_DATE_CREATION);
        LeavesSubmissionDTO leavesSubmissionDTO = leavesSubmissionMapper.toDto(updatedLeavesSubmission);

        restLeavesSubmissionMockMvc.perform(put("/api/leaves-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesSubmissionDTO)))
            .andExpect(status().isOk());

        // Validate the LeavesSubmission in the database
        List<LeavesSubmission> leavesSubmissionList = leavesSubmissionRepository.findAll();
        assertThat(leavesSubmissionList).hasSize(databaseSizeBeforeUpdate);
        LeavesSubmission testLeavesSubmission = leavesSubmissionList.get(leavesSubmissionList.size() - 1);
        assertThat(testLeavesSubmission.isSubmitted()).isEqualTo(UPDATED_SUBMITTED);
        assertThat(testLeavesSubmission.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testLeavesSubmission.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void updateNonExistingLeavesSubmission() throws Exception {
        int databaseSizeBeforeUpdate = leavesSubmissionRepository.findAll().size();

        // Create the LeavesSubmission
        LeavesSubmissionDTO leavesSubmissionDTO = leavesSubmissionMapper.toDto(leavesSubmission);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeavesSubmissionMockMvc.perform(put("/api/leaves-submissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesSubmissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeavesSubmission in the database
        List<LeavesSubmission> leavesSubmissionList = leavesSubmissionRepository.findAll();
        assertThat(leavesSubmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeavesSubmission() throws Exception {
        // Initialize the database
        leavesSubmissionRepository.saveAndFlush(leavesSubmission);

        int databaseSizeBeforeDelete = leavesSubmissionRepository.findAll().size();

        // Get the leavesSubmission
        restLeavesSubmissionMockMvc.perform(delete("/api/leaves-submissions/{id}", leavesSubmission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LeavesSubmission> leavesSubmissionList = leavesSubmissionRepository.findAll();
        assertThat(leavesSubmissionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesSubmission.class);
        LeavesSubmission leavesSubmission1 = new LeavesSubmission();
        leavesSubmission1.setId(1L);
        LeavesSubmission leavesSubmission2 = new LeavesSubmission();
        leavesSubmission2.setId(leavesSubmission1.getId());
        assertThat(leavesSubmission1).isEqualTo(leavesSubmission2);
        leavesSubmission2.setId(2L);
        assertThat(leavesSubmission1).isNotEqualTo(leavesSubmission2);
        leavesSubmission1.setId(null);
        assertThat(leavesSubmission1).isNotEqualTo(leavesSubmission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesSubmissionDTO.class);
        LeavesSubmissionDTO leavesSubmissionDTO1 = new LeavesSubmissionDTO();
        leavesSubmissionDTO1.setId(1L);
        LeavesSubmissionDTO leavesSubmissionDTO2 = new LeavesSubmissionDTO();
        assertThat(leavesSubmissionDTO1).isNotEqualTo(leavesSubmissionDTO2);
        leavesSubmissionDTO2.setId(leavesSubmissionDTO1.getId());
        assertThat(leavesSubmissionDTO1).isEqualTo(leavesSubmissionDTO2);
        leavesSubmissionDTO2.setId(2L);
        assertThat(leavesSubmissionDTO1).isNotEqualTo(leavesSubmissionDTO2);
        leavesSubmissionDTO1.setId(null);
        assertThat(leavesSubmissionDTO1).isNotEqualTo(leavesSubmissionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leavesSubmissionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leavesSubmissionMapper.fromId(null)).isNull();
    }
}

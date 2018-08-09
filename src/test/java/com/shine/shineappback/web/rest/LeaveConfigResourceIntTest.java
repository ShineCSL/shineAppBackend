package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.LeaveConfig;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.LeaveConfigRepository;
import com.shine.shineappback.service.LeaveConfigService;
import com.shine.shineappback.service.dto.LeaveConfigDTO;
import com.shine.shineappback.service.mapper.LeaveConfigMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.LeaveConfigCriteria;
import com.shine.shineappback.service.LeaveConfigQueryService;

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
 * Test class for the LeaveConfigResource REST controller.
 *
 * @see LeaveConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class LeaveConfigResourceIntTest {

    private static final Integer DEFAULT_NB_SICK_LEAVES = 1;
    private static final Integer UPDATED_NB_SICK_LEAVES = 2;

    private static final Integer DEFAULT_NB_ANNUAL_LEAVES = 1;
    private static final Integer UPDATED_NB_ANNUAL_LEAVES = 2;

    private static final Integer DEFAULT_NB_SPECIAL_LEAVES = 1;
    private static final Integer UPDATED_NB_SPECIAL_LEAVES = 2;

    @Autowired
    private LeaveConfigRepository leaveConfigRepository;


    @Autowired
    private LeaveConfigMapper leaveConfigMapper;
    

    @Autowired
    private LeaveConfigService leaveConfigService;

    @Autowired
    private LeaveConfigQueryService leaveConfigQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeaveConfigMockMvc;

    private LeaveConfig leaveConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeaveConfigResource leaveConfigResource = new LeaveConfigResource(leaveConfigService, leaveConfigQueryService);
        this.restLeaveConfigMockMvc = MockMvcBuilders.standaloneSetup(leaveConfigResource)
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
    public static LeaveConfig createEntity(EntityManager em) {
        LeaveConfig leaveConfig = new LeaveConfig()
            .nbSickLeaves(DEFAULT_NB_SICK_LEAVES)
            .nbAnnualLeaves(DEFAULT_NB_ANNUAL_LEAVES)
            .nbSpecialLeaves(DEFAULT_NB_SPECIAL_LEAVES);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leaveConfig.setUser(user);
        return leaveConfig;
    }

    @Before
    public void initTest() {
        leaveConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveConfig() throws Exception {
        int databaseSizeBeforeCreate = leaveConfigRepository.findAll().size();

        // Create the LeaveConfig
        LeaveConfigDTO leaveConfigDTO = leaveConfigMapper.toDto(leaveConfig);
        restLeaveConfigMockMvc.perform(post("/api/leave-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the LeaveConfig in the database
        List<LeaveConfig> leaveConfigList = leaveConfigRepository.findAll();
        assertThat(leaveConfigList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveConfig testLeaveConfig = leaveConfigList.get(leaveConfigList.size() - 1);
        assertThat(testLeaveConfig.getNbSickLeaves()).isEqualTo(DEFAULT_NB_SICK_LEAVES);
        assertThat(testLeaveConfig.getNbAnnualLeaves()).isEqualTo(DEFAULT_NB_ANNUAL_LEAVES);
        assertThat(testLeaveConfig.getNbSpecialLeaves()).isEqualTo(DEFAULT_NB_SPECIAL_LEAVES);
    }

    @Test
    @Transactional
    public void createLeaveConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveConfigRepository.findAll().size();

        // Create the LeaveConfig with an existing ID
        leaveConfig.setId(1L);
        LeaveConfigDTO leaveConfigDTO = leaveConfigMapper.toDto(leaveConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveConfigMockMvc.perform(post("/api/leave-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaveConfig in the database
        List<LeaveConfig> leaveConfigList = leaveConfigRepository.findAll();
        assertThat(leaveConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigs() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList
        restLeaveConfigMockMvc.perform(get("/api/leave-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbSickLeaves").value(hasItem(DEFAULT_NB_SICK_LEAVES)))
            .andExpect(jsonPath("$.[*].nbAnnualLeaves").value(hasItem(DEFAULT_NB_ANNUAL_LEAVES)))
            .andExpect(jsonPath("$.[*].nbSpecialLeaves").value(hasItem(DEFAULT_NB_SPECIAL_LEAVES)));
    }
    

    @Test
    @Transactional
    public void getLeaveConfig() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get the leaveConfig
        restLeaveConfigMockMvc.perform(get("/api/leave-configs/{id}", leaveConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leaveConfig.getId().intValue()))
            .andExpect(jsonPath("$.nbSickLeaves").value(DEFAULT_NB_SICK_LEAVES))
            .andExpect(jsonPath("$.nbAnnualLeaves").value(DEFAULT_NB_ANNUAL_LEAVES))
            .andExpect(jsonPath("$.nbSpecialLeaves").value(DEFAULT_NB_SPECIAL_LEAVES));
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSickLeavesIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSickLeaves equals to DEFAULT_NB_SICK_LEAVES
        defaultLeaveConfigShouldBeFound("nbSickLeaves.equals=" + DEFAULT_NB_SICK_LEAVES);

        // Get all the leaveConfigList where nbSickLeaves equals to UPDATED_NB_SICK_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSickLeaves.equals=" + UPDATED_NB_SICK_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSickLeavesIsInShouldWork() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSickLeaves in DEFAULT_NB_SICK_LEAVES or UPDATED_NB_SICK_LEAVES
        defaultLeaveConfigShouldBeFound("nbSickLeaves.in=" + DEFAULT_NB_SICK_LEAVES + "," + UPDATED_NB_SICK_LEAVES);

        // Get all the leaveConfigList where nbSickLeaves equals to UPDATED_NB_SICK_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSickLeaves.in=" + UPDATED_NB_SICK_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSickLeavesIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSickLeaves is not null
        defaultLeaveConfigShouldBeFound("nbSickLeaves.specified=true");

        // Get all the leaveConfigList where nbSickLeaves is null
        defaultLeaveConfigShouldNotBeFound("nbSickLeaves.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSickLeavesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSickLeaves greater than or equals to DEFAULT_NB_SICK_LEAVES
        defaultLeaveConfigShouldBeFound("nbSickLeaves.greaterOrEqualThan=" + DEFAULT_NB_SICK_LEAVES);

        // Get all the leaveConfigList where nbSickLeaves greater than or equals to UPDATED_NB_SICK_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSickLeaves.greaterOrEqualThan=" + UPDATED_NB_SICK_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSickLeavesIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSickLeaves less than or equals to DEFAULT_NB_SICK_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSickLeaves.lessThan=" + DEFAULT_NB_SICK_LEAVES);

        // Get all the leaveConfigList where nbSickLeaves less than or equals to UPDATED_NB_SICK_LEAVES
        defaultLeaveConfigShouldBeFound("nbSickLeaves.lessThan=" + UPDATED_NB_SICK_LEAVES);
    }


    @Test
    @Transactional
    public void getAllLeaveConfigsByNbAnnualLeavesIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbAnnualLeaves equals to DEFAULT_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbAnnualLeaves.equals=" + DEFAULT_NB_ANNUAL_LEAVES);

        // Get all the leaveConfigList where nbAnnualLeaves equals to UPDATED_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbAnnualLeaves.equals=" + UPDATED_NB_ANNUAL_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbAnnualLeavesIsInShouldWork() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbAnnualLeaves in DEFAULT_NB_ANNUAL_LEAVES or UPDATED_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbAnnualLeaves.in=" + DEFAULT_NB_ANNUAL_LEAVES + "," + UPDATED_NB_ANNUAL_LEAVES);

        // Get all the leaveConfigList where nbAnnualLeaves equals to UPDATED_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbAnnualLeaves.in=" + UPDATED_NB_ANNUAL_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbAnnualLeavesIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbAnnualLeaves is not null
        defaultLeaveConfigShouldBeFound("nbAnnualLeaves.specified=true");

        // Get all the leaveConfigList where nbAnnualLeaves is null
        defaultLeaveConfigShouldNotBeFound("nbAnnualLeaves.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbAnnualLeavesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbAnnualLeaves greater than or equals to DEFAULT_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbAnnualLeaves.greaterOrEqualThan=" + DEFAULT_NB_ANNUAL_LEAVES);

        // Get all the leaveConfigList where nbAnnualLeaves greater than or equals to UPDATED_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbAnnualLeaves.greaterOrEqualThan=" + UPDATED_NB_ANNUAL_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbAnnualLeavesIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbAnnualLeaves less than or equals to DEFAULT_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbAnnualLeaves.lessThan=" + DEFAULT_NB_ANNUAL_LEAVES);

        // Get all the leaveConfigList where nbAnnualLeaves less than or equals to UPDATED_NB_ANNUAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbAnnualLeaves.lessThan=" + UPDATED_NB_ANNUAL_LEAVES);
    }


    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSpecialLeavesIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSpecialLeaves equals to DEFAULT_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbSpecialLeaves.equals=" + DEFAULT_NB_SPECIAL_LEAVES);

        // Get all the leaveConfigList where nbSpecialLeaves equals to UPDATED_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSpecialLeaves.equals=" + UPDATED_NB_SPECIAL_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSpecialLeavesIsInShouldWork() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSpecialLeaves in DEFAULT_NB_SPECIAL_LEAVES or UPDATED_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbSpecialLeaves.in=" + DEFAULT_NB_SPECIAL_LEAVES + "," + UPDATED_NB_SPECIAL_LEAVES);

        // Get all the leaveConfigList where nbSpecialLeaves equals to UPDATED_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSpecialLeaves.in=" + UPDATED_NB_SPECIAL_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSpecialLeavesIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSpecialLeaves is not null
        defaultLeaveConfigShouldBeFound("nbSpecialLeaves.specified=true");

        // Get all the leaveConfigList where nbSpecialLeaves is null
        defaultLeaveConfigShouldNotBeFound("nbSpecialLeaves.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSpecialLeavesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSpecialLeaves greater than or equals to DEFAULT_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbSpecialLeaves.greaterOrEqualThan=" + DEFAULT_NB_SPECIAL_LEAVES);

        // Get all the leaveConfigList where nbSpecialLeaves greater than or equals to UPDATED_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSpecialLeaves.greaterOrEqualThan=" + UPDATED_NB_SPECIAL_LEAVES);
    }

    @Test
    @Transactional
    public void getAllLeaveConfigsByNbSpecialLeavesIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        // Get all the leaveConfigList where nbSpecialLeaves less than or equals to DEFAULT_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldNotBeFound("nbSpecialLeaves.lessThan=" + DEFAULT_NB_SPECIAL_LEAVES);

        // Get all the leaveConfigList where nbSpecialLeaves less than or equals to UPDATED_NB_SPECIAL_LEAVES
        defaultLeaveConfigShouldBeFound("nbSpecialLeaves.lessThan=" + UPDATED_NB_SPECIAL_LEAVES);
    }


    @Test
    @Transactional
    public void getAllLeaveConfigsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leaveConfig.setUser(user);
        leaveConfigRepository.saveAndFlush(leaveConfig);
        Long userId = user.getId();

        // Get all the leaveConfigList where user equals to userId
        defaultLeaveConfigShouldBeFound("userId.equals=" + userId);

        // Get all the leaveConfigList where user equals to userId + 1
        defaultLeaveConfigShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllLeaveConfigsByApproverIsEqualToSomething() throws Exception {
        // Initialize the database
        User approver = UserResourceIntTest.createEntity(em);
        em.persist(approver);
        em.flush();
        leaveConfig.setApprover(approver);
        leaveConfigRepository.saveAndFlush(leaveConfig);
        Long approverId = approver.getId();

        // Get all the leaveConfigList where approver equals to approverId
        defaultLeaveConfigShouldBeFound("approverId.equals=" + approverId);

        // Get all the leaveConfigList where approver equals to approverId + 1
        defaultLeaveConfigShouldNotBeFound("approverId.equals=" + (approverId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLeaveConfigShouldBeFound(String filter) throws Exception {
        restLeaveConfigMockMvc.perform(get("/api/leave-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbSickLeaves").value(hasItem(DEFAULT_NB_SICK_LEAVES)))
            .andExpect(jsonPath("$.[*].nbAnnualLeaves").value(hasItem(DEFAULT_NB_ANNUAL_LEAVES)))
            .andExpect(jsonPath("$.[*].nbSpecialLeaves").value(hasItem(DEFAULT_NB_SPECIAL_LEAVES)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLeaveConfigShouldNotBeFound(String filter) throws Exception {
        restLeaveConfigMockMvc.perform(get("/api/leave-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingLeaveConfig() throws Exception {
        // Get the leaveConfig
        restLeaveConfigMockMvc.perform(get("/api/leave-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveConfig() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        int databaseSizeBeforeUpdate = leaveConfigRepository.findAll().size();

        // Update the leaveConfig
        LeaveConfig updatedLeaveConfig = leaveConfigRepository.findById(leaveConfig.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveConfig are not directly saved in db
        em.detach(updatedLeaveConfig);
        updatedLeaveConfig
            .nbSickLeaves(UPDATED_NB_SICK_LEAVES)
            .nbAnnualLeaves(UPDATED_NB_ANNUAL_LEAVES)
            .nbSpecialLeaves(UPDATED_NB_SPECIAL_LEAVES);
        LeaveConfigDTO leaveConfigDTO = leaveConfigMapper.toDto(updatedLeaveConfig);

        restLeaveConfigMockMvc.perform(put("/api/leave-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveConfigDTO)))
            .andExpect(status().isOk());

        // Validate the LeaveConfig in the database
        List<LeaveConfig> leaveConfigList = leaveConfigRepository.findAll();
        assertThat(leaveConfigList).hasSize(databaseSizeBeforeUpdate);
        LeaveConfig testLeaveConfig = leaveConfigList.get(leaveConfigList.size() - 1);
        assertThat(testLeaveConfig.getNbSickLeaves()).isEqualTo(UPDATED_NB_SICK_LEAVES);
        assertThat(testLeaveConfig.getNbAnnualLeaves()).isEqualTo(UPDATED_NB_ANNUAL_LEAVES);
        assertThat(testLeaveConfig.getNbSpecialLeaves()).isEqualTo(UPDATED_NB_SPECIAL_LEAVES);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveConfig() throws Exception {
        int databaseSizeBeforeUpdate = leaveConfigRepository.findAll().size();

        // Create the LeaveConfig
        LeaveConfigDTO leaveConfigDTO = leaveConfigMapper.toDto(leaveConfig);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeaveConfigMockMvc.perform(put("/api/leave-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaveConfig in the database
        List<LeaveConfig> leaveConfigList = leaveConfigRepository.findAll();
        assertThat(leaveConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaveConfig() throws Exception {
        // Initialize the database
        leaveConfigRepository.saveAndFlush(leaveConfig);

        int databaseSizeBeforeDelete = leaveConfigRepository.findAll().size();

        // Get the leaveConfig
        restLeaveConfigMockMvc.perform(delete("/api/leave-configs/{id}", leaveConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LeaveConfig> leaveConfigList = leaveConfigRepository.findAll();
        assertThat(leaveConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveConfig.class);
        LeaveConfig leaveConfig1 = new LeaveConfig();
        leaveConfig1.setId(1L);
        LeaveConfig leaveConfig2 = new LeaveConfig();
        leaveConfig2.setId(leaveConfig1.getId());
        assertThat(leaveConfig1).isEqualTo(leaveConfig2);
        leaveConfig2.setId(2L);
        assertThat(leaveConfig1).isNotEqualTo(leaveConfig2);
        leaveConfig1.setId(null);
        assertThat(leaveConfig1).isNotEqualTo(leaveConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveConfigDTO.class);
        LeaveConfigDTO leaveConfigDTO1 = new LeaveConfigDTO();
        leaveConfigDTO1.setId(1L);
        LeaveConfigDTO leaveConfigDTO2 = new LeaveConfigDTO();
        assertThat(leaveConfigDTO1).isNotEqualTo(leaveConfigDTO2);
        leaveConfigDTO2.setId(leaveConfigDTO1.getId());
        assertThat(leaveConfigDTO1).isEqualTo(leaveConfigDTO2);
        leaveConfigDTO2.setId(2L);
        assertThat(leaveConfigDTO1).isNotEqualTo(leaveConfigDTO2);
        leaveConfigDTO1.setId(null);
        assertThat(leaveConfigDTO1).isNotEqualTo(leaveConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leaveConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leaveConfigMapper.fromId(null)).isNull();
    }
}

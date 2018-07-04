package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.LeaveConfig;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.LeaveConfigRepository;
import com.shine.shineappback.service.LeaveConfigService;
import com.shine.shineappback.service.dto.LeaveConfigDTO;
import com.shine.shineappback.service.mapper.LeaveConfigMapper;
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
        final LeaveConfigResource leaveConfigResource = new LeaveConfigResource(leaveConfigService);
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

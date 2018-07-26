package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.ActivityConfig;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.ActivityConfigRepository;
import com.shine.shineappback.service.ActivityConfigService;
import com.shine.shineappback.service.dto.ActivityConfigDTO;
import com.shine.shineappback.service.mapper.ActivityConfigMapper;
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
 * Test class for the ActivityConfigResource REST controller.
 *
 * @see ActivityConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class ActivityConfigResourceIntTest {

    @Autowired
    private ActivityConfigRepository activityConfigRepository;


    @Autowired
    private ActivityConfigMapper activityConfigMapper;
    

    @Autowired
    private ActivityConfigService activityConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityConfigMockMvc;

    private ActivityConfig activityConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityConfigResource activityConfigResource = new ActivityConfigResource(activityConfigService);
        this.restActivityConfigMockMvc = MockMvcBuilders.standaloneSetup(activityConfigResource)
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
    public static ActivityConfig createEntity(EntityManager em) {
        ActivityConfig activityConfig = new ActivityConfig();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        activityConfig.setUser(user);
        return activityConfig;
    }

    @Before
    public void initTest() {
        activityConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityConfig() throws Exception {
        int databaseSizeBeforeCreate = activityConfigRepository.findAll().size();

        // Create the ActivityConfig
        ActivityConfigDTO activityConfigDTO = activityConfigMapper.toDto(activityConfig);
        restActivityConfigMockMvc.perform(post("/api/activity-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityConfig in the database
        List<ActivityConfig> activityConfigList = activityConfigRepository.findAll();
        assertThat(activityConfigList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityConfig testActivityConfig = activityConfigList.get(activityConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void createActivityConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityConfigRepository.findAll().size();

        // Create the ActivityConfig with an existing ID
        activityConfig.setId(1L);
        ActivityConfigDTO activityConfigDTO = activityConfigMapper.toDto(activityConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityConfigMockMvc.perform(post("/api/activity-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityConfig in the database
        List<ActivityConfig> activityConfigList = activityConfigRepository.findAll();
        assertThat(activityConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActivityConfigs() throws Exception {
        // Initialize the database
        activityConfigRepository.saveAndFlush(activityConfig);

        // Get all the activityConfigList
        restActivityConfigMockMvc.perform(get("/api/activity-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityConfig.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getActivityConfig() throws Exception {
        // Initialize the database
        activityConfigRepository.saveAndFlush(activityConfig);

        // Get the activityConfig
        restActivityConfigMockMvc.perform(get("/api/activity-configs/{id}", activityConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityConfig.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingActivityConfig() throws Exception {
        // Get the activityConfig
        restActivityConfigMockMvc.perform(get("/api/activity-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityConfig() throws Exception {
        // Initialize the database
        activityConfigRepository.saveAndFlush(activityConfig);

        int databaseSizeBeforeUpdate = activityConfigRepository.findAll().size();

        // Update the activityConfig
        ActivityConfig updatedActivityConfig = activityConfigRepository.findById(activityConfig.getId()).get();
        // Disconnect from session so that the updates on updatedActivityConfig are not directly saved in db
        em.detach(updatedActivityConfig);
        ActivityConfigDTO activityConfigDTO = activityConfigMapper.toDto(updatedActivityConfig);

        restActivityConfigMockMvc.perform(put("/api/activity-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityConfigDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityConfig in the database
        List<ActivityConfig> activityConfigList = activityConfigRepository.findAll();
        assertThat(activityConfigList).hasSize(databaseSizeBeforeUpdate);
        ActivityConfig testActivityConfig = activityConfigList.get(activityConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityConfig() throws Exception {
        int databaseSizeBeforeUpdate = activityConfigRepository.findAll().size();

        // Create the ActivityConfig
        ActivityConfigDTO activityConfigDTO = activityConfigMapper.toDto(activityConfig);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityConfigMockMvc.perform(put("/api/activity-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityConfig in the database
        List<ActivityConfig> activityConfigList = activityConfigRepository.findAll();
        assertThat(activityConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityConfig() throws Exception {
        // Initialize the database
        activityConfigRepository.saveAndFlush(activityConfig);

        int databaseSizeBeforeDelete = activityConfigRepository.findAll().size();

        // Get the activityConfig
        restActivityConfigMockMvc.perform(delete("/api/activity-configs/{id}", activityConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityConfig> activityConfigList = activityConfigRepository.findAll();
        assertThat(activityConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityConfig.class);
        ActivityConfig activityConfig1 = new ActivityConfig();
        activityConfig1.setId(1L);
        ActivityConfig activityConfig2 = new ActivityConfig();
        activityConfig2.setId(activityConfig1.getId());
        assertThat(activityConfig1).isEqualTo(activityConfig2);
        activityConfig2.setId(2L);
        assertThat(activityConfig1).isNotEqualTo(activityConfig2);
        activityConfig1.setId(null);
        assertThat(activityConfig1).isNotEqualTo(activityConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityConfigDTO.class);
        ActivityConfigDTO activityConfigDTO1 = new ActivityConfigDTO();
        activityConfigDTO1.setId(1L);
        ActivityConfigDTO activityConfigDTO2 = new ActivityConfigDTO();
        assertThat(activityConfigDTO1).isNotEqualTo(activityConfigDTO2);
        activityConfigDTO2.setId(activityConfigDTO1.getId());
        assertThat(activityConfigDTO1).isEqualTo(activityConfigDTO2);
        activityConfigDTO2.setId(2L);
        assertThat(activityConfigDTO1).isNotEqualTo(activityConfigDTO2);
        activityConfigDTO1.setId(null);
        assertThat(activityConfigDTO1).isNotEqualTo(activityConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityConfigMapper.fromId(null)).isNull();
    }
}

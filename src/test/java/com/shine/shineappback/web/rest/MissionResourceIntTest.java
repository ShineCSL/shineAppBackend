package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.Mission;
import com.shine.shineappback.domain.Client;
import com.shine.shineappback.repository.MissionRepository;
import com.shine.shineappback.service.MissionService;
import com.shine.shineappback.service.dto.MissionDTO;
import com.shine.shineappback.service.mapper.MissionMapper;
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
 * Test class for the MissionResource REST controller.
 *
 * @see MissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class MissionResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private MissionRepository missionRepository;


    @Autowired
    private MissionMapper missionMapper;
    

    @Autowired
    private MissionService missionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMissionMockMvc;

    private Mission mission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MissionResource missionResource = new MissionResource(missionService);
        this.restMissionMockMvc = MockMvcBuilders.standaloneSetup(missionResource)
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
    public static Mission createEntity(EntityManager em) {
        Mission mission = new Mission()
            .code(DEFAULT_CODE)
            .label(DEFAULT_LABEL);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        mission.setClient(client);
        return mission;
    }

    @Before
    public void initTest() {
        mission = createEntity(em);
    }

    @Test
    @Transactional
    public void createMission() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);
        restMissionMockMvc.perform(post("/api/missions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(missionDTO)))
            .andExpect(status().isCreated());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeCreate + 1);
        Mission testMission = missionList.get(missionList.size() - 1);
        assertThat(testMission.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMission.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createMissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // Create the Mission with an existing ID
        mission.setId(1L);
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMissionMockMvc.perform(post("/api/missions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(missionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = missionRepository.findAll().size();
        // set the field null
        mission.setCode(null);

        // Create the Mission, which fails.
        MissionDTO missionDTO = missionMapper.toDto(mission);

        restMissionMockMvc.perform(post("/api/missions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(missionDTO)))
            .andExpect(status().isBadRequest());

        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMissions() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missionList
        restMissionMockMvc.perform(get("/api/missions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mission.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }
    

    @Test
    @Transactional
    public void getMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get the mission
        restMissionMockMvc.perform(get("/api/missions/{id}", mission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mission.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMission() throws Exception {
        // Get the mission
        restMissionMockMvc.perform(get("/api/missions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission
        Mission updatedMission = missionRepository.findById(mission.getId()).get();
        // Disconnect from session so that the updates on updatedMission are not directly saved in db
        em.detach(updatedMission);
        updatedMission
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);
        MissionDTO missionDTO = missionMapper.toDto(updatedMission);

        restMissionMockMvc.perform(put("/api/missions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(missionDTO)))
            .andExpect(status().isOk());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missionList.get(missionList.size() - 1);
        assertThat(testMission.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMission.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingMission() throws Exception {
        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Create the Mission
        MissionDTO missionDTO = missionMapper.toDto(mission);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMissionMockMvc.perform(put("/api/missions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(missionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mission in the database
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        int databaseSizeBeforeDelete = missionRepository.findAll().size();

        // Get the mission
        restMissionMockMvc.perform(delete("/api/missions/{id}", mission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mission> missionList = missionRepository.findAll();
        assertThat(missionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mission.class);
        Mission mission1 = new Mission();
        mission1.setId(1L);
        Mission mission2 = new Mission();
        mission2.setId(mission1.getId());
        assertThat(mission1).isEqualTo(mission2);
        mission2.setId(2L);
        assertThat(mission1).isNotEqualTo(mission2);
        mission1.setId(null);
        assertThat(mission1).isNotEqualTo(mission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MissionDTO.class);
        MissionDTO missionDTO1 = new MissionDTO();
        missionDTO1.setId(1L);
        MissionDTO missionDTO2 = new MissionDTO();
        assertThat(missionDTO1).isNotEqualTo(missionDTO2);
        missionDTO2.setId(missionDTO1.getId());
        assertThat(missionDTO1).isEqualTo(missionDTO2);
        missionDTO2.setId(2L);
        assertThat(missionDTO1).isNotEqualTo(missionDTO2);
        missionDTO1.setId(null);
        assertThat(missionDTO1).isNotEqualTo(missionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(missionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(missionMapper.fromId(null)).isNull();
    }
}

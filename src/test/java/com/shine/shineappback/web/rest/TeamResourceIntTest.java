package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.Team;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.TeamRepository;
import com.shine.shineappback.service.TeamService;
import com.shine.shineappback.service.dto.TeamDTO;
import com.shine.shineappback.service.mapper.TeamMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.TeamCriteria;
import com.shine.shineappback.service.TeamQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static com.shine.shineappback.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class TeamResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private TeamRepository teamRepository;
    @Mock
    private TeamRepository teamRepositoryMock;

    @Autowired
    private TeamMapper teamMapper;
    
    @Mock
    private TeamService teamServiceMock;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamQueryService teamQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamMockMvc;

    private Team team;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamResource teamResource = new TeamResource(teamService, teamQueryService);
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
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
    public static Team createEntity(EntityManager em) {
        Team team = new Team()
            .code(DEFAULT_CODE)
            .label(DEFAULT_LABEL);
        return team;
    }

    @Before
    public void initTest() {
        team = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeam() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team
        TeamDTO teamDTO = teamMapper.toDto(team);
        restTeamMockMvc.perform(post("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
            .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate + 1);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTeam.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team with an existing ID
        team.setId(1L);
        TeamDTO teamDTO = teamMapper.toDto(team);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMockMvc.perform(post("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamRepository.findAll().size();
        // set the field null
        team.setCode(null);

        // Create the Team, which fails.
        TeamDTO teamDTO = teamMapper.toDto(team);

        restTeamMockMvc.perform(post("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
            .andExpect(status().isBadRequest());

        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }
    
    public void getAllTeamsWithEagerRelationshipsIsEnabled() throws Exception {
        TeamResource teamResource = new TeamResource(teamServiceMock, teamQueryService);
        when(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTeamMockMvc.perform(get("/api/teams?eagerload=true"))
        .andExpect(status().isOk());

        verify(teamServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllTeamsWithEagerRelationshipsIsNotEnabled() throws Exception {
        TeamResource teamResource = new TeamResource(teamServiceMock, teamQueryService);
            when(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTeamMockMvc.perform(get("/api/teams?eagerload=true"))
        .andExpect(status().isOk());

            verify(teamServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getAllTeamsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where code equals to DEFAULT_CODE
        defaultTeamShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the teamList where code equals to UPDATED_CODE
        defaultTeamShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTeamsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where code in DEFAULT_CODE or UPDATED_CODE
        defaultTeamShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the teamList where code equals to UPDATED_CODE
        defaultTeamShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTeamsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where code is not null
        defaultTeamShouldBeFound("code.specified=true");

        // Get all the teamList where code is null
        defaultTeamShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where label equals to DEFAULT_LABEL
        defaultTeamShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the teamList where label equals to UPDATED_LABEL
        defaultTeamShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllTeamsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultTeamShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the teamList where label equals to UPDATED_LABEL
        defaultTeamShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllTeamsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where label is not null
        defaultTeamShouldBeFound("label.specified=true");

        // Get all the teamList where label is null
        defaultTeamShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamsBySupervisorIsEqualToSomething() throws Exception {
        // Initialize the database
        User supervisor = UserResourceIntTest.createEntity(em);
        em.persist(supervisor);
        em.flush();
        team.setSupervisor(supervisor);
        teamRepository.saveAndFlush(team);
        Long supervisorId = supervisor.getId();

        // Get all the teamList where supervisor equals to supervisorId
        defaultTeamShouldBeFound("supervisorId.equals=" + supervisorId);

        // Get all the teamList where supervisor equals to supervisorId + 1
        defaultTeamShouldNotBeFound("supervisorId.equals=" + (supervisorId + 1));
    }


    @Test
    @Transactional
    public void getAllTeamsByResourcesIsEqualToSomething() throws Exception {
        // Initialize the database
        User resources = UserResourceIntTest.createEntity(em);
        em.persist(resources);
        em.flush();
        //team.addResources(resources);
        teamRepository.saveAndFlush(team);
        Long resourcesId = resources.getId();

        // Get all the teamList where resources equals to resourcesId
        defaultTeamShouldBeFound("resourcesId.equals=" + resourcesId);

        // Get all the teamList where resources equals to resourcesId + 1
        defaultTeamShouldNotBeFound("resourcesId.equals=" + (resourcesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTeamShouldBeFound(String filter) throws Exception {
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTeamShouldNotBeFound(String filter) throws Exception {
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Update the team
        Team updatedTeam = teamRepository.findById(team.getId()).get();
        // Disconnect from session so that the updates on updatedTeam are not directly saved in db
        em.detach(updatedTeam);
        updatedTeam
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);
        TeamDTO teamDTO = teamMapper.toDto(updatedTeam);

        restTeamMockMvc.perform(put("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
            .andExpect(status().isOk());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTeam.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingTeam() throws Exception {
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Create the Team
        TeamDTO teamDTO = teamMapper.toDto(team);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamMockMvc.perform(put("/api/teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        int databaseSizeBeforeDelete = teamRepository.findAll().size();

        // Get the team
        restTeamMockMvc.perform(delete("/api/teams/{id}", team.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Team.class);
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(team1.getId());
        assertThat(team1).isEqualTo(team2);
        team2.setId(2L);
        assertThat(team1).isNotEqualTo(team2);
        team1.setId(null);
        assertThat(team1).isNotEqualTo(team2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamDTO.class);
        TeamDTO teamDTO1 = new TeamDTO();
        teamDTO1.setId(1L);
        TeamDTO teamDTO2 = new TeamDTO();
        assertThat(teamDTO1).isNotEqualTo(teamDTO2);
        teamDTO2.setId(teamDTO1.getId());
        assertThat(teamDTO1).isEqualTo(teamDTO2);
        teamDTO2.setId(2L);
        assertThat(teamDTO1).isNotEqualTo(teamDTO2);
        teamDTO1.setId(null);
        assertThat(teamDTO1).isNotEqualTo(teamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(teamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(teamMapper.fromId(null)).isNull();
    }
}

package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.Leaves;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.Task;
import com.shine.shineappback.repository.LeavesRepository;
import com.shine.shineappback.service.LeavesService;
import com.shine.shineappback.service.dto.LeavesDTO;
import com.shine.shineappback.service.mapper.LeavesMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.shine.shineappback.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LeavesResource REST controller.
 *
 * @see LeavesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class LeavesResourceIntTest {

    private static final LocalDate DEFAULT_LEAVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_NB_OF_HOURS = 1D;
    private static final Double UPDATED_NB_OF_HOURS = 2D;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    @Autowired
    private LeavesRepository leavesRepository;


    @Autowired
    private LeavesMapper leavesMapper;
    

    @Autowired
    private LeavesService leavesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeavesMockMvc;

    private Leaves leaves;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeavesResource leavesResource = new LeavesResource(leavesService);
        this.restLeavesMockMvc = MockMvcBuilders.standaloneSetup(leavesResource)
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
    public static Leaves createEntity(EntityManager em) {
        Leaves leaves = new Leaves()
            .leaveDate(DEFAULT_LEAVE_DATE)
            .nbOfHours(DEFAULT_NB_OF_HOURS)
            .year(DEFAULT_YEAR)
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .comment(DEFAULT_COMMENT)
            .day(DEFAULT_DAY);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leaves.setUser(user);
        // Add required entity
        Task task = TaskResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        leaves.setTask(task);
        return leaves;
    }

    @Before
    public void initTest() {
        leaves = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaves() throws Exception {
        int databaseSizeBeforeCreate = leavesRepository.findAll().size();

        // Create the Leaves
        LeavesDTO leavesDTO = leavesMapper.toDto(leaves);
        restLeavesMockMvc.perform(post("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isCreated());

        // Validate the Leaves in the database
        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeCreate + 1);
        Leaves testLeaves = leavesList.get(leavesList.size() - 1);
        assertThat(testLeaves.getLeaveDate()).isEqualTo(DEFAULT_LEAVE_DATE);
        assertThat(testLeaves.getNbOfHours()).isEqualTo(DEFAULT_NB_OF_HOURS);
        assertThat(testLeaves.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testLeaves.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testLeaves.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testLeaves.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    public void createLeavesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leavesRepository.findAll().size();

        // Create the Leaves with an existing ID
        leaves.setId(1L);
        LeavesDTO leavesDTO = leavesMapper.toDto(leaves);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeavesMockMvc.perform(post("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Leaves in the database
        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLeaveDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRepository.findAll().size();
        // set the field null
        leaves.setLeaveDate(null);

        // Create the Leaves, which fails.
        LeavesDTO leavesDTO = leavesMapper.toDto(leaves);

        restLeavesMockMvc.perform(post("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isBadRequest());

        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbOfHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRepository.findAll().size();
        // set the field null
        leaves.setNbOfHours(null);

        // Create the Leaves, which fails.
        LeavesDTO leavesDTO = leavesMapper.toDto(leaves);

        restLeavesMockMvc.perform(post("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isBadRequest());

        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRepository.findAll().size();
        // set the field null
        leaves.setDay(null);

        // Create the Leaves, which fails.
        LeavesDTO leavesDTO = leavesMapper.toDto(leaves);

        restLeavesMockMvc.perform(post("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isBadRequest());

        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeaves() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList
        restLeavesMockMvc.perform(get("/api/leaves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaves.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaveDate").value(hasItem(DEFAULT_LEAVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].nbOfHours").value(hasItem(DEFAULT_NB_OF_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)));
    }
    

    @Test
    @Transactional
    public void getLeaves() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get the leaves
        restLeavesMockMvc.perform(get("/api/leaves/{id}", leaves.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leaves.getId().intValue()))
            .andExpect(jsonPath("$.leaveDate").value(DEFAULT_LEAVE_DATE.toString()))
            .andExpect(jsonPath("$.nbOfHours").value(DEFAULT_NB_OF_HOURS.doubleValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY));
    }
    @Test
    @Transactional
    public void getNonExistingLeaves() throws Exception {
        // Get the leaves
        restLeavesMockMvc.perform(get("/api/leaves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaves() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        int databaseSizeBeforeUpdate = leavesRepository.findAll().size();

        // Update the leaves
        Leaves updatedLeaves = leavesRepository.findById(leaves.getId()).get();
        // Disconnect from session so that the updates on updatedLeaves are not directly saved in db
        em.detach(updatedLeaves);
        updatedLeaves
            .leaveDate(UPDATED_LEAVE_DATE)
            .nbOfHours(UPDATED_NB_OF_HOURS)
            .year(UPDATED_YEAR)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .comment(UPDATED_COMMENT)
            .day(UPDATED_DAY);
        LeavesDTO leavesDTO = leavesMapper.toDto(updatedLeaves);

        restLeavesMockMvc.perform(put("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isOk());

        // Validate the Leaves in the database
        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeUpdate);
        Leaves testLeaves = leavesList.get(leavesList.size() - 1);
        assertThat(testLeaves.getLeaveDate()).isEqualTo(UPDATED_LEAVE_DATE);
        assertThat(testLeaves.getNbOfHours()).isEqualTo(UPDATED_NB_OF_HOURS);
        assertThat(testLeaves.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testLeaves.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testLeaves.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testLeaves.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaves() throws Exception {
        int databaseSizeBeforeUpdate = leavesRepository.findAll().size();

        // Create the Leaves
        LeavesDTO leavesDTO = leavesMapper.toDto(leaves);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeavesMockMvc.perform(put("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Leaves in the database
        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaves() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        int databaseSizeBeforeDelete = leavesRepository.findAll().size();

        // Get the leaves
        restLeavesMockMvc.perform(delete("/api/leaves/{id}", leaves.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Leaves.class);
        Leaves leaves1 = new Leaves();
        leaves1.setId(1L);
        Leaves leaves2 = new Leaves();
        leaves2.setId(leaves1.getId());
        assertThat(leaves1).isEqualTo(leaves2);
        leaves2.setId(2L);
        assertThat(leaves1).isNotEqualTo(leaves2);
        leaves1.setId(null);
        assertThat(leaves1).isNotEqualTo(leaves2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesDTO.class);
        LeavesDTO leavesDTO1 = new LeavesDTO();
        leavesDTO1.setId(1L);
        LeavesDTO leavesDTO2 = new LeavesDTO();
        assertThat(leavesDTO1).isNotEqualTo(leavesDTO2);
        leavesDTO2.setId(leavesDTO1.getId());
        assertThat(leavesDTO1).isEqualTo(leavesDTO2);
        leavesDTO2.setId(2L);
        assertThat(leavesDTO1).isNotEqualTo(leavesDTO2);
        leavesDTO1.setId(null);
        assertThat(leavesDTO1).isNotEqualTo(leavesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leavesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leavesMapper.fromId(null)).isNull();
    }
}

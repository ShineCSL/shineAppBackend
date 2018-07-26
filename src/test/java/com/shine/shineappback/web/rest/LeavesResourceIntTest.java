package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.Leaves;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.Task;
import com.shine.shineappback.domain.LeavesSubmission;
import com.shine.shineappback.domain.LeavesValidation;
import com.shine.shineappback.domain.LeavesRejection;
import com.shine.shineappback.repository.LeavesRepository;
import com.shine.shineappback.service.LeavesService;
import com.shine.shineappback.service.dto.LeavesDTO;
import com.shine.shineappback.service.mapper.LeavesMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.LeavesCriteria;
import com.shine.shineappback.service.LeavesQueryService;

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

    private static final Boolean DEFAULT_FULL_DAY = false;
    private static final Boolean UPDATED_FULL_DAY = true;

    private static final LocalDate DEFAULT_LEAVES_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVES_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LEAVES_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVES_TO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NB_OF_HOURS = 0;
    private static final Integer UPDATED_NB_OF_HOURS = 1;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    @Autowired
    private LeavesRepository leavesRepository;


    @Autowired
    private LeavesMapper leavesMapper;
    

    @Autowired
    private LeavesService leavesService;

    @Autowired
    private LeavesQueryService leavesQueryService;

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
        final LeavesResource leavesResource = new LeavesResource(leavesService, leavesQueryService);
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
            .fullDay(DEFAULT_FULL_DAY)
            .leavesFrom(DEFAULT_LEAVES_FROM)
            .leavesTo(DEFAULT_LEAVES_TO)
            .nbOfHours(DEFAULT_NB_OF_HOURS)
            .year(DEFAULT_YEAR)
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .comment(DEFAULT_COMMENT)
            .day(DEFAULT_DAY)
            .month(DEFAULT_MONTH);
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
        assertThat(testLeaves.isFullDay()).isEqualTo(DEFAULT_FULL_DAY);
        assertThat(testLeaves.getLeavesFrom()).isEqualTo(DEFAULT_LEAVES_FROM);
        assertThat(testLeaves.getLeavesTo()).isEqualTo(DEFAULT_LEAVES_TO);
        assertThat(testLeaves.getNbOfHours()).isEqualTo(DEFAULT_NB_OF_HOURS);
        assertThat(testLeaves.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testLeaves.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testLeaves.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testLeaves.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testLeaves.getMonth()).isEqualTo(DEFAULT_MONTH);
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
    public void checkLeavesFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRepository.findAll().size();
        // set the field null
        leaves.setLeavesFrom(null);

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
    public void checkLeavesToIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRepository.findAll().size();
        // set the field null
        leaves.setLeavesTo(null);

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
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRepository.findAll().size();
        // set the field null
        leaves.setMonth(null);

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
            .andExpect(jsonPath("$.[*].fullDay").value(hasItem(DEFAULT_FULL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].leavesFrom").value(hasItem(DEFAULT_LEAVES_FROM.toString())))
            .andExpect(jsonPath("$.[*].leavesTo").value(hasItem(DEFAULT_LEAVES_TO.toString())))
            .andExpect(jsonPath("$.[*].nbOfHours").value(hasItem(DEFAULT_NB_OF_HOURS)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
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
            .andExpect(jsonPath("$.fullDay").value(DEFAULT_FULL_DAY.booleanValue()))
            .andExpect(jsonPath("$.leavesFrom").value(DEFAULT_LEAVES_FROM.toString()))
            .andExpect(jsonPath("$.leavesTo").value(DEFAULT_LEAVES_TO.toString()))
            .andExpect(jsonPath("$.nbOfHours").value(DEFAULT_NB_OF_HOURS))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getAllLeavesByFullDayIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where fullDay equals to DEFAULT_FULL_DAY
        defaultLeavesShouldBeFound("fullDay.equals=" + DEFAULT_FULL_DAY);

        // Get all the leavesList where fullDay equals to UPDATED_FULL_DAY
        defaultLeavesShouldNotBeFound("fullDay.equals=" + UPDATED_FULL_DAY);
    }

    @Test
    @Transactional
    public void getAllLeavesByFullDayIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where fullDay in DEFAULT_FULL_DAY or UPDATED_FULL_DAY
        defaultLeavesShouldBeFound("fullDay.in=" + DEFAULT_FULL_DAY + "," + UPDATED_FULL_DAY);

        // Get all the leavesList where fullDay equals to UPDATED_FULL_DAY
        defaultLeavesShouldNotBeFound("fullDay.in=" + UPDATED_FULL_DAY);
    }

    @Test
    @Transactional
    public void getAllLeavesByFullDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where fullDay is not null
        defaultLeavesShouldBeFound("fullDay.specified=true");

        // Get all the leavesList where fullDay is null
        defaultLeavesShouldNotBeFound("fullDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesFromIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesFrom equals to DEFAULT_LEAVES_FROM
        defaultLeavesShouldBeFound("leavesFrom.equals=" + DEFAULT_LEAVES_FROM);

        // Get all the leavesList where leavesFrom equals to UPDATED_LEAVES_FROM
        defaultLeavesShouldNotBeFound("leavesFrom.equals=" + UPDATED_LEAVES_FROM);
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesFromIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesFrom in DEFAULT_LEAVES_FROM or UPDATED_LEAVES_FROM
        defaultLeavesShouldBeFound("leavesFrom.in=" + DEFAULT_LEAVES_FROM + "," + UPDATED_LEAVES_FROM);

        // Get all the leavesList where leavesFrom equals to UPDATED_LEAVES_FROM
        defaultLeavesShouldNotBeFound("leavesFrom.in=" + UPDATED_LEAVES_FROM);
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesFrom is not null
        defaultLeavesShouldBeFound("leavesFrom.specified=true");

        // Get all the leavesList where leavesFrom is null
        defaultLeavesShouldNotBeFound("leavesFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesFrom greater than or equals to DEFAULT_LEAVES_FROM
        defaultLeavesShouldBeFound("leavesFrom.greaterOrEqualThan=" + DEFAULT_LEAVES_FROM);

        // Get all the leavesList where leavesFrom greater than or equals to UPDATED_LEAVES_FROM
        defaultLeavesShouldNotBeFound("leavesFrom.greaterOrEqualThan=" + UPDATED_LEAVES_FROM);
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesFromIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesFrom less than or equals to DEFAULT_LEAVES_FROM
        defaultLeavesShouldNotBeFound("leavesFrom.lessThan=" + DEFAULT_LEAVES_FROM);

        // Get all the leavesList where leavesFrom less than or equals to UPDATED_LEAVES_FROM
        defaultLeavesShouldBeFound("leavesFrom.lessThan=" + UPDATED_LEAVES_FROM);
    }


    @Test
    @Transactional
    public void getAllLeavesByLeavesToIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesTo equals to DEFAULT_LEAVES_TO
        defaultLeavesShouldBeFound("leavesTo.equals=" + DEFAULT_LEAVES_TO);

        // Get all the leavesList where leavesTo equals to UPDATED_LEAVES_TO
        defaultLeavesShouldNotBeFound("leavesTo.equals=" + UPDATED_LEAVES_TO);
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesToIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesTo in DEFAULT_LEAVES_TO or UPDATED_LEAVES_TO
        defaultLeavesShouldBeFound("leavesTo.in=" + DEFAULT_LEAVES_TO + "," + UPDATED_LEAVES_TO);

        // Get all the leavesList where leavesTo equals to UPDATED_LEAVES_TO
        defaultLeavesShouldNotBeFound("leavesTo.in=" + UPDATED_LEAVES_TO);
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesToIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesTo is not null
        defaultLeavesShouldBeFound("leavesTo.specified=true");

        // Get all the leavesList where leavesTo is null
        defaultLeavesShouldNotBeFound("leavesTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesTo greater than or equals to DEFAULT_LEAVES_TO
        defaultLeavesShouldBeFound("leavesTo.greaterOrEqualThan=" + DEFAULT_LEAVES_TO);

        // Get all the leavesList where leavesTo greater than or equals to UPDATED_LEAVES_TO
        defaultLeavesShouldNotBeFound("leavesTo.greaterOrEqualThan=" + UPDATED_LEAVES_TO);
    }

    @Test
    @Transactional
    public void getAllLeavesByLeavesToIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where leavesTo less than or equals to DEFAULT_LEAVES_TO
        defaultLeavesShouldNotBeFound("leavesTo.lessThan=" + DEFAULT_LEAVES_TO);

        // Get all the leavesList where leavesTo less than or equals to UPDATED_LEAVES_TO
        defaultLeavesShouldBeFound("leavesTo.lessThan=" + UPDATED_LEAVES_TO);
    }


    @Test
    @Transactional
    public void getAllLeavesByNbOfHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where nbOfHours equals to DEFAULT_NB_OF_HOURS
        defaultLeavesShouldBeFound("nbOfHours.equals=" + DEFAULT_NB_OF_HOURS);

        // Get all the leavesList where nbOfHours equals to UPDATED_NB_OF_HOURS
        defaultLeavesShouldNotBeFound("nbOfHours.equals=" + UPDATED_NB_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllLeavesByNbOfHoursIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where nbOfHours in DEFAULT_NB_OF_HOURS or UPDATED_NB_OF_HOURS
        defaultLeavesShouldBeFound("nbOfHours.in=" + DEFAULT_NB_OF_HOURS + "," + UPDATED_NB_OF_HOURS);

        // Get all the leavesList where nbOfHours equals to UPDATED_NB_OF_HOURS
        defaultLeavesShouldNotBeFound("nbOfHours.in=" + UPDATED_NB_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllLeavesByNbOfHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where nbOfHours is not null
        defaultLeavesShouldBeFound("nbOfHours.specified=true");

        // Get all the leavesList where nbOfHours is null
        defaultLeavesShouldNotBeFound("nbOfHours.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByNbOfHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where nbOfHours greater than or equals to DEFAULT_NB_OF_HOURS
        defaultLeavesShouldBeFound("nbOfHours.greaterOrEqualThan=" + DEFAULT_NB_OF_HOURS);

        // Get all the leavesList where nbOfHours greater than or equals to (DEFAULT_NB_OF_HOURS + 1)
        defaultLeavesShouldNotBeFound("nbOfHours.greaterOrEqualThan=" + (DEFAULT_NB_OF_HOURS + 1));
    }

    @Test
    @Transactional
    public void getAllLeavesByNbOfHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where nbOfHours less than or equals to DEFAULT_NB_OF_HOURS
        defaultLeavesShouldNotBeFound("nbOfHours.lessThan=" + DEFAULT_NB_OF_HOURS);

        // Get all the leavesList where nbOfHours less than or equals to (DEFAULT_NB_OF_HOURS + 1)
        defaultLeavesShouldBeFound("nbOfHours.lessThan=" + (DEFAULT_NB_OF_HOURS + 1));
    }


    @Test
    @Transactional
    public void getAllLeavesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where year equals to DEFAULT_YEAR
        defaultLeavesShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the leavesList where year equals to UPDATED_YEAR
        defaultLeavesShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllLeavesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultLeavesShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the leavesList where year equals to UPDATED_YEAR
        defaultLeavesShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllLeavesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where year is not null
        defaultLeavesShouldBeFound("year.specified=true");

        // Get all the leavesList where year is null
        defaultLeavesShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where year greater than or equals to DEFAULT_YEAR
        defaultLeavesShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the leavesList where year greater than or equals to UPDATED_YEAR
        defaultLeavesShouldNotBeFound("year.greaterOrEqualThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllLeavesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where year less than or equals to DEFAULT_YEAR
        defaultLeavesShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the leavesList where year less than or equals to UPDATED_YEAR
        defaultLeavesShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }


    @Test
    @Transactional
    public void getAllLeavesByWeekNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where weekNumber equals to DEFAULT_WEEK_NUMBER
        defaultLeavesShouldBeFound("weekNumber.equals=" + DEFAULT_WEEK_NUMBER);

        // Get all the leavesList where weekNumber equals to UPDATED_WEEK_NUMBER
        defaultLeavesShouldNotBeFound("weekNumber.equals=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeavesByWeekNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where weekNumber in DEFAULT_WEEK_NUMBER or UPDATED_WEEK_NUMBER
        defaultLeavesShouldBeFound("weekNumber.in=" + DEFAULT_WEEK_NUMBER + "," + UPDATED_WEEK_NUMBER);

        // Get all the leavesList where weekNumber equals to UPDATED_WEEK_NUMBER
        defaultLeavesShouldNotBeFound("weekNumber.in=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeavesByWeekNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where weekNumber is not null
        defaultLeavesShouldBeFound("weekNumber.specified=true");

        // Get all the leavesList where weekNumber is null
        defaultLeavesShouldNotBeFound("weekNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByWeekNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where weekNumber greater than or equals to DEFAULT_WEEK_NUMBER
        defaultLeavesShouldBeFound("weekNumber.greaterOrEqualThan=" + DEFAULT_WEEK_NUMBER);

        // Get all the leavesList where weekNumber greater than or equals to UPDATED_WEEK_NUMBER
        defaultLeavesShouldNotBeFound("weekNumber.greaterOrEqualThan=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeavesByWeekNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where weekNumber less than or equals to DEFAULT_WEEK_NUMBER
        defaultLeavesShouldNotBeFound("weekNumber.lessThan=" + DEFAULT_WEEK_NUMBER);

        // Get all the leavesList where weekNumber less than or equals to UPDATED_WEEK_NUMBER
        defaultLeavesShouldBeFound("weekNumber.lessThan=" + UPDATED_WEEK_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLeavesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where comment equals to DEFAULT_COMMENT
        defaultLeavesShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the leavesList where comment equals to UPDATED_COMMENT
        defaultLeavesShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllLeavesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultLeavesShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the leavesList where comment equals to UPDATED_COMMENT
        defaultLeavesShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllLeavesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where comment is not null
        defaultLeavesShouldBeFound("comment.specified=true");

        // Get all the leavesList where comment is null
        defaultLeavesShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where day equals to DEFAULT_DAY
        defaultLeavesShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the leavesList where day equals to UPDATED_DAY
        defaultLeavesShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllLeavesByDayIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where day in DEFAULT_DAY or UPDATED_DAY
        defaultLeavesShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the leavesList where day equals to UPDATED_DAY
        defaultLeavesShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllLeavesByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where day is not null
        defaultLeavesShouldBeFound("day.specified=true");

        // Get all the leavesList where day is null
        defaultLeavesShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where day greater than or equals to DEFAULT_DAY
        defaultLeavesShouldBeFound("day.greaterOrEqualThan=" + DEFAULT_DAY);

        // Get all the leavesList where day greater than or equals to UPDATED_DAY
        defaultLeavesShouldNotBeFound("day.greaterOrEqualThan=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllLeavesByDayIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where day less than or equals to DEFAULT_DAY
        defaultLeavesShouldNotBeFound("day.lessThan=" + DEFAULT_DAY);

        // Get all the leavesList where day less than or equals to UPDATED_DAY
        defaultLeavesShouldBeFound("day.lessThan=" + UPDATED_DAY);
    }


    @Test
    @Transactional
    public void getAllLeavesByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where month equals to DEFAULT_MONTH
        defaultLeavesShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the leavesList where month equals to UPDATED_MONTH
        defaultLeavesShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllLeavesByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultLeavesShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the leavesList where month equals to UPDATED_MONTH
        defaultLeavesShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllLeavesByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where month is not null
        defaultLeavesShouldBeFound("month.specified=true");

        // Get all the leavesList where month is null
        defaultLeavesShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeavesByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where month greater than or equals to DEFAULT_MONTH
        defaultLeavesShouldBeFound("month.greaterOrEqualThan=" + DEFAULT_MONTH);

        // Get all the leavesList where month greater than or equals to UPDATED_MONTH
        defaultLeavesShouldNotBeFound("month.greaterOrEqualThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllLeavesByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        leavesRepository.saveAndFlush(leaves);

        // Get all the leavesList where month less than or equals to DEFAULT_MONTH
        defaultLeavesShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the leavesList where month less than or equals to UPDATED_MONTH
        defaultLeavesShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }


    @Test
    @Transactional
    public void getAllLeavesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leaves.setUser(user);
        leavesRepository.saveAndFlush(leaves);
        Long userId = user.getId();

        // Get all the leavesList where user equals to userId
        defaultLeavesShouldBeFound("userId.equals=" + userId);

        // Get all the leavesList where user equals to userId + 1
        defaultLeavesShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllLeavesByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        Task task = TaskResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        leaves.setTask(task);
        leavesRepository.saveAndFlush(leaves);
        Long taskId = task.getId();

        // Get all the leavesList where task equals to taskId
        defaultLeavesShouldBeFound("taskId.equals=" + taskId);

        // Get all the leavesList where task equals to taskId + 1
        defaultLeavesShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }


    @Test
    @Transactional
    public void getAllLeavesByLeavesSubmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        LeavesSubmission leavesSubmission = LeavesSubmissionResourceIntTest.createEntity(em);
        em.persist(leavesSubmission);
        em.flush();
        leaves.setLeavesSubmission(leavesSubmission);
        leavesRepository.saveAndFlush(leaves);
        Long leavesSubmissionId = leavesSubmission.getId();

        // Get all the leavesList where leavesSubmission equals to leavesSubmissionId
        defaultLeavesShouldBeFound("leavesSubmissionId.equals=" + leavesSubmissionId);

        // Get all the leavesList where leavesSubmission equals to leavesSubmissionId + 1
        defaultLeavesShouldNotBeFound("leavesSubmissionId.equals=" + (leavesSubmissionId + 1));
    }


    @Test
    @Transactional
    public void getAllLeavesByLeavesValidationIsEqualToSomething() throws Exception {
        // Initialize the database
        LeavesValidation leavesValidation = LeavesValidationResourceIntTest.createEntity(em);
        em.persist(leavesValidation);
        em.flush();
        leaves.setLeavesValidation(leavesValidation);
        leavesRepository.saveAndFlush(leaves);
        Long leavesValidationId = leavesValidation.getId();

        // Get all the leavesList where leavesValidation equals to leavesValidationId
        defaultLeavesShouldBeFound("leavesValidationId.equals=" + leavesValidationId);

        // Get all the leavesList where leavesValidation equals to leavesValidationId + 1
        defaultLeavesShouldNotBeFound("leavesValidationId.equals=" + (leavesValidationId + 1));
    }


    @Test
    @Transactional
    public void getAllLeavesByLeavesRejectionIsEqualToSomething() throws Exception {
        // Initialize the database
        LeavesRejection leavesRejection = LeavesRejectionResourceIntTest.createEntity(em);
        em.persist(leavesRejection);
        em.flush();
        leaves.setLeavesRejection(leavesRejection);
        leavesRepository.saveAndFlush(leaves);
        Long leavesRejectionId = leavesRejection.getId();

        // Get all the leavesList where leavesRejection equals to leavesRejectionId
        defaultLeavesShouldBeFound("leavesRejectionId.equals=" + leavesRejectionId);

        // Get all the leavesList where leavesRejection equals to leavesRejectionId + 1
        defaultLeavesShouldNotBeFound("leavesRejectionId.equals=" + (leavesRejectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLeavesShouldBeFound(String filter) throws Exception {
        restLeavesMockMvc.perform(get("/api/leaves?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaves.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullDay").value(hasItem(DEFAULT_FULL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].leavesFrom").value(hasItem(DEFAULT_LEAVES_FROM.toString())))
            .andExpect(jsonPath("$.[*].leavesTo").value(hasItem(DEFAULT_LEAVES_TO.toString())))
            .andExpect(jsonPath("$.[*].nbOfHours").value(hasItem(DEFAULT_NB_OF_HOURS)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLeavesShouldNotBeFound(String filter) throws Exception {
        restLeavesMockMvc.perform(get("/api/leaves?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
            .fullDay(UPDATED_FULL_DAY)
            .leavesFrom(UPDATED_LEAVES_FROM)
            .leavesTo(UPDATED_LEAVES_TO)
            .nbOfHours(UPDATED_NB_OF_HOURS)
            .year(UPDATED_YEAR)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .comment(UPDATED_COMMENT)
            .day(UPDATED_DAY)
            .month(UPDATED_MONTH);
        LeavesDTO leavesDTO = leavesMapper.toDto(updatedLeaves);

        restLeavesMockMvc.perform(put("/api/leaves")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesDTO)))
            .andExpect(status().isOk());

        // Validate the Leaves in the database
        List<Leaves> leavesList = leavesRepository.findAll();
        assertThat(leavesList).hasSize(databaseSizeBeforeUpdate);
        Leaves testLeaves = leavesList.get(leavesList.size() - 1);
        assertThat(testLeaves.isFullDay()).isEqualTo(UPDATED_FULL_DAY);
        assertThat(testLeaves.getLeavesFrom()).isEqualTo(UPDATED_LEAVES_FROM);
        assertThat(testLeaves.getLeavesTo()).isEqualTo(UPDATED_LEAVES_TO);
        assertThat(testLeaves.getNbOfHours()).isEqualTo(UPDATED_NB_OF_HOURS);
        assertThat(testLeaves.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testLeaves.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testLeaves.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testLeaves.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testLeaves.getMonth()).isEqualTo(UPDATED_MONTH);
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

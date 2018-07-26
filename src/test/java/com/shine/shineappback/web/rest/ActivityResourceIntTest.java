package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.Activity;
import com.shine.shineappback.domain.Task;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.Mission;
import com.shine.shineappback.domain.ActivityRejection;
import com.shine.shineappback.domain.ActivitySubmission;
import com.shine.shineappback.domain.ActivityValidation;
import com.shine.shineappback.repository.ActivityRepository;
import com.shine.shineappback.service.ActivityService;
import com.shine.shineappback.service.dto.ActivityDTO;
import com.shine.shineappback.service.mapper.ActivityMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.ActivityCriteria;
import com.shine.shineappback.service.ActivityQueryService;

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
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class ActivityResourceIntTest {

    private static final LocalDate DEFAULT_ACTIVITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVITY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_NB_OF_HOURS = 1D;
    private static final Double UPDATED_NB_OF_HOURS = 2D;

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    @Autowired
    private ActivityRepository activityRepository;


    @Autowired
    private ActivityMapper activityMapper;
    

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityQueryService activityQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityResource activityResource = new ActivityResource(activityService, activityQueryService);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
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
    public static Activity createEntity(EntityManager em) {
        Activity activity = new Activity()
            .activityDate(DEFAULT_ACTIVITY_DATE)
            .nbOfHours(DEFAULT_NB_OF_HOURS)
            .day(DEFAULT_DAY)
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .year(DEFAULT_YEAR)
            .month(DEFAULT_MONTH);
        // Add required entity
        Task task = TaskResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        activity.setTask(task);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        activity.setUser(user);
        // Add required entity
        Mission mission = MissionResourceIntTest.createEntity(em);
        em.persist(mission);
        em.flush();
        activity.setMission(mission);
        return activity;
    }

    @Before
    public void initTest() {
        activity = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);
        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getActivityDate()).isEqualTo(DEFAULT_ACTIVITY_DATE);
        assertThat(testActivity.getNbOfHours()).isEqualTo(DEFAULT_NB_OF_HOURS);
        assertThat(testActivity.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testActivity.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testActivity.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testActivity.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    public void createActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity with an existing ID
        activity.setId(1L);
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivityDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setActivityDate(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbOfHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setNbOfHours(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setDay(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setWeekNumber(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setYear(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityRepository.findAll().size();
        // set the field null
        activity.setMonth(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc.perform(post("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivities() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].nbOfHours").value(hasItem(DEFAULT_NB_OF_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }
    

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.activityDate").value(DEFAULT_ACTIVITY_DATE.toString()))
            .andExpect(jsonPath("$.nbOfHours").value(DEFAULT_NB_OF_HOURS.doubleValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getAllActivitiesByActivityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityDate equals to DEFAULT_ACTIVITY_DATE
        defaultActivityShouldBeFound("activityDate.equals=" + DEFAULT_ACTIVITY_DATE);

        // Get all the activityList where activityDate equals to UPDATED_ACTIVITY_DATE
        defaultActivityShouldNotBeFound("activityDate.equals=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    public void getAllActivitiesByActivityDateIsInShouldWork() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityDate in DEFAULT_ACTIVITY_DATE or UPDATED_ACTIVITY_DATE
        defaultActivityShouldBeFound("activityDate.in=" + DEFAULT_ACTIVITY_DATE + "," + UPDATED_ACTIVITY_DATE);

        // Get all the activityList where activityDate equals to UPDATED_ACTIVITY_DATE
        defaultActivityShouldNotBeFound("activityDate.in=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    public void getAllActivitiesByActivityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityDate is not null
        defaultActivityShouldBeFound("activityDate.specified=true");

        // Get all the activityList where activityDate is null
        defaultActivityShouldNotBeFound("activityDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitiesByActivityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityDate greater than or equals to DEFAULT_ACTIVITY_DATE
        defaultActivityShouldBeFound("activityDate.greaterOrEqualThan=" + DEFAULT_ACTIVITY_DATE);

        // Get all the activityList where activityDate greater than or equals to UPDATED_ACTIVITY_DATE
        defaultActivityShouldNotBeFound("activityDate.greaterOrEqualThan=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    public void getAllActivitiesByActivityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where activityDate less than or equals to DEFAULT_ACTIVITY_DATE
        defaultActivityShouldNotBeFound("activityDate.lessThan=" + DEFAULT_ACTIVITY_DATE);

        // Get all the activityList where activityDate less than or equals to UPDATED_ACTIVITY_DATE
        defaultActivityShouldBeFound("activityDate.lessThan=" + UPDATED_ACTIVITY_DATE);
    }


    @Test
    @Transactional
    public void getAllActivitiesByNbOfHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where nbOfHours equals to DEFAULT_NB_OF_HOURS
        defaultActivityShouldBeFound("nbOfHours.equals=" + DEFAULT_NB_OF_HOURS);

        // Get all the activityList where nbOfHours equals to UPDATED_NB_OF_HOURS
        defaultActivityShouldNotBeFound("nbOfHours.equals=" + UPDATED_NB_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllActivitiesByNbOfHoursIsInShouldWork() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where nbOfHours in DEFAULT_NB_OF_HOURS or UPDATED_NB_OF_HOURS
        defaultActivityShouldBeFound("nbOfHours.in=" + DEFAULT_NB_OF_HOURS + "," + UPDATED_NB_OF_HOURS);

        // Get all the activityList where nbOfHours equals to UPDATED_NB_OF_HOURS
        defaultActivityShouldNotBeFound("nbOfHours.in=" + UPDATED_NB_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllActivitiesByNbOfHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where nbOfHours is not null
        defaultActivityShouldBeFound("nbOfHours.specified=true");

        // Get all the activityList where nbOfHours is null
        defaultActivityShouldNotBeFound("nbOfHours.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitiesByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where day equals to DEFAULT_DAY
        defaultActivityShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the activityList where day equals to UPDATED_DAY
        defaultActivityShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllActivitiesByDayIsInShouldWork() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where day in DEFAULT_DAY or UPDATED_DAY
        defaultActivityShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the activityList where day equals to UPDATED_DAY
        defaultActivityShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllActivitiesByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where day is not null
        defaultActivityShouldBeFound("day.specified=true");

        // Get all the activityList where day is null
        defaultActivityShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitiesByDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where day greater than or equals to DEFAULT_DAY
        defaultActivityShouldBeFound("day.greaterOrEqualThan=" + DEFAULT_DAY);

        // Get all the activityList where day greater than or equals to UPDATED_DAY
        defaultActivityShouldNotBeFound("day.greaterOrEqualThan=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllActivitiesByDayIsLessThanSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where day less than or equals to DEFAULT_DAY
        defaultActivityShouldNotBeFound("day.lessThan=" + DEFAULT_DAY);

        // Get all the activityList where day less than or equals to UPDATED_DAY
        defaultActivityShouldBeFound("day.lessThan=" + UPDATED_DAY);
    }


    @Test
    @Transactional
    public void getAllActivitiesByWeekNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where weekNumber equals to DEFAULT_WEEK_NUMBER
        defaultActivityShouldBeFound("weekNumber.equals=" + DEFAULT_WEEK_NUMBER);

        // Get all the activityList where weekNumber equals to UPDATED_WEEK_NUMBER
        defaultActivityShouldNotBeFound("weekNumber.equals=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllActivitiesByWeekNumberIsInShouldWork() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where weekNumber in DEFAULT_WEEK_NUMBER or UPDATED_WEEK_NUMBER
        defaultActivityShouldBeFound("weekNumber.in=" + DEFAULT_WEEK_NUMBER + "," + UPDATED_WEEK_NUMBER);

        // Get all the activityList where weekNumber equals to UPDATED_WEEK_NUMBER
        defaultActivityShouldNotBeFound("weekNumber.in=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllActivitiesByWeekNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where weekNumber is not null
        defaultActivityShouldBeFound("weekNumber.specified=true");

        // Get all the activityList where weekNumber is null
        defaultActivityShouldNotBeFound("weekNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitiesByWeekNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where weekNumber greater than or equals to DEFAULT_WEEK_NUMBER
        defaultActivityShouldBeFound("weekNumber.greaterOrEqualThan=" + DEFAULT_WEEK_NUMBER);

        // Get all the activityList where weekNumber greater than or equals to UPDATED_WEEK_NUMBER
        defaultActivityShouldNotBeFound("weekNumber.greaterOrEqualThan=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllActivitiesByWeekNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where weekNumber less than or equals to DEFAULT_WEEK_NUMBER
        defaultActivityShouldNotBeFound("weekNumber.lessThan=" + DEFAULT_WEEK_NUMBER);

        // Get all the activityList where weekNumber less than or equals to UPDATED_WEEK_NUMBER
        defaultActivityShouldBeFound("weekNumber.lessThan=" + UPDATED_WEEK_NUMBER);
    }


    @Test
    @Transactional
    public void getAllActivitiesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where year equals to DEFAULT_YEAR
        defaultActivityShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the activityList where year equals to UPDATED_YEAR
        defaultActivityShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllActivitiesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultActivityShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the activityList where year equals to UPDATED_YEAR
        defaultActivityShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllActivitiesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where year is not null
        defaultActivityShouldBeFound("year.specified=true");

        // Get all the activityList where year is null
        defaultActivityShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitiesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where year greater than or equals to DEFAULT_YEAR
        defaultActivityShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the activityList where year greater than or equals to UPDATED_YEAR
        defaultActivityShouldNotBeFound("year.greaterOrEqualThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllActivitiesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where year less than or equals to DEFAULT_YEAR
        defaultActivityShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the activityList where year less than or equals to UPDATED_YEAR
        defaultActivityShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }


    @Test
    @Transactional
    public void getAllActivitiesByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where month equals to DEFAULT_MONTH
        defaultActivityShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the activityList where month equals to UPDATED_MONTH
        defaultActivityShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllActivitiesByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultActivityShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the activityList where month equals to UPDATED_MONTH
        defaultActivityShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllActivitiesByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where month is not null
        defaultActivityShouldBeFound("month.specified=true");

        // Get all the activityList where month is null
        defaultActivityShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitiesByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where month greater than or equals to DEFAULT_MONTH
        defaultActivityShouldBeFound("month.greaterOrEqualThan=" + DEFAULT_MONTH);

        // Get all the activityList where month greater than or equals to UPDATED_MONTH
        defaultActivityShouldNotBeFound("month.greaterOrEqualThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllActivitiesByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activityList where month less than or equals to DEFAULT_MONTH
        defaultActivityShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the activityList where month less than or equals to UPDATED_MONTH
        defaultActivityShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }


    @Test
    @Transactional
    public void getAllActivitiesByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        Task task = TaskResourceIntTest.createEntity(em);
        em.persist(task);
        em.flush();
        activity.setTask(task);
        activityRepository.saveAndFlush(activity);
        Long taskId = task.getId();

        // Get all the activityList where task equals to taskId
        defaultActivityShouldBeFound("taskId.equals=" + taskId);

        // Get all the activityList where task equals to taskId + 1
        defaultActivityShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }


    @Test
    @Transactional
    public void getAllActivitiesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        activity.setUser(user);
        activityRepository.saveAndFlush(activity);
        Long userId = user.getId();

        // Get all the activityList where user equals to userId
        defaultActivityShouldBeFound("userId.equals=" + userId);

        // Get all the activityList where user equals to userId + 1
        defaultActivityShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllActivitiesByMissionIsEqualToSomething() throws Exception {
        // Initialize the database
        Mission mission = MissionResourceIntTest.createEntity(em);
        em.persist(mission);
        em.flush();
        activity.setMission(mission);
        activityRepository.saveAndFlush(activity);
        Long missionId = mission.getId();

        // Get all the activityList where mission equals to missionId
        defaultActivityShouldBeFound("missionId.equals=" + missionId);

        // Get all the activityList where mission equals to missionId + 1
        defaultActivityShouldNotBeFound("missionId.equals=" + (missionId + 1));
    }


    @Test
    @Transactional
    public void getAllActivitiesByActivityRejectionIsEqualToSomething() throws Exception {
        // Initialize the database
        ActivityRejection activityRejection = ActivityRejectionResourceIntTest.createEntity(em);
        em.persist(activityRejection);
        em.flush();
        activity.setActivityRejection(activityRejection);
        activityRepository.saveAndFlush(activity);
        Long activityRejectionId = activityRejection.getId();

        // Get all the activityList where activityRejection equals to activityRejectionId
        defaultActivityShouldBeFound("activityRejectionId.equals=" + activityRejectionId);

        // Get all the activityList where activityRejection equals to activityRejectionId + 1
        defaultActivityShouldNotBeFound("activityRejectionId.equals=" + (activityRejectionId + 1));
    }


    @Test
    @Transactional
    public void getAllActivitiesByActivitySubmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        ActivitySubmission activitySubmission = ActivitySubmissionResourceIntTest.createEntity(em);
        em.persist(activitySubmission);
        em.flush();
        activity.setActivitySubmission(activitySubmission);
        activityRepository.saveAndFlush(activity);
        Long activitySubmissionId = activitySubmission.getId();

        // Get all the activityList where activitySubmission equals to activitySubmissionId
        defaultActivityShouldBeFound("activitySubmissionId.equals=" + activitySubmissionId);

        // Get all the activityList where activitySubmission equals to activitySubmissionId + 1
        defaultActivityShouldNotBeFound("activitySubmissionId.equals=" + (activitySubmissionId + 1));
    }


    @Test
    @Transactional
    public void getAllActivitiesByActivityValidationIsEqualToSomething() throws Exception {
        // Initialize the database
        ActivityValidation activityValidation = ActivityValidationResourceIntTest.createEntity(em);
        em.persist(activityValidation);
        em.flush();
        activity.setActivityValidation(activityValidation);
        activityRepository.saveAndFlush(activity);
        Long activityValidationId = activityValidation.getId();

        // Get all the activityList where activityValidation equals to activityValidationId
        defaultActivityShouldBeFound("activityValidationId.equals=" + activityValidationId);

        // Get all the activityList where activityValidation equals to activityValidationId + 1
        defaultActivityShouldNotBeFound("activityValidationId.equals=" + (activityValidationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultActivityShouldBeFound(String filter) throws Exception {
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].nbOfHours").value(hasItem(DEFAULT_NB_OF_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultActivityShouldNotBeFound(String filter) throws Exception {
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        Activity updatedActivity = activityRepository.findById(activity.getId()).get();
        // Disconnect from session so that the updates on updatedActivity are not directly saved in db
        em.detach(updatedActivity);
        updatedActivity
            .activityDate(UPDATED_ACTIVITY_DATE)
            .nbOfHours(UPDATED_NB_OF_HOURS)
            .day(UPDATED_DAY)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH);
        ActivityDTO activityDTO = activityMapper.toDto(updatedActivity);

        restActivityMockMvc.perform(put("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
        assertThat(testActivity.getActivityDate()).isEqualTo(UPDATED_ACTIVITY_DATE);
        assertThat(testActivity.getNbOfHours()).isEqualTo(UPDATED_NB_OF_HOURS);
        assertThat(testActivity.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testActivity.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testActivity.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testActivity.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityMockMvc.perform(put("/api/activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = new Activity();
        activity1.setId(1L);
        Activity activity2 = new Activity();
        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);
        activity2.setId(2L);
        assertThat(activity1).isNotEqualTo(activity2);
        activity1.setId(null);
        assertThat(activity1).isNotEqualTo(activity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityDTO.class);
        ActivityDTO activityDTO1 = new ActivityDTO();
        activityDTO1.setId(1L);
        ActivityDTO activityDTO2 = new ActivityDTO();
        assertThat(activityDTO1).isNotEqualTo(activityDTO2);
        activityDTO2.setId(activityDTO1.getId());
        assertThat(activityDTO1).isEqualTo(activityDTO2);
        activityDTO2.setId(2L);
        assertThat(activityDTO1).isNotEqualTo(activityDTO2);
        activityDTO1.setId(null);
        assertThat(activityDTO1).isNotEqualTo(activityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityMapper.fromId(null)).isNull();
    }
}

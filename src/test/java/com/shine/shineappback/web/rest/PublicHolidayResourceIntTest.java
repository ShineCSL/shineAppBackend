package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.PublicHoliday;
import com.shine.shineappback.repository.PublicHolidayRepository;
import com.shine.shineappback.service.PublicHolidayService;
import com.shine.shineappback.service.dto.PublicHolidayDTO;
import com.shine.shineappback.service.mapper.PublicHolidayMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.PublicHolidayCriteria;
import com.shine.shineappback.service.PublicHolidayQueryService;

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
 * Test class for the PublicHolidayResource REST controller.
 *
 * @see PublicHolidayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class PublicHolidayResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_HOLIDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_HOLIDAY = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    @Autowired
    private PublicHolidayRepository publicHolidayRepository;


    @Autowired
    private PublicHolidayMapper publicHolidayMapper;
    

    @Autowired
    private PublicHolidayService publicHolidayService;

    @Autowired
    private PublicHolidayQueryService publicHolidayQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPublicHolidayMockMvc;

    private PublicHoliday publicHoliday;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PublicHolidayResource publicHolidayResource = new PublicHolidayResource(publicHolidayService, publicHolidayQueryService);
        this.restPublicHolidayMockMvc = MockMvcBuilders.standaloneSetup(publicHolidayResource)
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
    public static PublicHoliday createEntity(EntityManager em) {
        PublicHoliday publicHoliday = new PublicHoliday()
            .label(DEFAULT_LABEL)
            .dateHoliday(DEFAULT_DATE_HOLIDAY)
            .day(DEFAULT_DAY)
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR);
        return publicHoliday;
    }

    @Before
    public void initTest() {
        publicHoliday = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublicHoliday() throws Exception {
        int databaseSizeBeforeCreate = publicHolidayRepository.findAll().size();

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);
        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isCreated());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeCreate + 1);
        PublicHoliday testPublicHoliday = publicHolidayList.get(publicHolidayList.size() - 1);
        assertThat(testPublicHoliday.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testPublicHoliday.getDateHoliday()).isEqualTo(DEFAULT_DATE_HOLIDAY);
        assertThat(testPublicHoliday.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testPublicHoliday.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testPublicHoliday.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPublicHoliday.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createPublicHolidayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publicHolidayRepository.findAll().size();

        // Create the PublicHoliday with an existing ID
        publicHoliday.setId(1L);
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicHolidayRepository.findAll().size();
        // set the field null
        publicHoliday.setLabel(null);

        // Create the PublicHoliday, which fails.
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateHolidayIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicHolidayRepository.findAll().size();
        // set the field null
        publicHoliday.setDateHoliday(null);

        // Create the PublicHoliday, which fails.
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicHolidayRepository.findAll().size();
        // set the field null
        publicHoliday.setDay(null);

        // Create the PublicHoliday, which fails.
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicHolidayRepository.findAll().size();
        // set the field null
        publicHoliday.setWeekNumber(null);

        // Create the PublicHoliday, which fails.
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicHolidayRepository.findAll().size();
        // set the field null
        publicHoliday.setMonth(null);

        // Create the PublicHoliday, which fails.
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicHolidayRepository.findAll().size();
        // set the field null
        publicHoliday.setYear(null);

        // Create the PublicHoliday, which fails.
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        restPublicHolidayMockMvc.perform(post("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPublicHolidays() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList
        restPublicHolidayMockMvc.perform(get("/api/public-holidays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicHoliday.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].dateHoliday").value(hasItem(DEFAULT_DATE_HOLIDAY.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }
    

    @Test
    @Transactional
    public void getPublicHoliday() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get the publicHoliday
        restPublicHolidayMockMvc.perform(get("/api/public-holidays/{id}", publicHoliday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(publicHoliday.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.dateHoliday").value(DEFAULT_DATE_HOLIDAY.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where label equals to DEFAULT_LABEL
        defaultPublicHolidayShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the publicHolidayList where label equals to UPDATED_LABEL
        defaultPublicHolidayShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultPublicHolidayShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the publicHolidayList where label equals to UPDATED_LABEL
        defaultPublicHolidayShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where label is not null
        defaultPublicHolidayShouldBeFound("label.specified=true");

        // Get all the publicHolidayList where label is null
        defaultPublicHolidayShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDateHolidayIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where dateHoliday equals to DEFAULT_DATE_HOLIDAY
        defaultPublicHolidayShouldBeFound("dateHoliday.equals=" + DEFAULT_DATE_HOLIDAY);

        // Get all the publicHolidayList where dateHoliday equals to UPDATED_DATE_HOLIDAY
        defaultPublicHolidayShouldNotBeFound("dateHoliday.equals=" + UPDATED_DATE_HOLIDAY);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDateHolidayIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where dateHoliday in DEFAULT_DATE_HOLIDAY or UPDATED_DATE_HOLIDAY
        defaultPublicHolidayShouldBeFound("dateHoliday.in=" + DEFAULT_DATE_HOLIDAY + "," + UPDATED_DATE_HOLIDAY);

        // Get all the publicHolidayList where dateHoliday equals to UPDATED_DATE_HOLIDAY
        defaultPublicHolidayShouldNotBeFound("dateHoliday.in=" + UPDATED_DATE_HOLIDAY);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDateHolidayIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where dateHoliday is not null
        defaultPublicHolidayShouldBeFound("dateHoliday.specified=true");

        // Get all the publicHolidayList where dateHoliday is null
        defaultPublicHolidayShouldNotBeFound("dateHoliday.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDateHolidayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where dateHoliday greater than or equals to DEFAULT_DATE_HOLIDAY
        defaultPublicHolidayShouldBeFound("dateHoliday.greaterOrEqualThan=" + DEFAULT_DATE_HOLIDAY);

        // Get all the publicHolidayList where dateHoliday greater than or equals to UPDATED_DATE_HOLIDAY
        defaultPublicHolidayShouldNotBeFound("dateHoliday.greaterOrEqualThan=" + UPDATED_DATE_HOLIDAY);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDateHolidayIsLessThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where dateHoliday less than or equals to DEFAULT_DATE_HOLIDAY
        defaultPublicHolidayShouldNotBeFound("dateHoliday.lessThan=" + DEFAULT_DATE_HOLIDAY);

        // Get all the publicHolidayList where dateHoliday less than or equals to UPDATED_DATE_HOLIDAY
        defaultPublicHolidayShouldBeFound("dateHoliday.lessThan=" + UPDATED_DATE_HOLIDAY);
    }


    @Test
    @Transactional
    public void getAllPublicHolidaysByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where day equals to DEFAULT_DAY
        defaultPublicHolidayShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the publicHolidayList where day equals to UPDATED_DAY
        defaultPublicHolidayShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDayIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where day in DEFAULT_DAY or UPDATED_DAY
        defaultPublicHolidayShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the publicHolidayList where day equals to UPDATED_DAY
        defaultPublicHolidayShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where day is not null
        defaultPublicHolidayShouldBeFound("day.specified=true");

        // Get all the publicHolidayList where day is null
        defaultPublicHolidayShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where day greater than or equals to DEFAULT_DAY
        defaultPublicHolidayShouldBeFound("day.greaterOrEqualThan=" + DEFAULT_DAY);

        // Get all the publicHolidayList where day greater than or equals to UPDATED_DAY
        defaultPublicHolidayShouldNotBeFound("day.greaterOrEqualThan=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByDayIsLessThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where day less than or equals to DEFAULT_DAY
        defaultPublicHolidayShouldNotBeFound("day.lessThan=" + DEFAULT_DAY);

        // Get all the publicHolidayList where day less than or equals to UPDATED_DAY
        defaultPublicHolidayShouldBeFound("day.lessThan=" + UPDATED_DAY);
    }


    @Test
    @Transactional
    public void getAllPublicHolidaysByWeekNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where weekNumber equals to DEFAULT_WEEK_NUMBER
        defaultPublicHolidayShouldBeFound("weekNumber.equals=" + DEFAULT_WEEK_NUMBER);

        // Get all the publicHolidayList where weekNumber equals to UPDATED_WEEK_NUMBER
        defaultPublicHolidayShouldNotBeFound("weekNumber.equals=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByWeekNumberIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where weekNumber in DEFAULT_WEEK_NUMBER or UPDATED_WEEK_NUMBER
        defaultPublicHolidayShouldBeFound("weekNumber.in=" + DEFAULT_WEEK_NUMBER + "," + UPDATED_WEEK_NUMBER);

        // Get all the publicHolidayList where weekNumber equals to UPDATED_WEEK_NUMBER
        defaultPublicHolidayShouldNotBeFound("weekNumber.in=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByWeekNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where weekNumber is not null
        defaultPublicHolidayShouldBeFound("weekNumber.specified=true");

        // Get all the publicHolidayList where weekNumber is null
        defaultPublicHolidayShouldNotBeFound("weekNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByWeekNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where weekNumber greater than or equals to DEFAULT_WEEK_NUMBER
        defaultPublicHolidayShouldBeFound("weekNumber.greaterOrEqualThan=" + DEFAULT_WEEK_NUMBER);

        // Get all the publicHolidayList where weekNumber greater than or equals to UPDATED_WEEK_NUMBER
        defaultPublicHolidayShouldNotBeFound("weekNumber.greaterOrEqualThan=" + UPDATED_WEEK_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByWeekNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where weekNumber less than or equals to DEFAULT_WEEK_NUMBER
        defaultPublicHolidayShouldNotBeFound("weekNumber.lessThan=" + DEFAULT_WEEK_NUMBER);

        // Get all the publicHolidayList where weekNumber less than or equals to UPDATED_WEEK_NUMBER
        defaultPublicHolidayShouldBeFound("weekNumber.lessThan=" + UPDATED_WEEK_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPublicHolidaysByMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where month equals to DEFAULT_MONTH
        defaultPublicHolidayShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the publicHolidayList where month equals to UPDATED_MONTH
        defaultPublicHolidayShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByMonthIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultPublicHolidayShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the publicHolidayList where month equals to UPDATED_MONTH
        defaultPublicHolidayShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where month is not null
        defaultPublicHolidayShouldBeFound("month.specified=true");

        // Get all the publicHolidayList where month is null
        defaultPublicHolidayShouldNotBeFound("month.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where month greater than or equals to DEFAULT_MONTH
        defaultPublicHolidayShouldBeFound("month.greaterOrEqualThan=" + DEFAULT_MONTH);

        // Get all the publicHolidayList where month greater than or equals to UPDATED_MONTH
        defaultPublicHolidayShouldNotBeFound("month.greaterOrEqualThan=" + UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where month less than or equals to DEFAULT_MONTH
        defaultPublicHolidayShouldNotBeFound("month.lessThan=" + DEFAULT_MONTH);

        // Get all the publicHolidayList where month less than or equals to UPDATED_MONTH
        defaultPublicHolidayShouldBeFound("month.lessThan=" + UPDATED_MONTH);
    }


    @Test
    @Transactional
    public void getAllPublicHolidaysByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where year equals to DEFAULT_YEAR
        defaultPublicHolidayShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the publicHolidayList where year equals to UPDATED_YEAR
        defaultPublicHolidayShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByYearIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultPublicHolidayShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the publicHolidayList where year equals to UPDATED_YEAR
        defaultPublicHolidayShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where year is not null
        defaultPublicHolidayShouldBeFound("year.specified=true");

        // Get all the publicHolidayList where year is null
        defaultPublicHolidayShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where year greater than or equals to DEFAULT_YEAR
        defaultPublicHolidayShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the publicHolidayList where year greater than or equals to UPDATED_YEAR
        defaultPublicHolidayShouldNotBeFound("year.greaterOrEqualThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllPublicHolidaysByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where year less than or equals to DEFAULT_YEAR
        defaultPublicHolidayShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the publicHolidayList where year less than or equals to UPDATED_YEAR
        defaultPublicHolidayShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPublicHolidayShouldBeFound(String filter) throws Exception {
        restPublicHolidayMockMvc.perform(get("/api/public-holidays?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicHoliday.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].dateHoliday").value(hasItem(DEFAULT_DATE_HOLIDAY.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPublicHolidayShouldNotBeFound(String filter) throws Exception {
        restPublicHolidayMockMvc.perform(get("/api/public-holidays?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingPublicHoliday() throws Exception {
        // Get the publicHoliday
        restPublicHolidayMockMvc.perform(get("/api/public-holidays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublicHoliday() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();

        // Update the publicHoliday
        PublicHoliday updatedPublicHoliday = publicHolidayRepository.findById(publicHoliday.getId()).get();
        // Disconnect from session so that the updates on updatedPublicHoliday are not directly saved in db
        em.detach(updatedPublicHoliday);
        updatedPublicHoliday
            .label(UPDATED_LABEL)
            .dateHoliday(UPDATED_DATE_HOLIDAY)
            .day(UPDATED_DAY)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(updatedPublicHoliday);

        restPublicHolidayMockMvc.perform(put("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isOk());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
        PublicHoliday testPublicHoliday = publicHolidayList.get(publicHolidayList.size() - 1);
        assertThat(testPublicHoliday.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPublicHoliday.getDateHoliday()).isEqualTo(UPDATED_DATE_HOLIDAY);
        assertThat(testPublicHoliday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testPublicHoliday.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testPublicHoliday.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPublicHoliday.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void updateNonExistingPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPublicHolidayMockMvc.perform(put("/api/public-holidays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePublicHoliday() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        int databaseSizeBeforeDelete = publicHolidayRepository.findAll().size();

        // Get the publicHoliday
        restPublicHolidayMockMvc.perform(delete("/api/public-holidays/{id}", publicHoliday.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicHoliday.class);
        PublicHoliday publicHoliday1 = new PublicHoliday();
        publicHoliday1.setId(1L);
        PublicHoliday publicHoliday2 = new PublicHoliday();
        publicHoliday2.setId(publicHoliday1.getId());
        assertThat(publicHoliday1).isEqualTo(publicHoliday2);
        publicHoliday2.setId(2L);
        assertThat(publicHoliday1).isNotEqualTo(publicHoliday2);
        publicHoliday1.setId(null);
        assertThat(publicHoliday1).isNotEqualTo(publicHoliday2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicHolidayDTO.class);
        PublicHolidayDTO publicHolidayDTO1 = new PublicHolidayDTO();
        publicHolidayDTO1.setId(1L);
        PublicHolidayDTO publicHolidayDTO2 = new PublicHolidayDTO();
        assertThat(publicHolidayDTO1).isNotEqualTo(publicHolidayDTO2);
        publicHolidayDTO2.setId(publicHolidayDTO1.getId());
        assertThat(publicHolidayDTO1).isEqualTo(publicHolidayDTO2);
        publicHolidayDTO2.setId(2L);
        assertThat(publicHolidayDTO1).isNotEqualTo(publicHolidayDTO2);
        publicHolidayDTO1.setId(null);
        assertThat(publicHolidayDTO1).isNotEqualTo(publicHolidayDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(publicHolidayMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(publicHolidayMapper.fromId(null)).isNull();
    }
}

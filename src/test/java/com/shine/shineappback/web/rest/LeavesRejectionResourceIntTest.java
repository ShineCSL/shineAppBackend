package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.LeavesRejection;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.repository.LeavesRejectionRepository;
import com.shine.shineappback.service.LeavesRejectionService;
import com.shine.shineappback.service.dto.LeavesRejectionDTO;
import com.shine.shineappback.service.mapper.LeavesRejectionMapper;
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
 * Test class for the LeavesRejectionResource REST controller.
 *
 * @see LeavesRejectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class LeavesRejectionResourceIntTest {

    private static final Boolean DEFAULT_REJECTED = false;
    private static final Boolean UPDATED_REJECTED = true;

    private static final ZonedDateTime DEFAULT_DATE_MODIFICATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFICATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LeavesRejectionRepository leavesRejectionRepository;


    @Autowired
    private LeavesRejectionMapper leavesRejectionMapper;
    

    @Autowired
    private LeavesRejectionService leavesRejectionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeavesRejectionMockMvc;

    private LeavesRejection leavesRejection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeavesRejectionResource leavesRejectionResource = new LeavesRejectionResource(leavesRejectionService);
        this.restLeavesRejectionMockMvc = MockMvcBuilders.standaloneSetup(leavesRejectionResource)
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
    public static LeavesRejection createEntity(EntityManager em) {
        LeavesRejection leavesRejection = new LeavesRejection()
            .rejected(DEFAULT_REJECTED)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        leavesRejection.setUserCreation(user);
        return leavesRejection;
    }

    @Before
    public void initTest() {
        leavesRejection = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeavesRejection() throws Exception {
        int databaseSizeBeforeCreate = leavesRejectionRepository.findAll().size();

        // Create the LeavesRejection
        LeavesRejectionDTO leavesRejectionDTO = leavesRejectionMapper.toDto(leavesRejection);
        restLeavesRejectionMockMvc.perform(post("/api/leaves-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesRejectionDTO)))
            .andExpect(status().isCreated());

        // Validate the LeavesRejection in the database
        List<LeavesRejection> leavesRejectionList = leavesRejectionRepository.findAll();
        assertThat(leavesRejectionList).hasSize(databaseSizeBeforeCreate + 1);
        LeavesRejection testLeavesRejection = leavesRejectionList.get(leavesRejectionList.size() - 1);
        assertThat(testLeavesRejection.isRejected()).isEqualTo(DEFAULT_REJECTED);
        assertThat(testLeavesRejection.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testLeavesRejection.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
    }

    @Test
    @Transactional
    public void createLeavesRejectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leavesRejectionRepository.findAll().size();

        // Create the LeavesRejection with an existing ID
        leavesRejection.setId(1L);
        LeavesRejectionDTO leavesRejectionDTO = leavesRejectionMapper.toDto(leavesRejection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeavesRejectionMockMvc.perform(post("/api/leaves-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesRejectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeavesRejection in the database
        List<LeavesRejection> leavesRejectionList = leavesRejectionRepository.findAll();
        assertThat(leavesRejectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = leavesRejectionRepository.findAll().size();
        // set the field null
        leavesRejection.setDateCreation(null);

        // Create the LeavesRejection, which fails.
        LeavesRejectionDTO leavesRejectionDTO = leavesRejectionMapper.toDto(leavesRejection);

        restLeavesRejectionMockMvc.perform(post("/api/leaves-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesRejectionDTO)))
            .andExpect(status().isBadRequest());

        List<LeavesRejection> leavesRejectionList = leavesRejectionRepository.findAll();
        assertThat(leavesRejectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeavesRejections() throws Exception {
        // Initialize the database
        leavesRejectionRepository.saveAndFlush(leavesRejection);

        // Get all the leavesRejectionList
        restLeavesRejectionMockMvc.perform(get("/api/leaves-rejections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leavesRejection.getId().intValue())))
            .andExpect(jsonPath("$.[*].rejected").value(hasItem(DEFAULT_REJECTED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(sameInstant(DEFAULT_DATE_MODIFICATION))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))));
    }
    

    @Test
    @Transactional
    public void getLeavesRejection() throws Exception {
        // Initialize the database
        leavesRejectionRepository.saveAndFlush(leavesRejection);

        // Get the leavesRejection
        restLeavesRejectionMockMvc.perform(get("/api/leaves-rejections/{id}", leavesRejection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leavesRejection.getId().intValue()))
            .andExpect(jsonPath("$.rejected").value(DEFAULT_REJECTED.booleanValue()))
            .andExpect(jsonPath("$.dateModification").value(sameInstant(DEFAULT_DATE_MODIFICATION)))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)));
    }
    @Test
    @Transactional
    public void getNonExistingLeavesRejection() throws Exception {
        // Get the leavesRejection
        restLeavesRejectionMockMvc.perform(get("/api/leaves-rejections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeavesRejection() throws Exception {
        // Initialize the database
        leavesRejectionRepository.saveAndFlush(leavesRejection);

        int databaseSizeBeforeUpdate = leavesRejectionRepository.findAll().size();

        // Update the leavesRejection
        LeavesRejection updatedLeavesRejection = leavesRejectionRepository.findById(leavesRejection.getId()).get();
        // Disconnect from session so that the updates on updatedLeavesRejection are not directly saved in db
        em.detach(updatedLeavesRejection);
        updatedLeavesRejection
            .rejected(UPDATED_REJECTED)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateCreation(UPDATED_DATE_CREATION);
        LeavesRejectionDTO leavesRejectionDTO = leavesRejectionMapper.toDto(updatedLeavesRejection);

        restLeavesRejectionMockMvc.perform(put("/api/leaves-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesRejectionDTO)))
            .andExpect(status().isOk());

        // Validate the LeavesRejection in the database
        List<LeavesRejection> leavesRejectionList = leavesRejectionRepository.findAll();
        assertThat(leavesRejectionList).hasSize(databaseSizeBeforeUpdate);
        LeavesRejection testLeavesRejection = leavesRejectionList.get(leavesRejectionList.size() - 1);
        assertThat(testLeavesRejection.isRejected()).isEqualTo(UPDATED_REJECTED);
        assertThat(testLeavesRejection.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testLeavesRejection.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    public void updateNonExistingLeavesRejection() throws Exception {
        int databaseSizeBeforeUpdate = leavesRejectionRepository.findAll().size();

        // Create the LeavesRejection
        LeavesRejectionDTO leavesRejectionDTO = leavesRejectionMapper.toDto(leavesRejection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeavesRejectionMockMvc.perform(put("/api/leaves-rejections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leavesRejectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeavesRejection in the database
        List<LeavesRejection> leavesRejectionList = leavesRejectionRepository.findAll();
        assertThat(leavesRejectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeavesRejection() throws Exception {
        // Initialize the database
        leavesRejectionRepository.saveAndFlush(leavesRejection);

        int databaseSizeBeforeDelete = leavesRejectionRepository.findAll().size();

        // Get the leavesRejection
        restLeavesRejectionMockMvc.perform(delete("/api/leaves-rejections/{id}", leavesRejection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LeavesRejection> leavesRejectionList = leavesRejectionRepository.findAll();
        assertThat(leavesRejectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesRejection.class);
        LeavesRejection leavesRejection1 = new LeavesRejection();
        leavesRejection1.setId(1L);
        LeavesRejection leavesRejection2 = new LeavesRejection();
        leavesRejection2.setId(leavesRejection1.getId());
        assertThat(leavesRejection1).isEqualTo(leavesRejection2);
        leavesRejection2.setId(2L);
        assertThat(leavesRejection1).isNotEqualTo(leavesRejection2);
        leavesRejection1.setId(null);
        assertThat(leavesRejection1).isNotEqualTo(leavesRejection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeavesRejectionDTO.class);
        LeavesRejectionDTO leavesRejectionDTO1 = new LeavesRejectionDTO();
        leavesRejectionDTO1.setId(1L);
        LeavesRejectionDTO leavesRejectionDTO2 = new LeavesRejectionDTO();
        assertThat(leavesRejectionDTO1).isNotEqualTo(leavesRejectionDTO2);
        leavesRejectionDTO2.setId(leavesRejectionDTO1.getId());
        assertThat(leavesRejectionDTO1).isEqualTo(leavesRejectionDTO2);
        leavesRejectionDTO2.setId(2L);
        assertThat(leavesRejectionDTO1).isNotEqualTo(leavesRejectionDTO2);
        leavesRejectionDTO1.setId(null);
        assertThat(leavesRejectionDTO1).isNotEqualTo(leavesRejectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leavesRejectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leavesRejectionMapper.fromId(null)).isNull();
    }
}

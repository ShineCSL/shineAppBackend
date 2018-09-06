package com.shine.shineappback.web.rest;

import com.shine.shineappback.ShineAppBackendApp;

import com.shine.shineappback.domain.Invoice;
import com.shine.shineappback.domain.Currency;
import com.shine.shineappback.domain.User;
import com.shine.shineappback.domain.InvoiceRejection;
import com.shine.shineappback.domain.InvoiceSubmission;
import com.shine.shineappback.domain.InvoiceValidation;
import com.shine.shineappback.domain.TypeInvoice;
import com.shine.shineappback.repository.InvoiceRepository;
import com.shine.shineappback.service.InvoiceService;
import com.shine.shineappback.service.dto.InvoiceDTO;
import com.shine.shineappback.service.mapper.InvoiceMapper;
import com.shine.shineappback.web.rest.errors.ExceptionTranslator;
import com.shine.shineappback.service.dto.InvoiceCriteria;
import com.shine.shineappback.service.InvoiceQueryService;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShineAppBackendApp.class)
public class InvoiceResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final LocalDate DEFAULT_DATE_INVOICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INVOICE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    @Autowired
    private InvoiceRepository invoiceRepository;


    @Autowired
    private InvoiceMapper invoiceMapper;
    

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceQueryService invoiceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService, invoiceQueryService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
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
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .dateInvoice(DEFAULT_DATE_INVOICE)
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE)
            .rate(DEFAULT_RATE);
        // Add required entity
        Currency currency = CurrencyResourceIntTest.createEntity(em);
        em.persist(currency);
        em.flush();
        invoice.setCurrency(currency);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        invoice.setUser(user);
        // Add required entity
        TypeInvoice typeInvoice = TypeInvoiceResourceIntTest.createEntity(em);
        em.persist(typeInvoice);
        em.flush();
        invoice.setTypeInvoice(typeInvoice);
        return invoice;
    }

    @Before
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testInvoice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoice.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoice.getDateInvoice()).isEqualTo(DEFAULT_DATE_INVOICE);
        assertThat(testInvoice.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testInvoice.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
        assertThat(testInvoice.getRate()).isEqualTo(DEFAULT_RATE);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setLabel(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setAmount(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateInvoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setDateInvoice(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateInvoice").value(hasItem(DEFAULT_DATE_INVOICE.toString())))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.dateInvoice").value(DEFAULT_DATE_INVOICE.toString()))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllInvoicesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where label equals to DEFAULT_LABEL
        defaultInvoiceShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the invoiceList where label equals to UPDATED_LABEL
        defaultInvoiceShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultInvoiceShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the invoiceList where label equals to UPDATED_LABEL
        defaultInvoiceShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where label is not null
        defaultInvoiceShouldBeFound("label.specified=true");

        // Get all the invoiceList where label is null
        defaultInvoiceShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where description equals to DEFAULT_DESCRIPTION
        defaultInvoiceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the invoiceList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInvoiceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the invoiceList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where description is not null
        defaultInvoiceShouldBeFound("description.specified=true");

        // Get all the invoiceList where description is null
        defaultInvoiceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where amount equals to DEFAULT_AMOUNT
        defaultInvoiceShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the invoiceList where amount equals to UPDATED_AMOUNT
        defaultInvoiceShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultInvoiceShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the invoiceList where amount equals to UPDATED_AMOUNT
        defaultInvoiceShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where amount is not null
        defaultInvoiceShouldBeFound("amount.specified=true");

        // Get all the invoiceList where amount is null
        defaultInvoiceShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateInvoice equals to DEFAULT_DATE_INVOICE
        defaultInvoiceShouldBeFound("dateInvoice.equals=" + DEFAULT_DATE_INVOICE);

        // Get all the invoiceList where dateInvoice equals to UPDATED_DATE_INVOICE
        defaultInvoiceShouldNotBeFound("dateInvoice.equals=" + UPDATED_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateInvoiceIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateInvoice in DEFAULT_DATE_INVOICE or UPDATED_DATE_INVOICE
        defaultInvoiceShouldBeFound("dateInvoice.in=" + DEFAULT_DATE_INVOICE + "," + UPDATED_DATE_INVOICE);

        // Get all the invoiceList where dateInvoice equals to UPDATED_DATE_INVOICE
        defaultInvoiceShouldNotBeFound("dateInvoice.in=" + UPDATED_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateInvoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateInvoice is not null
        defaultInvoiceShouldBeFound("dateInvoice.specified=true");

        // Get all the invoiceList where dateInvoice is null
        defaultInvoiceShouldNotBeFound("dateInvoice.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateInvoiceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateInvoice greater than or equals to DEFAULT_DATE_INVOICE
        defaultInvoiceShouldBeFound("dateInvoice.greaterOrEqualThan=" + DEFAULT_DATE_INVOICE);

        // Get all the invoiceList where dateInvoice greater than or equals to UPDATED_DATE_INVOICE
        defaultInvoiceShouldNotBeFound("dateInvoice.greaterOrEqualThan=" + UPDATED_DATE_INVOICE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDateInvoiceIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateInvoice less than or equals to DEFAULT_DATE_INVOICE
        defaultInvoiceShouldNotBeFound("dateInvoice.lessThan=" + DEFAULT_DATE_INVOICE);

        // Get all the invoiceList where dateInvoice less than or equals to UPDATED_DATE_INVOICE
        defaultInvoiceShouldBeFound("dateInvoice.lessThan=" + UPDATED_DATE_INVOICE);
    }


    @Test
    @Transactional
    public void getAllInvoicesByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where rate equals to DEFAULT_RATE
        defaultInvoiceShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the invoiceList where rate equals to UPDATED_RATE
        defaultInvoiceShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultInvoiceShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the invoiceList where rate equals to UPDATED_RATE
        defaultInvoiceShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where rate is not null
        defaultInvoiceShouldBeFound("rate.specified=true");

        // Get all the invoiceList where rate is null
        defaultInvoiceShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        Currency currency = CurrencyResourceIntTest.createEntity(em);
        em.persist(currency);
        em.flush();
        invoice.setCurrency(currency);
        invoiceRepository.saveAndFlush(invoice);
        Long currencyId = currency.getId();

        // Get all the invoiceList where currency equals to currencyId
        defaultInvoiceShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the invoiceList where currency equals to currencyId + 1
        defaultInvoiceShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        invoice.setUser(user);
        invoiceRepository.saveAndFlush(invoice);
        Long userId = user.getId();

        // Get all the invoiceList where user equals to userId
        defaultInvoiceShouldBeFound("userId.equals=" + userId);

        // Get all the invoiceList where user equals to userId + 1
        defaultInvoiceShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvoiceRejectionIsEqualToSomething() throws Exception {
        // Initialize the database
        InvoiceRejection invoiceRejection = InvoiceRejectionResourceIntTest.createEntity(em);
        em.persist(invoiceRejection);
        em.flush();
        invoice.setInvoiceRejection(invoiceRejection);
        invoiceRepository.saveAndFlush(invoice);
        Long invoiceRejectionId = invoiceRejection.getId();

        // Get all the invoiceList where invoiceRejection equals to invoiceRejectionId
        defaultInvoiceShouldBeFound("invoiceRejectionId.equals=" + invoiceRejectionId);

        // Get all the invoiceList where invoiceRejection equals to invoiceRejectionId + 1
        defaultInvoiceShouldNotBeFound("invoiceRejectionId.equals=" + (invoiceRejectionId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvoiceSubmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        InvoiceSubmission invoiceSubmission = InvoiceSubmissionResourceIntTest.createEntity(em);
        em.persist(invoiceSubmission);
        em.flush();
        invoice.setInvoiceSubmission(invoiceSubmission);
        invoiceRepository.saveAndFlush(invoice);
        Long invoiceSubmissionId = invoiceSubmission.getId();

        // Get all the invoiceList where invoiceSubmission equals to invoiceSubmissionId
        defaultInvoiceShouldBeFound("invoiceSubmissionId.equals=" + invoiceSubmissionId);

        // Get all the invoiceList where invoiceSubmission equals to invoiceSubmissionId + 1
        defaultInvoiceShouldNotBeFound("invoiceSubmissionId.equals=" + (invoiceSubmissionId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByInvoiceValidationIsEqualToSomething() throws Exception {
        // Initialize the database
        InvoiceValidation invoiceValidation = InvoiceValidationResourceIntTest.createEntity(em);
        em.persist(invoiceValidation);
        em.flush();
        invoice.setInvoiceValidation(invoiceValidation);
        invoiceRepository.saveAndFlush(invoice);
        Long invoiceValidationId = invoiceValidation.getId();

        // Get all the invoiceList where invoiceValidation equals to invoiceValidationId
        defaultInvoiceShouldBeFound("invoiceValidationId.equals=" + invoiceValidationId);

        // Get all the invoiceList where invoiceValidation equals to invoiceValidationId + 1
        defaultInvoiceShouldNotBeFound("invoiceValidationId.equals=" + (invoiceValidationId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByTypeInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        TypeInvoice typeInvoice = TypeInvoiceResourceIntTest.createEntity(em);
        em.persist(typeInvoice);
        em.flush();
        invoice.setTypeInvoice(typeInvoice);
        invoiceRepository.saveAndFlush(invoice);
        Long typeInvoiceId = typeInvoice.getId();

        // Get all the invoiceList where typeInvoice equals to typeInvoiceId
        defaultInvoiceShouldBeFound("typeInvoiceId.equals=" + typeInvoiceId);

        // Get all the invoiceList where typeInvoice equals to typeInvoiceId + 1
        defaultInvoiceShouldNotBeFound("typeInvoiceId.equals=" + (typeInvoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateInvoice").value(hasItem(DEFAULT_DATE_INVOICE.toString())))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .dateInvoice(UPDATED_DATE_INVOICE)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .rate(UPDATED_RATE);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testInvoice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoice.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getDateInvoice()).isEqualTo(UPDATED_DATE_INVOICE);
        assertThat(testInvoice.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testInvoice.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
        assertThat(testInvoice.getRate()).isEqualTo(UPDATED_RATE);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDTO.class);
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO2.setId(invoiceDTO1.getId());
        assertThat(invoiceDTO1).isEqualTo(invoiceDTO2);
        invoiceDTO2.setId(2L);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO1.setId(null);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceMapper.fromId(null)).isNull();
    }
}

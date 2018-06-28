package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_label", nullable = false)
    private String label;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "date_invoice", nullable = false)
    private LocalDate dateInvoice;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

    @Column(name = "date_modification")
    private ZonedDateTime dateModification;

    @Lob
    @Column(name = "document")
    private byte[] document;

    @Column(name = "document_content_type")
    private String documentContentType;

    @Column(name = "rate")
    private Double rate;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private TypeInvoice typeInvoice;

    @OneToOne
    @JoinColumn(unique = true)
    private InvoiceSubmission invoiceSubmission;

    @OneToOne
    @JoinColumn(unique = true)
    private InvoiceValidation invoiceValidation;

    @OneToOne
    @JoinColumn(unique = true)
    private InvoiceRejection invoiceRejection;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Currency currency;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User userCreation;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User userModification;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Invoice label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public Invoice description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public Invoice amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public Invoice dateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
        return this;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public Invoice dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public Invoice dateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public byte[] getDocument() {
        return document;
    }

    public Invoice document(byte[] document) {
        this.document = document;
        return this;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public Invoice documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public Double getRate() {
        return rate;
    }

    public Invoice rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public TypeInvoice getTypeInvoice() {
        return typeInvoice;
    }

    public Invoice typeInvoice(TypeInvoice typeInvoice) {
        this.typeInvoice = typeInvoice;
        return this;
    }

    public void setTypeInvoice(TypeInvoice typeInvoice) {
        this.typeInvoice = typeInvoice;
    }

    public InvoiceSubmission getInvoiceSubmission() {
        return invoiceSubmission;
    }

    public Invoice invoiceSubmission(InvoiceSubmission invoiceSubmission) {
        this.invoiceSubmission = invoiceSubmission;
        return this;
    }

    public void setInvoiceSubmission(InvoiceSubmission invoiceSubmission) {
        this.invoiceSubmission = invoiceSubmission;
    }

    public InvoiceValidation getInvoiceValidation() {
        return invoiceValidation;
    }

    public Invoice invoiceValidation(InvoiceValidation invoiceValidation) {
        this.invoiceValidation = invoiceValidation;
        return this;
    }

    public void setInvoiceValidation(InvoiceValidation invoiceValidation) {
        this.invoiceValidation = invoiceValidation;
    }

    public InvoiceRejection getInvoiceRejection() {
        return invoiceRejection;
    }

    public Invoice invoiceRejection(InvoiceRejection invoiceRejection) {
        this.invoiceRejection = invoiceRejection;
        return this;
    }

    public void setInvoiceRejection(InvoiceRejection invoiceRejection) {
        this.invoiceRejection = invoiceRejection;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Invoice currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public User getUserCreation() {
        return userCreation;
    }

    public Invoice userCreation(User user) {
        this.userCreation = user;
        return this;
    }

    public void setUserCreation(User user) {
        this.userCreation = user;
    }

    public User getUserModification() {
        return userModification;
    }

    public Invoice userModification(User user) {
        this.userModification = user;
        return this;
    }

    public void setUserModification(User user) {
        this.userModification = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", dateInvoice='" + getDateInvoice() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", document='" + getDocument() + "'" +
            ", documentContentType='" + getDocumentContentType() + "'" +
            ", rate=" + getRate() +
            "}";
    }
}

package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A InvoiceValidation.
 */
@Entity
@Table(name = "invoice_validation")
public class InvoiceValidation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "validated")
    private Boolean validated;

    @NotNull
    @Column(name = "date_invoice", nullable = false)
    private LocalDate dateInvoice;

    @OneToOne(mappedBy = "invoiceValidation")
    @JsonIgnore
    private Invoice invoice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isValidated() {
        return validated;
    }

    public InvoiceValidation validated(Boolean validated) {
        this.validated = validated;
        return this;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public InvoiceValidation dateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
        return this;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public InvoiceValidation invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public User getUser() {
        return user;
    }

    public InvoiceValidation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        InvoiceValidation invoiceValidation = (InvoiceValidation) o;
        if (invoiceValidation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceValidation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceValidation{" +
            "id=" + getId() +
            ", validated='" + isValidated() + "'" +
            ", dateInvoice='" + getDateInvoice() + "'" +
            "}";
    }
}

package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InvoiceSubmission.
 */
@Entity
@Table(name = "invoice_submission")
public class InvoiceSubmission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submitted")
    private Boolean submitted;

    @OneToOne(mappedBy = "invoiceSubmission")
    @JsonIgnore
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSubmitted() {
        return submitted;
    }

    public InvoiceSubmission submitted(Boolean submitted) {
        this.submitted = submitted;
        return this;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public InvoiceSubmission invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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
        InvoiceSubmission invoiceSubmission = (InvoiceSubmission) o;
        if (invoiceSubmission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceSubmission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceSubmission{" +
            "id=" + getId() +
            ", submitted='" + isSubmitted() + "'" +
            "}";
    }
}

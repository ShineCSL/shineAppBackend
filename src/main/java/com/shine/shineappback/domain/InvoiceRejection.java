package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InvoiceRejection.
 */
@Entity
@Table(name = "invoice_rejection")
public class InvoiceRejection extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rejected")
    private Boolean rejected;

    @OneToOne(mappedBy = "invoiceRejection")
    @JsonIgnore
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isRejected() {
        return rejected;
    }

    public InvoiceRejection rejected(Boolean rejected) {
        this.rejected = rejected;
        return this;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public InvoiceRejection invoice(Invoice invoice) {
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
        InvoiceRejection invoiceRejection = (InvoiceRejection) o;
        if (invoiceRejection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceRejection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceRejection{" +
            "id=" + getId() +
            ", rejected='" + isRejected() + "'" +
            "}";
    }
}

package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
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

    @NotNull
    @Column(name = "date_invoice", nullable = false)
    private LocalDate dateInvoice;

    @Column(name = "jhi_comment")
    private String comment;

    @OneToOne(mappedBy = "invoiceRejection")
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

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public InvoiceRejection dateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
        return this;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public String getComment() {
        return comment;
    }

    public InvoiceRejection comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public User getUser() {
        return user;
    }

    public InvoiceRejection user(User user) {
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
            ", dateInvoice='" + getDateInvoice() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}

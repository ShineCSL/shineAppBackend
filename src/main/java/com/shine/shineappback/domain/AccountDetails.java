package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AccountDetails.
 */
@Entity
@Table(name = "account_details")
public class AccountDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "rate")
    private Double rate;

    @NotNull
    @Column(name = "jhi_label", nullable = false)
    private String label;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Client client;

    @OneToOne
    @JoinColumn(unique = true)
    private Invoice invoice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public AccountDetails amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRate() {
        return rate;
    }

    public AccountDetails rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getLabel() {
        return label;
    }

    public AccountDetails label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public AccountDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public AccountDetails type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public AccountDetails client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public AccountDetails invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountDetails currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
        AccountDetails accountDetails = (AccountDetails) o;
        if (accountDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", rate=" + getRate() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}

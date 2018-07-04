package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AccountDetails entity.
 */
public class AccountDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;

    private Double rate;

    @NotNull
    private String label;

    private String description;

    @NotNull
    private String type;

    private Long clientId;

    private Long invoiceId;

    private String invoiceLabel;

    private Long currencyId;

    private String currencyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceLabel() {
        return invoiceLabel;
    }

    public void setInvoiceLabel(String invoiceLabel) {
        this.invoiceLabel = invoiceLabel;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountDetailsDTO accountDetailsDTO = (AccountDetailsDTO) o;
        if (accountDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountDetailsDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", rate=" + getRate() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", client=" + getClientId() +
            ", invoice=" + getInvoiceId() +
            ", invoice='" + getInvoiceLabel() + "'" +
            ", currency=" + getCurrencyId() +
            ", currency='" + getCurrencyCode() + "'" +
            "}";
    }
}

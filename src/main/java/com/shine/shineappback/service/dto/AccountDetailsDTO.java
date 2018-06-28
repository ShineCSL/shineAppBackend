package com.shine.shineappback.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AccountDetails entity.
 */
public class AccountDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()[\\\\]+-_*/%]*$")
    private String code;

    @NotNull
    private Double amount;

    @NotNull
    private String type;

    private Double rate;

    @NotNull
    private ZonedDateTime dateCreation;

    private ZonedDateTime dateModification;

    private Long clientId;

    private Long userCreationId;

    private String userCreationLogin;

    private Long userModficationId;

    private String userModficationLogin;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUserCreationId() {
        return userCreationId;
    }

    public void setUserCreationId(Long userId) {
        this.userCreationId = userId;
    }

    public String getUserCreationLogin() {
        return userCreationLogin;
    }

    public void setUserCreationLogin(String userLogin) {
        this.userCreationLogin = userLogin;
    }

    public Long getUserModficationId() {
        return userModficationId;
    }

    public void setUserModficationId(Long userId) {
        this.userModficationId = userId;
    }

    public String getUserModficationLogin() {
        return userModficationLogin;
    }

    public void setUserModficationLogin(String userLogin) {
        this.userModficationLogin = userLogin;
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
            ", code='" + getCode() + "'" +
            ", amount=" + getAmount() +
            ", type='" + getType() + "'" +
            ", rate=" + getRate() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", client=" + getClientId() +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            ", userModfication=" + getUserModficationId() +
            ", userModfication='" + getUserModficationLogin() + "'" +
            ", invoice=" + getInvoiceId() +
            ", invoice='" + getInvoiceLabel() + "'" +
            ", currency=" + getCurrencyId() +
            ", currency='" + getCurrencyCode() + "'" +
            "}";
    }
}

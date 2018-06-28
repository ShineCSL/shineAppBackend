package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Invoice entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    private String description;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate dateInvoice;

    @NotNull
    private ZonedDateTime dateCreation;

    private ZonedDateTime dateModification;

    @Lob
    private byte[] document;
    private String documentContentType;

    private Double rate;

    private Long typeInvoiceId;

    private Long invoiceSubmissionId;

    private Long invoiceValidationId;

    private Long invoiceRejectionId;

    private Long currencyId;

    private String currencyCode;

    private Long userCreationId;

    private String userCreationLogin;

    private Long userModificationId;

    private String userModificationLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
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

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getTypeInvoiceId() {
        return typeInvoiceId;
    }

    public void setTypeInvoiceId(Long typeInvoiceId) {
        this.typeInvoiceId = typeInvoiceId;
    }

    public Long getInvoiceSubmissionId() {
        return invoiceSubmissionId;
    }

    public void setInvoiceSubmissionId(Long invoiceSubmissionId) {
        this.invoiceSubmissionId = invoiceSubmissionId;
    }

    public Long getInvoiceValidationId() {
        return invoiceValidationId;
    }

    public void setInvoiceValidationId(Long invoiceValidationId) {
        this.invoiceValidationId = invoiceValidationId;
    }

    public Long getInvoiceRejectionId() {
        return invoiceRejectionId;
    }

    public void setInvoiceRejectionId(Long invoiceRejectionId) {
        this.invoiceRejectionId = invoiceRejectionId;
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

    public Long getUserModificationId() {
        return userModificationId;
    }

    public void setUserModificationId(Long userId) {
        this.userModificationId = userId;
    }

    public String getUserModificationLogin() {
        return userModificationLogin;
    }

    public void setUserModificationLogin(String userLogin) {
        this.userModificationLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (invoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", dateInvoice='" + getDateInvoice() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", document='" + getDocument() + "'" +
            ", rate=" + getRate() +
            ", typeInvoice=" + getTypeInvoiceId() +
            ", invoiceSubmission=" + getInvoiceSubmissionId() +
            ", invoiceValidation=" + getInvoiceValidationId() +
            ", invoiceRejection=" + getInvoiceRejectionId() +
            ", currency=" + getCurrencyId() +
            ", currency='" + getCurrencyCode() + "'" +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            "}";
    }
}

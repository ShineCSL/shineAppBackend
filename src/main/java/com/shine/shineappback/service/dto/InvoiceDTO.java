package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Invoice entity.
 */
public class InvoiceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    private String description;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate dateInvoice;

    @Lob
    private byte[] document;
    private String documentContentType;

    private Double rate;

    private Long currencyId;

    private String currencyCode;

    private Long invoiceRejectionId;

    private String invoiceRejectionRejected;

    private Long invoiceSubmissionId;

    private String invoiceSubmissionSubmitted;

    private Long invoiceValidationId;

    private String invoiceValidationValidated;

    private Long typeInvoiceId;

    private String typeInvoiceCode;

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

    public Long getInvoiceRejectionId() {
        return invoiceRejectionId;
    }

    public void setInvoiceRejectionId(Long invoiceRejectionId) {
        this.invoiceRejectionId = invoiceRejectionId;
    }

    public String getInvoiceRejectionRejected() {
        return invoiceRejectionRejected;
    }

    public void setInvoiceRejectionRejected(String invoiceRejectionRejected) {
        this.invoiceRejectionRejected = invoiceRejectionRejected;
    }

    public Long getInvoiceSubmissionId() {
        return invoiceSubmissionId;
    }

    public void setInvoiceSubmissionId(Long invoiceSubmissionId) {
        this.invoiceSubmissionId = invoiceSubmissionId;
    }

    public String getInvoiceSubmissionSubmitted() {
        return invoiceSubmissionSubmitted;
    }

    public void setInvoiceSubmissionSubmitted(String invoiceSubmissionSubmitted) {
        this.invoiceSubmissionSubmitted = invoiceSubmissionSubmitted;
    }

    public Long getInvoiceValidationId() {
        return invoiceValidationId;
    }

    public void setInvoiceValidationId(Long invoiceValidationId) {
        this.invoiceValidationId = invoiceValidationId;
    }

    public String getInvoiceValidationValidated() {
        return invoiceValidationValidated;
    }

    public void setInvoiceValidationValidated(String invoiceValidationValidated) {
        this.invoiceValidationValidated = invoiceValidationValidated;
    }

    public Long getTypeInvoiceId() {
        return typeInvoiceId;
    }

    public void setTypeInvoiceId(Long typeInvoiceId) {
        this.typeInvoiceId = typeInvoiceId;
    }

    public String getTypeInvoiceCode() {
        return typeInvoiceCode;
    }

    public void setTypeInvoiceCode(String typeInvoiceCode) {
        this.typeInvoiceCode = typeInvoiceCode;
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
            ", document='" + getDocument() + "'" +
            ", rate=" + getRate() +
            ", currency=" + getCurrencyId() +
            ", currency='" + getCurrencyCode() + "'" +
            ", invoiceRejection=" + getInvoiceRejectionId() +
            ", invoiceRejection='" + getInvoiceRejectionRejected() + "'" +
            ", invoiceSubmission=" + getInvoiceSubmissionId() +
            ", invoiceSubmission='" + getInvoiceSubmissionSubmitted() + "'" +
            ", invoiceValidation=" + getInvoiceValidationId() +
            ", invoiceValidation='" + getInvoiceValidationValidated() + "'" +
            ", typeInvoice=" + getTypeInvoiceId() +
            ", typeInvoice='" + getTypeInvoiceCode() + "'" +
            "}";
    }
}

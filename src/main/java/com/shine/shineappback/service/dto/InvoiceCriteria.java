package com.shine.shineappback.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Invoice entity. This class is used in InvoiceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /invoices?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter label;

    private StringFilter description;

    private DoubleFilter amount;

    private LocalDateFilter dateInvoice;

    private DoubleFilter rate;

    private LongFilter currencyId;

    private LongFilter invoiceRejectionId;

    private LongFilter invoiceSubmissionId;

    private LongFilter invoiceValidationId;

    private LongFilter typeInvoiceId;

    public InvoiceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public LocalDateFilter getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(LocalDateFilter dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public DoubleFilter getRate() {
        return rate;
    }

    public void setRate(DoubleFilter rate) {
        this.rate = rate;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public LongFilter getInvoiceRejectionId() {
        return invoiceRejectionId;
    }

    public void setInvoiceRejectionId(LongFilter invoiceRejectionId) {
        this.invoiceRejectionId = invoiceRejectionId;
    }

    public LongFilter getInvoiceSubmissionId() {
        return invoiceSubmissionId;
    }

    public void setInvoiceSubmissionId(LongFilter invoiceSubmissionId) {
        this.invoiceSubmissionId = invoiceSubmissionId;
    }

    public LongFilter getInvoiceValidationId() {
        return invoiceValidationId;
    }

    public void setInvoiceValidationId(LongFilter invoiceValidationId) {
        this.invoiceValidationId = invoiceValidationId;
    }

    public LongFilter getTypeInvoiceId() {
        return typeInvoiceId;
    }

    public void setTypeInvoiceId(LongFilter typeInvoiceId) {
        this.typeInvoiceId = typeInvoiceId;
    }

    @Override
    public String toString() {
        return "InvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (dateInvoice != null ? "dateInvoice=" + dateInvoice + ", " : "") +
                (rate != null ? "rate=" + rate + ", " : "") +
                (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
                (invoiceRejectionId != null ? "invoiceRejectionId=" + invoiceRejectionId + ", " : "") +
                (invoiceSubmissionId != null ? "invoiceSubmissionId=" + invoiceSubmissionId + ", " : "") +
                (invoiceValidationId != null ? "invoiceValidationId=" + invoiceValidationId + ", " : "") +
                (typeInvoiceId != null ? "typeInvoiceId=" + typeInvoiceId + ", " : "") +
            "}";
    }

}

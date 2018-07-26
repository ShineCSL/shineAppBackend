package com.shine.shineappback.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the AccountDetails entity. This class is used in AccountDetailsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /account-details?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountDetailsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private DoubleFilter amount;

    private DoubleFilter rate;

    private StringFilter label;

    private StringFilter description;

    private StringFilter type;

    private LongFilter clientId;

    private LongFilter invoiceId;

    private LongFilter currencyId;

    public AccountDetailsCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public DoubleFilter getRate() {
        return rate;
    }

    public void setRate(DoubleFilter rate) {
        this.rate = rate;
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

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "AccountDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (rate != null ? "rate=" + rate + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
                (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
            "}";
    }

}

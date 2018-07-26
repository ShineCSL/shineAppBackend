package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InvoiceRejection entity.
 */
public class InvoiceRejectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean rejected;

    @NotNull
    private LocalDate dateInvoice;

    private String comment;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceRejectionDTO invoiceRejectionDTO = (InvoiceRejectionDTO) o;
        if (invoiceRejectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceRejectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceRejectionDTO{" +
            "id=" + getId() +
            ", rejected='" + isRejected() + "'" +
            ", dateInvoice='" + getDateInvoice() + "'" +
            ", comment='" + getComment() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}

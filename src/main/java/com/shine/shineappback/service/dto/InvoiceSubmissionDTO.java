package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InvoiceSubmission entity.
 */
public class InvoiceSubmissionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean submitted;

    @NotNull
    private LocalDate dateInvoice;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
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

        InvoiceSubmissionDTO invoiceSubmissionDTO = (InvoiceSubmissionDTO) o;
        if (invoiceSubmissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceSubmissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceSubmissionDTO{" +
            "id=" + getId() +
            ", submitted='" + isSubmitted() + "'" +
            ", dateInvoice='" + getDateInvoice() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}

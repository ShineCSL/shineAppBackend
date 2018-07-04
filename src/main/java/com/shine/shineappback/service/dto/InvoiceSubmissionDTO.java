package com.shine.shineappback.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InvoiceSubmission entity.
 */
public class InvoiceSubmissionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean submitted;

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
            "}";
    }
}

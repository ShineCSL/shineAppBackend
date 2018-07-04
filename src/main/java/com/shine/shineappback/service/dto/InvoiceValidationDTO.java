package com.shine.shineappback.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InvoiceValidation entity.
 */
public class InvoiceValidationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean validated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceValidationDTO invoiceValidationDTO = (InvoiceValidationDTO) o;
        if (invoiceValidationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceValidationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceValidationDTO{" +
            "id=" + getId() +
            ", validated='" + isValidated() + "'" +
            "}";
    }
}

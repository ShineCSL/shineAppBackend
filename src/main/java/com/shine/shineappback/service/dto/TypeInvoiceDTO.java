package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TypeInvoice entity.
 */
public class TypeInvoiceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String labelEn;

    private String labelFr;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()\\[\\]#$+*%\\-_/\\\\]*$")
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public void setLabelEn(String labelEn) {
        this.labelEn = labelEn;
    }

    public String getLabelFr() {
        return labelFr;
    }

    public void setLabelFr(String labelFr) {
        this.labelFr = labelFr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeInvoiceDTO typeInvoiceDTO = (TypeInvoiceDTO) o;
        if (typeInvoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeInvoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeInvoiceDTO{" +
            "id=" + getId() +
            ", labelEn='" + getLabelEn() + "'" +
            ", labelFr='" + getLabelFr() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}

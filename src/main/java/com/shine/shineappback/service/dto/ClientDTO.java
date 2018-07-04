package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Client entity.
 */
public class ClientDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String label;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()\\[\\]#$+*%\\-_/\\\\]*$")
    private String code;

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

        ClientDTO clientDTO = (ClientDTO) o;
        if (clientDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}

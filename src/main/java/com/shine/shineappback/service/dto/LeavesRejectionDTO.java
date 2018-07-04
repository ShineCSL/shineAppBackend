package com.shine.shineappback.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesRejection entity.
 */
public class LeavesRejectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean rejected;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LeavesRejectionDTO leavesRejectionDTO = (LeavesRejectionDTO) o;
        if (leavesRejectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesRejectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesRejectionDTO{" +
            "id=" + getId() +
            ", rejected='" + isRejected() + "'" +
            "}";
    }
}

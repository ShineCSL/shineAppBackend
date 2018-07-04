package com.shine.shineappback.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesSubmission entity.
 */
public class LeavesSubmissionDTO extends AbstractAuditingDTO implements Serializable {

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

        LeavesSubmissionDTO leavesSubmissionDTO = (LeavesSubmissionDTO) o;
        if (leavesSubmissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesSubmissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesSubmissionDTO{" +
            "id=" + getId() +
            ", submitted='" + isSubmitted() + "'" +
            "}";
    }
}

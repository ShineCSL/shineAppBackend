package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesSubmission entity.
 */
public class LeavesSubmissionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean submitted;

    @NotNull
    private LocalDate leavesDate;

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

    public LocalDate getLeavesDate() {
        return leavesDate;
    }

    public void setLeavesDate(LocalDate leavesDate) {
        this.leavesDate = leavesDate;
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
            ", leavesDate='" + getLeavesDate() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}

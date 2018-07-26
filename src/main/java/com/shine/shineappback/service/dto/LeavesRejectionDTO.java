package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesRejection entity.
 */
public class LeavesRejectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean rejected;

    @NotNull
    private LocalDate leavesDate;

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

    public LocalDate getLeavesDate() {
        return leavesDate;
    }

    public void setLeavesDate(LocalDate leavesDate) {
        this.leavesDate = leavesDate;
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
            ", leavesDate='" + getLeavesDate() + "'" +
            ", comment='" + getComment() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}

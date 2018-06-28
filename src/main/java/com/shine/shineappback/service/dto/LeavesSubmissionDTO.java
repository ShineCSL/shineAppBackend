package com.shine.shineappback.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesSubmission entity.
 */
public class LeavesSubmissionDTO implements Serializable {

    private Long id;

    private Boolean submitted;

    private ZonedDateTime dateModification;

    @NotNull
    private ZonedDateTime dateCreation;

    private Long userModificationId;

    private String userModificationLogin;

    private Long userCreationId;

    private String userCreationLogin;

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

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getUserModificationId() {
        return userModificationId;
    }

    public void setUserModificationId(Long userId) {
        this.userModificationId = userId;
    }

    public String getUserModificationLogin() {
        return userModificationLogin;
    }

    public void setUserModificationLogin(String userLogin) {
        this.userModificationLogin = userLogin;
    }

    public Long getUserCreationId() {
        return userCreationId;
    }

    public void setUserCreationId(Long userId) {
        this.userCreationId = userId;
    }

    public String getUserCreationLogin() {
        return userCreationLogin;
    }

    public void setUserCreationLogin(String userLogin) {
        this.userCreationLogin = userLogin;
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
            ", dateModification='" + getDateModification() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            "}";
    }
}

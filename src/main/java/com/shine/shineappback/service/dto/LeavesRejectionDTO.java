package com.shine.shineappback.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesRejection entity.
 */
public class LeavesRejectionDTO implements Serializable {

    private Long id;

    private Boolean rejected;

    private ZonedDateTime dateModification;

    @NotNull
    private ZonedDateTime dateCreation;

    private Long userCreationId;

    private String userCreationLogin;

    private Long userModificationId;

    private String userModificationLogin;

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
            ", dateModification='" + getDateModification() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            "}";
    }
}

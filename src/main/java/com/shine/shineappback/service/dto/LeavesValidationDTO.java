package com.shine.shineappback.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeavesValidation entity.
 */
public class LeavesValidationDTO implements Serializable {

    private Long id;

    private Boolean validated;

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

    public Boolean isValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
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

        LeavesValidationDTO leavesValidationDTO = (LeavesValidationDTO) o;
        if (leavesValidationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesValidationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesValidationDTO{" +
            "id=" + getId() +
            ", validated='" + isValidated() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            "}";
    }
}

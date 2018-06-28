package com.shine.shineappback.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeaveConfig entity.
 */
public class LeaveConfigDTO implements Serializable {

    private Long id;

    private Integer nbSickLeaves;

    private Integer nbAnnualLeaves;

    private Integer nbSpecialLeaves;

    @NotNull
    private ZonedDateTime dateCreation;

    private ZonedDateTime dateModification;

    private Long userId;

    private String userLogin;

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

    public Integer getNbSickLeaves() {
        return nbSickLeaves;
    }

    public void setNbSickLeaves(Integer nbSickLeaves) {
        this.nbSickLeaves = nbSickLeaves;
    }

    public Integer getNbAnnualLeaves() {
        return nbAnnualLeaves;
    }

    public void setNbAnnualLeaves(Integer nbAnnualLeaves) {
        this.nbAnnualLeaves = nbAnnualLeaves;
    }

    public Integer getNbSpecialLeaves() {
        return nbSpecialLeaves;
    }

    public void setNbSpecialLeaves(Integer nbSpecialLeaves) {
        this.nbSpecialLeaves = nbSpecialLeaves;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
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

        LeaveConfigDTO leaveConfigDTO = (LeaveConfigDTO) o;
        if (leaveConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaveConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeaveConfigDTO{" +
            "id=" + getId() +
            ", nbSickLeaves=" + getNbSickLeaves() +
            ", nbAnnualLeaves=" + getNbAnnualLeaves() +
            ", nbSpecialLeaves=" + getNbSpecialLeaves() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            "}";
    }
}

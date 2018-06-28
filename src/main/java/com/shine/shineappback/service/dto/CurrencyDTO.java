package com.shine.shineappback.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Currency entity.
 */
public class CurrencyDTO implements Serializable {

    private Long id;

    private String labelEn;

    private String labelFr;

    @NotNull
    private ZonedDateTime dateCreation;

    private ZonedDateTime dateModification;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()[\\\\]+-_*/%]*$")
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

        CurrencyDTO currencyDTO = (CurrencyDTO) o;
        if (currencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
            "id=" + getId() +
            ", labelEn='" + getLabelEn() + "'" +
            ", labelFr='" + getLabelFr() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", code='" + getCode() + "'" +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            "}";
    }
}

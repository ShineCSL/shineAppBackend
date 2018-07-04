package com.shine.shineappback.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ActivityValidation entity.
 */
public class ActivityValidationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Integer weekNumber;

    private Integer year;

    private Boolean validated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean isValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityValidationDTO activityValidationDTO = (ActivityValidationDTO) o;
        if (activityValidationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityValidationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityValidationDTO{" +
            "id=" + getId() +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            ", validated='" + isValidated() + "'" +
            "}";
    }
}

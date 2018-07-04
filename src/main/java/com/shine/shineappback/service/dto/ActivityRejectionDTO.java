package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ActivityRejection entity.
 */
public class ActivityRejectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean rejected;

    @NotNull
    private Integer weekNumber;

    @NotNull
    private Integer year;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityRejectionDTO activityRejectionDTO = (ActivityRejectionDTO) o;
        if (activityRejectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityRejectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityRejectionDTO{" +
            "id=" + getId() +
            ", rejected='" + isRejected() + "'" +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            "}";
    }
}

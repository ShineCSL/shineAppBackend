package com.shine.shineappback.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ActivitySubmission entity.
 */
public class ActivitySubmissionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean submitted;

    private Integer year;

    private Integer weekNumber;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivitySubmissionDTO activitySubmissionDTO = (ActivitySubmissionDTO) o;
        if (activitySubmissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activitySubmissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivitySubmissionDTO{" +
            "id=" + getId() +
            ", submitted='" + isSubmitted() + "'" +
            ", year=" + getYear() +
            ", weekNumber=" + getWeekNumber() +
            "}";
    }
}

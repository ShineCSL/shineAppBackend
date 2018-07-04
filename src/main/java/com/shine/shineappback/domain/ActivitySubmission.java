package com.shine.shineappback.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ActivitySubmission.
 */
@Entity
@Table(name = "activity_submission")
public class ActivitySubmission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submitted")
    private Boolean submitted;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "week_number")
    private Integer weekNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSubmitted() {
        return submitted;
    }

    public ActivitySubmission submitted(Boolean submitted) {
        this.submitted = submitted;
        return this;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public Integer getYear() {
        return year;
    }

    public ActivitySubmission year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public ActivitySubmission weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivitySubmission activitySubmission = (ActivitySubmission) o;
        if (activitySubmission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activitySubmission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivitySubmission{" +
            "id=" + getId() +
            ", submitted='" + isSubmitted() + "'" +
            ", year=" + getYear() +
            ", weekNumber=" + getWeekNumber() +
            "}";
    }
}

package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ActivityValidation.
 */
@Entity
@Table(
name = "activity_validation",
uniqueConstraints = {@UniqueConstraint(columnNames = {"week_number", "jhi_year", "user_id"})}
)
public class ActivityValidation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "week_number")
    private Integer weekNumber;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "validated")
    private Boolean validated;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public ActivityValidation weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getYear() {
        return year;
    }

    public ActivityValidation year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean isValidated() {
        return validated;
    }

    public ActivityValidation validated(Boolean validated) {
        this.validated = validated;
        return this;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public User getUser() {
        return user;
    }

    public ActivityValidation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        ActivityValidation activityValidation = (ActivityValidation) o;
        if (activityValidation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityValidation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityValidation{" +
            "id=" + getId() +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            ", validated='" + isValidated() + "'" +
            "}";
    }
}

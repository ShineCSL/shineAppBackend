package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ActivityRejection.
 */
@Entity
@Table(name = "activity_rejection")
public class ActivityRejection extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rejected")
    private Boolean rejected;

    @NotNull
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    @Column(name = "jhi_comment")
    private String comment;

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

    public Boolean isRejected() {
        return rejected;
    }

    public ActivityRejection rejected(Boolean rejected) {
        this.rejected = rejected;
        return this;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public ActivityRejection weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getYear() {
        return year;
    }

    public ActivityRejection year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getComment() {
        return comment;
    }

    public ActivityRejection comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public ActivityRejection user(User user) {
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
        ActivityRejection activityRejection = (ActivityRejection) o;
        if (activityRejection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityRejection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityRejection{" +
            "id=" + getId() +
            ", rejected='" + isRejected() + "'" +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}

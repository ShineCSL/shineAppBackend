package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
public class Activity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @NotNull
    @Column(name = "nb_of_hours", nullable = false)
    private Double nbOfHours;

    @NotNull
    @Column(name = "day", nullable = false)
    private Integer day;

    @NotNull
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Task task;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Mission mission;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ActivityRejection activityRejection;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ActivitySubmission activitySubmission;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ActivityValidation activityValidation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public Activity activityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
        return this;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public Double getNbOfHours() {
        return nbOfHours;
    }

    public Activity nbOfHours(Double nbOfHours) {
        this.nbOfHours = nbOfHours;
        return this;
    }

    public void setNbOfHours(Double nbOfHours) {
        this.nbOfHours = nbOfHours;
    }

    public Integer getDay() {
        return day;
    }

    public Activity day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public Activity weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getYear() {
        return year;
    }

    public Activity year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public Activity month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Task getTask() {
        return task;
    }

    public Activity task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public Activity user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mission getMission() {
        return mission;
    }

    public Activity mission(Mission mission) {
        this.mission = mission;
        return this;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public ActivityRejection getActivityRejection() {
        return activityRejection;
    }

    public Activity activityRejection(ActivityRejection activityRejection) {
        this.activityRejection = activityRejection;
        return this;
    }

    public void setActivityRejection(ActivityRejection activityRejection) {
        this.activityRejection = activityRejection;
    }

    public ActivitySubmission getActivitySubmission() {
        return activitySubmission;
    }

    public Activity activitySubmission(ActivitySubmission activitySubmission) {
        this.activitySubmission = activitySubmission;
        return this;
    }

    public void setActivitySubmission(ActivitySubmission activitySubmission) {
        this.activitySubmission = activitySubmission;
    }

    public ActivityValidation getActivityValidation() {
        return activityValidation;
    }

    public Activity activityValidation(ActivityValidation activityValidation) {
        this.activityValidation = activityValidation;
        return this;
    }

    public void setActivityValidation(ActivityValidation activityValidation) {
        this.activityValidation = activityValidation;
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
        Activity activity = (Activity) o;
        if (activity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", activityDate='" + getActivityDate() + "'" +
            ", nbOfHours=" + getNbOfHours() +
            ", day=" + getDay() +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            "}";
    }
}

package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Leaves.
 */
@Entity
@Table(name = "leaves")
public class Leaves extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "leave_date", nullable = false)
    private LocalDate leaveDate;

    @NotNull
    @Column(name = "nb_of_hours", nullable = false)
    private Double nbOfHours;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "week_number")
    private Integer weekNumber;

    @Column(name = "jhi_comment")
    private String comment;

    @NotNull
    @Column(name = "day", nullable = false)
    private Integer day;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Task task;

    @OneToOne
    @JoinColumn(unique = true)
    private LeavesSubmission leavesSubmission;

    @OneToOne
    @JoinColumn(unique = true)
    private LeavesValidation leavesValidation;

    @OneToOne
    @JoinColumn(unique = true)
    private LeavesRejection leavesRejection;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public Leaves leaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
        return this;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Double getNbOfHours() {
        return nbOfHours;
    }

    public Leaves nbOfHours(Double nbOfHours) {
        this.nbOfHours = nbOfHours;
        return this;
    }

    public void setNbOfHours(Double nbOfHours) {
        this.nbOfHours = nbOfHours;
    }

    public Integer getYear() {
        return year;
    }

    public Leaves year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public Leaves weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getComment() {
        return comment;
    }

    public Leaves comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDay() {
        return day;
    }

    public Leaves day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public User getUser() {
        return user;
    }

    public Leaves user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public Leaves task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LeavesSubmission getLeavesSubmission() {
        return leavesSubmission;
    }

    public Leaves leavesSubmission(LeavesSubmission leavesSubmission) {
        this.leavesSubmission = leavesSubmission;
        return this;
    }

    public void setLeavesSubmission(LeavesSubmission leavesSubmission) {
        this.leavesSubmission = leavesSubmission;
    }

    public LeavesValidation getLeavesValidation() {
        return leavesValidation;
    }

    public Leaves leavesValidation(LeavesValidation leavesValidation) {
        this.leavesValidation = leavesValidation;
        return this;
    }

    public void setLeavesValidation(LeavesValidation leavesValidation) {
        this.leavesValidation = leavesValidation;
    }

    public LeavesRejection getLeavesRejection() {
        return leavesRejection;
    }

    public Leaves leavesRejection(LeavesRejection leavesRejection) {
        this.leavesRejection = leavesRejection;
        return this;
    }

    public void setLeavesRejection(LeavesRejection leavesRejection) {
        this.leavesRejection = leavesRejection;
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
        Leaves leaves = (Leaves) o;
        if (leaves.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaves.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Leaves{" +
            "id=" + getId() +
            ", leaveDate='" + getLeaveDate() + "'" +
            ", nbOfHours=" + getNbOfHours() +
            ", year=" + getYear() +
            ", weekNumber=" + getWeekNumber() +
            ", comment='" + getComment() + "'" +
            ", day=" + getDay() +
            "}";
    }
}

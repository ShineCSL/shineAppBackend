package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Leaves entity.
 */
public class LeavesDTO implements Serializable {

    private Long id;

    private Boolean fullDay;

    @NotNull
    private LocalDate leavesFrom;

    @NotNull
    private LocalDate leavesTo;

    @NotNull
    private Integer nbOfHours;

    private Integer year;

    private Integer weekNumber;

    private String comment;

    @NotNull
    private Integer day;

    @NotNull
    private Integer month;

    private Long userId;

    private String userLogin;

    private Long taskId;

    private String taskCode;

    private Long leavesSubmissionId;

    private String leavesSubmissionSubmitted;

    private Long leavesValidationId;

    private String leavesValidationValidated;

    private Long leavesRejectionId;

    private String leavesRejectionRejected;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isFullDay() {
        return fullDay;
    }

    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    public LocalDate getLeavesFrom() {
        return leavesFrom;
    }

    public void setLeavesFrom(LocalDate leavesFrom) {
        this.leavesFrom = leavesFrom;
    }

    public LocalDate getLeavesTo() {
        return leavesTo;
    }

    public void setLeavesTo(LocalDate leavesTo) {
        this.leavesTo = leavesTo;
    }

    public Integer getNbOfHours() {
        return nbOfHours;
    }

    public void setNbOfHours(Integer nbOfHours) {
        this.nbOfHours = nbOfHours;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Long getLeavesSubmissionId() {
        return leavesSubmissionId;
    }

    public void setLeavesSubmissionId(Long leavesSubmissionId) {
        this.leavesSubmissionId = leavesSubmissionId;
    }

    public String getLeavesSubmissionSubmitted() {
        return leavesSubmissionSubmitted;
    }

    public void setLeavesSubmissionSubmitted(String leavesSubmissionSubmitted) {
        this.leavesSubmissionSubmitted = leavesSubmissionSubmitted;
    }

    public Long getLeavesValidationId() {
        return leavesValidationId;
    }

    public void setLeavesValidationId(Long leavesValidationId) {
        this.leavesValidationId = leavesValidationId;
    }

    public String getLeavesValidationValidated() {
        return leavesValidationValidated;
    }

    public void setLeavesValidationValidated(String leavesValidationValidated) {
        this.leavesValidationValidated = leavesValidationValidated;
    }

    public Long getLeavesRejectionId() {
        return leavesRejectionId;
    }

    public void setLeavesRejectionId(Long leavesRejectionId) {
        this.leavesRejectionId = leavesRejectionId;
    }

    public String getLeavesRejectionRejected() {
        return leavesRejectionRejected;
    }

    public void setLeavesRejectionRejected(String leavesRejectionRejected) {
        this.leavesRejectionRejected = leavesRejectionRejected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LeavesDTO leavesDTO = (LeavesDTO) o;
        if (leavesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesDTO{" +
            "id=" + getId() +
            ", fullDay='" + isFullDay() + "'" +
            ", leavesFrom='" + getLeavesFrom() + "'" +
            ", leavesTo='" + getLeavesTo() + "'" +
            ", nbOfHours=" + getNbOfHours() +
            ", year=" + getYear() +
            ", weekNumber=" + getWeekNumber() +
            ", comment='" + getComment() + "'" +
            ", day=" + getDay() +
            ", month=" + getMonth() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", task=" + getTaskId() +
            ", task='" + getTaskCode() + "'" +
            ", leavesSubmission=" + getLeavesSubmissionId() +
            ", leavesSubmission='" + getLeavesSubmissionSubmitted() + "'" +
            ", leavesValidation=" + getLeavesValidationId() +
            ", leavesValidation='" + getLeavesValidationValidated() + "'" +
            ", leavesRejection=" + getLeavesRejectionId() +
            ", leavesRejection='" + getLeavesRejectionRejected() + "'" +
            "}";
    }
}

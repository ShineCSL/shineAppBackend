package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Leaves entity.
 */
public class LeavesDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate leaveDate;

    @NotNull
    private Double nbOfHours;

    private Integer year;

    private ZonedDateTime dateCreation;

    private ZonedDateTime dateModification;

    private Integer weekNumber;

    private String comment;

    private Long userId;

    private String userLogin;

    private Long leavesSubmissionId;

    private Long leavesValidationId;

    private Long leavesRejectionId;

    private Long taskId;

    private String taskCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Double getNbOfHours() {
        return nbOfHours;
    }

    public void setNbOfHours(Double nbOfHours) {
        this.nbOfHours = nbOfHours;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public Long getLeavesSubmissionId() {
        return leavesSubmissionId;
    }

    public void setLeavesSubmissionId(Long leavesSubmissionId) {
        this.leavesSubmissionId = leavesSubmissionId;
    }

    public Long getLeavesValidationId() {
        return leavesValidationId;
    }

    public void setLeavesValidationId(Long leavesValidationId) {
        this.leavesValidationId = leavesValidationId;
    }

    public Long getLeavesRejectionId() {
        return leavesRejectionId;
    }

    public void setLeavesRejectionId(Long leavesRejectionId) {
        this.leavesRejectionId = leavesRejectionId;
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
            ", leaveDate='" + getLeaveDate() + "'" +
            ", nbOfHours=" + getNbOfHours() +
            ", year=" + getYear() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", weekNumber=" + getWeekNumber() +
            ", comment='" + getComment() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", leavesSubmission=" + getLeavesSubmissionId() +
            ", leavesValidation=" + getLeavesValidationId() +
            ", leavesRejection=" + getLeavesRejectionId() +
            ", task=" + getTaskId() +
            ", task='" + getTaskCode() + "'" +
            "}";
    }
}

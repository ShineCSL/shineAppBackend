package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Activity entity.
 */
public class ActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate activityDate;

    @NotNull
    private Double nbOfHours;

    @NotNull
    private Integer day;

    @NotNull
    private Integer weekNumber;

    @NotNull
    private Integer year;

    @NotNull
    private ZonedDateTime dateCreation;

    private ZonedDateTime dateModification;

    private Long taskId;

    private String taskCode;

    private Long userId;

    private String userLogin;

    private Long activitySubmissionId;

    private Long activityValidationId;

    private Long activityRejectionId;

    private Long userCreationId;

    private String userCreationLogin;

    private Long userModificationId;

    private String userModificationLogin;

    private Long missionId;

    private String missionCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public Double getNbOfHours() {
        return nbOfHours;
    }

    public void setNbOfHours(Double nbOfHours) {
        this.nbOfHours = nbOfHours;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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

    public Long getActivitySubmissionId() {
        return activitySubmissionId;
    }

    public void setActivitySubmissionId(Long activitySubmissionId) {
        this.activitySubmissionId = activitySubmissionId;
    }

    public Long getActivityValidationId() {
        return activityValidationId;
    }

    public void setActivityValidationId(Long activityValidationId) {
        this.activityValidationId = activityValidationId;
    }

    public Long getActivityRejectionId() {
        return activityRejectionId;
    }

    public void setActivityRejectionId(Long activityRejectionId) {
        this.activityRejectionId = activityRejectionId;
    }

    public Long getUserCreationId() {
        return userCreationId;
    }

    public void setUserCreationId(Long userId) {
        this.userCreationId = userId;
    }

    public String getUserCreationLogin() {
        return userCreationLogin;
    }

    public void setUserCreationLogin(String userLogin) {
        this.userCreationLogin = userLogin;
    }

    public Long getUserModificationId() {
        return userModificationId;
    }

    public void setUserModificationId(Long userId) {
        this.userModificationId = userId;
    }

    public String getUserModificationLogin() {
        return userModificationLogin;
    }

    public void setUserModificationLogin(String userLogin) {
        this.userModificationLogin = userLogin;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getMissionCode() {
        return missionCode;
    }

    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (activityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
            "id=" + getId() +
            ", activityDate='" + getActivityDate() + "'" +
            ", nbOfHours=" + getNbOfHours() +
            ", day=" + getDay() +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", task=" + getTaskId() +
            ", task='" + getTaskCode() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", activitySubmission=" + getActivitySubmissionId() +
            ", activityValidation=" + getActivityValidationId() +
            ", activityRejection=" + getActivityRejectionId() +
            ", userCreation=" + getUserCreationId() +
            ", userCreation='" + getUserCreationLogin() + "'" +
            ", userModification=" + getUserModificationId() +
            ", userModification='" + getUserModificationLogin() + "'" +
            ", mission=" + getMissionId() +
            ", mission='" + getMissionCode() + "'" +
            "}";
    }
}

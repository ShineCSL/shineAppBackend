package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Activity entity.
 */
public class ActivityDTO extends AbstractAuditingDTO implements Serializable {

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
    private Integer month;

    private Long taskId;

    private String taskCode;

    private Long userId;

    private String userLogin;

    private Long missionId;

    private String missionCode;

    private Long activityRejectionId;

    private String activityRejectionRejected;

    private Long activitySubmissionId;

    private String activitySubmissionSubmitted;

    private Long activityValidationId;

    private String activityValidationValidated;

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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
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

    public Long getActivityRejectionId() {
        return activityRejectionId;
    }

    public void setActivityRejectionId(Long activityRejectionId) {
        this.activityRejectionId = activityRejectionId;
    }

    public String getActivityRejectionRejected() {
        return activityRejectionRejected;
    }

    public void setActivityRejectionRejected(String activityRejectionRejected) {
        this.activityRejectionRejected = activityRejectionRejected;
    }

    public Long getActivitySubmissionId() {
        return activitySubmissionId;
    }

    public void setActivitySubmissionId(Long activitySubmissionId) {
        this.activitySubmissionId = activitySubmissionId;
    }

    public String getActivitySubmissionSubmitted() {
        return activitySubmissionSubmitted;
    }

    public void setActivitySubmissionSubmitted(String activitySubmissionSubmitted) {
        this.activitySubmissionSubmitted = activitySubmissionSubmitted;
    }

    public Long getActivityValidationId() {
        return activityValidationId;
    }

    public void setActivityValidationId(Long activityValidationId) {
        this.activityValidationId = activityValidationId;
    }

    public String getActivityValidationValidated() {
        return activityValidationValidated;
    }

    public void setActivityValidationValidated(String activityValidationValidated) {
        this.activityValidationValidated = activityValidationValidated;
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
            ", month=" + getMonth() +
            ", task=" + getTaskId() +
            ", task='" + getTaskCode() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", mission=" + getMissionId() +
            ", mission='" + getMissionCode() + "'" +
            ", activityRejection=" + getActivityRejectionId() +
            ", activityRejection='" + getActivityRejectionRejected() + "'" +
            ", activitySubmission=" + getActivitySubmissionId() +
            ", activitySubmission='" + getActivitySubmissionSubmitted() + "'" +
            ", activityValidation=" + getActivityValidationId() +
            ", activityValidation='" + getActivityValidationValidated() + "'" +
            "}";
    }
}

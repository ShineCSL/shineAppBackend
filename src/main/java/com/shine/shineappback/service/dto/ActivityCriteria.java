package com.shine.shineappback.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Activity entity. This class is used in ActivityResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /activities?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActivityCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter activityDate;

    private DoubleFilter nbOfHours;

    private IntegerFilter day;

    private IntegerFilter weekNumber;

    private IntegerFilter year;

    private IntegerFilter month;

    private LongFilter taskId;

    private LongFilter userId;

    private LongFilter missionId;

    private LongFilter activityRejectionId;

    private LongFilter activitySubmissionId;

    private LongFilter activityValidationId;

    public ActivityCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDateFilter activityDate) {
        this.activityDate = activityDate;
    }

    public DoubleFilter getNbOfHours() {
        return nbOfHours;
    }

    public void setNbOfHours(DoubleFilter nbOfHours) {
        this.nbOfHours = nbOfHours;
    }

    public IntegerFilter getDay() {
        return day;
    }

    public void setDay(IntegerFilter day) {
        this.day = day;
    }

    public IntegerFilter getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(IntegerFilter weekNumber) {
        this.weekNumber = weekNumber;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public IntegerFilter getMonth() {
        return month;
    }

    public void setMonth(IntegerFilter month) {
        this.month = month;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getMissionId() {
        return missionId;
    }

    public void setMissionId(LongFilter missionId) {
        this.missionId = missionId;
    }

    public LongFilter getActivityRejectionId() {
        return activityRejectionId;
    }

    public void setActivityRejectionId(LongFilter activityRejectionId) {
        this.activityRejectionId = activityRejectionId;
    }

    public LongFilter getActivitySubmissionId() {
        return activitySubmissionId;
    }

    public void setActivitySubmissionId(LongFilter activitySubmissionId) {
        this.activitySubmissionId = activitySubmissionId;
    }

    public LongFilter getActivityValidationId() {
        return activityValidationId;
    }

    public void setActivityValidationId(LongFilter activityValidationId) {
        this.activityValidationId = activityValidationId;
    }

    @Override
    public String toString() {
        return "ActivityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (activityDate != null ? "activityDate=" + activityDate + ", " : "") +
                (nbOfHours != null ? "nbOfHours=" + nbOfHours + ", " : "") +
                (day != null ? "day=" + day + ", " : "") +
                (weekNumber != null ? "weekNumber=" + weekNumber + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (month != null ? "month=" + month + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (missionId != null ? "missionId=" + missionId + ", " : "") +
                (activityRejectionId != null ? "activityRejectionId=" + activityRejectionId + ", " : "") +
                (activitySubmissionId != null ? "activitySubmissionId=" + activitySubmissionId + ", " : "") +
                (activityValidationId != null ? "activityValidationId=" + activityValidationId + ", " : "") +
            "}";
    }

}

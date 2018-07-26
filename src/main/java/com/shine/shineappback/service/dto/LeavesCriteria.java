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
 * Criteria class for the Leaves entity. This class is used in LeavesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /leaves?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeavesCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private BooleanFilter fullDay;

    private LocalDateFilter leavesFrom;

    private LocalDateFilter leavesTo;

    private IntegerFilter nbOfHours;

    private IntegerFilter year;

    private IntegerFilter weekNumber;

    private StringFilter comment;

    private IntegerFilter day;

    private IntegerFilter month;

    private LongFilter userId;

    private LongFilter taskId;

    private LongFilter leavesSubmissionId;

    private LongFilter leavesValidationId;

    private LongFilter leavesRejectionId;

    public LeavesCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getFullDay() {
        return fullDay;
    }

    public void setFullDay(BooleanFilter fullDay) {
        this.fullDay = fullDay;
    }

    public LocalDateFilter getLeavesFrom() {
        return leavesFrom;
    }

    public void setLeavesFrom(LocalDateFilter leavesFrom) {
        this.leavesFrom = leavesFrom;
    }

    public LocalDateFilter getLeavesTo() {
        return leavesTo;
    }

    public void setLeavesTo(LocalDateFilter leavesTo) {
        this.leavesTo = leavesTo;
    }

    public IntegerFilter getNbOfHours() {
        return nbOfHours;
    }

    public void setNbOfHours(IntegerFilter nbOfHours) {
        this.nbOfHours = nbOfHours;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public IntegerFilter getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(IntegerFilter weekNumber) {
        this.weekNumber = weekNumber;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public IntegerFilter getDay() {
        return day;
    }

    public void setDay(IntegerFilter day) {
        this.day = day;
    }

    public IntegerFilter getMonth() {
        return month;
    }

    public void setMonth(IntegerFilter month) {
        this.month = month;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getLeavesSubmissionId() {
        return leavesSubmissionId;
    }

    public void setLeavesSubmissionId(LongFilter leavesSubmissionId) {
        this.leavesSubmissionId = leavesSubmissionId;
    }

    public LongFilter getLeavesValidationId() {
        return leavesValidationId;
    }

    public void setLeavesValidationId(LongFilter leavesValidationId) {
        this.leavesValidationId = leavesValidationId;
    }

    public LongFilter getLeavesRejectionId() {
        return leavesRejectionId;
    }

    public void setLeavesRejectionId(LongFilter leavesRejectionId) {
        this.leavesRejectionId = leavesRejectionId;
    }

    @Override
    public String toString() {
        return "LeavesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullDay != null ? "fullDay=" + fullDay + ", " : "") +
                (leavesFrom != null ? "leavesFrom=" + leavesFrom + ", " : "") +
                (leavesTo != null ? "leavesTo=" + leavesTo + ", " : "") +
                (nbOfHours != null ? "nbOfHours=" + nbOfHours + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (weekNumber != null ? "weekNumber=" + weekNumber + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (day != null ? "day=" + day + ", " : "") +
                (month != null ? "month=" + month + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
                (leavesSubmissionId != null ? "leavesSubmissionId=" + leavesSubmissionId + ", " : "") +
                (leavesValidationId != null ? "leavesValidationId=" + leavesValidationId + ", " : "") +
                (leavesRejectionId != null ? "leavesRejectionId=" + leavesRejectionId + ", " : "") +
            "}";
    }

}

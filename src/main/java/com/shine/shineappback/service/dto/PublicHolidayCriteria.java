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
 * Criteria class for the PublicHoliday entity. This class is used in PublicHolidayResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /public-holidays?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PublicHolidayCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter label;

    private LocalDateFilter dateHoliday;

    private IntegerFilter day;

    private IntegerFilter weekNumber;

    private IntegerFilter month;

    private IntegerFilter year;

    public PublicHolidayCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public LocalDateFilter getDateHoliday() {
        return dateHoliday;
    }

    public void setDateHoliday(LocalDateFilter dateHoliday) {
        this.dateHoliday = dateHoliday;
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

    public IntegerFilter getMonth() {
        return month;
    }

    public void setMonth(IntegerFilter month) {
        this.month = month;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "PublicHolidayCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (dateHoliday != null ? "dateHoliday=" + dateHoliday + ", " : "") +
                (day != null ? "day=" + day + ", " : "") +
                (weekNumber != null ? "weekNumber=" + weekNumber + ", " : "") +
                (month != null ? "month=" + month + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
            "}";
    }

}

package com.shine.shineappback.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PublicHoliday entity.
 */
public class PublicHolidayDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    @NotNull
    private LocalDate dateHoliday;

    @NotNull
    private Integer day;

    @NotNull
    private Integer weekNumber;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getDateHoliday() {
        return dateHoliday;
    }

    public void setDateHoliday(LocalDate dateHoliday) {
        this.dateHoliday = dateHoliday;
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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicHolidayDTO publicHolidayDTO = (PublicHolidayDTO) o;
        if (publicHolidayDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publicHolidayDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PublicHolidayDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", dateHoliday='" + getDateHoliday() + "'" +
            ", day=" + getDay() +
            ", weekNumber=" + getWeekNumber() +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            "}";
    }
}

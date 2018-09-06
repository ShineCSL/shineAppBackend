package com.shine.shineappback.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PublicHoliday.
 */
@Entity
@Table(
		name = "public_holiday", 
		uniqueConstraints = {@UniqueConstraint(columnNames = {"jhi_label", "date_holiday"})}
)
public class PublicHoliday implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_label", nullable = false)
    private String label;

    @NotNull
    @Column(name = "date_holiday", nullable = false)
    private LocalDate dateHoliday;

    @NotNull
    @Column(name = "day", nullable = false)
    private Integer day;

    @NotNull
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;

    @NotNull
    @Column(name = "jhi_year", nullable = false)
    private Integer year;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public PublicHoliday label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getDateHoliday() {
        return dateHoliday;
    }

    public PublicHoliday dateHoliday(LocalDate dateHoliday) {
        this.dateHoliday = dateHoliday;
        return this;
    }

    public void setDateHoliday(LocalDate dateHoliday) {
        this.dateHoliday = dateHoliday;
    }

    public Integer getDay() {
        return day;
    }

    public PublicHoliday day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public PublicHoliday weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getMonth() {
        return month;
    }

    public PublicHoliday month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public PublicHoliday year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
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
        PublicHoliday publicHoliday = (PublicHoliday) o;
        if (publicHoliday.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publicHoliday.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PublicHoliday{" +
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

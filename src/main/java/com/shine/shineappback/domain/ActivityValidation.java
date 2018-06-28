package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ActivityValidation.
 */
@Entity
@Table(name = "activity_validation")
public class ActivityValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "week_number")
    private Integer weekNumber;

    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "jhi_validation")
    private Boolean validation;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

    @Column(name = "date_modification")
    private ZonedDateTime dateModification;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User userModification;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User userCreation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public ActivityValidation weekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getYear() {
        return year;
    }

    public ActivityValidation year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean isValidation() {
        return validation;
    }

    public ActivityValidation validation(Boolean validation) {
        this.validation = validation;
        return this;
    }

    public void setValidation(Boolean validation) {
        this.validation = validation;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public ActivityValidation dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public ActivityValidation dateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public User getUserModification() {
        return userModification;
    }

    public ActivityValidation userModification(User user) {
        this.userModification = user;
        return this;
    }

    public void setUserModification(User user) {
        this.userModification = user;
    }

    public User getUserCreation() {
        return userCreation;
    }

    public ActivityValidation userCreation(User user) {
        this.userCreation = user;
        return this;
    }

    public void setUserCreation(User user) {
        this.userCreation = user;
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
        ActivityValidation activityValidation = (ActivityValidation) o;
        if (activityValidation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityValidation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityValidation{" +
            "id=" + getId() +
            ", weekNumber=" + getWeekNumber() +
            ", year=" + getYear() +
            ", validation='" + isValidation() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}

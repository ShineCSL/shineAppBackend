package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LeaveConfig.
 */
@Entity
@Table(name = "leave_config")
public class LeaveConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nb_sick_leaves")
    private Integer nbSickLeaves;

    @Column(name = "nb_annual_leaves")
    private Integer nbAnnualLeaves;

    @Column(name = "nb_special_leaves")
    private Integer nbSpecialLeaves;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

    @Column(name = "date_modification")
    private ZonedDateTime dateModification;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User userCreation;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User userModification;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbSickLeaves() {
        return nbSickLeaves;
    }

    public LeaveConfig nbSickLeaves(Integer nbSickLeaves) {
        this.nbSickLeaves = nbSickLeaves;
        return this;
    }

    public void setNbSickLeaves(Integer nbSickLeaves) {
        this.nbSickLeaves = nbSickLeaves;
    }

    public Integer getNbAnnualLeaves() {
        return nbAnnualLeaves;
    }

    public LeaveConfig nbAnnualLeaves(Integer nbAnnualLeaves) {
        this.nbAnnualLeaves = nbAnnualLeaves;
        return this;
    }

    public void setNbAnnualLeaves(Integer nbAnnualLeaves) {
        this.nbAnnualLeaves = nbAnnualLeaves;
    }

    public Integer getNbSpecialLeaves() {
        return nbSpecialLeaves;
    }

    public LeaveConfig nbSpecialLeaves(Integer nbSpecialLeaves) {
        this.nbSpecialLeaves = nbSpecialLeaves;
        return this;
    }

    public void setNbSpecialLeaves(Integer nbSpecialLeaves) {
        this.nbSpecialLeaves = nbSpecialLeaves;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public LeaveConfig dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public LeaveConfig dateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public User getUser() {
        return user;
    }

    public LeaveConfig user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserCreation() {
        return userCreation;
    }

    public LeaveConfig userCreation(User user) {
        this.userCreation = user;
        return this;
    }

    public void setUserCreation(User user) {
        this.userCreation = user;
    }

    public User getUserModification() {
        return userModification;
    }

    public LeaveConfig userModification(User user) {
        this.userModification = user;
        return this;
    }

    public void setUserModification(User user) {
        this.userModification = user;
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
        LeaveConfig leaveConfig = (LeaveConfig) o;
        if (leaveConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaveConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeaveConfig{" +
            "id=" + getId() +
            ", nbSickLeaves=" + getNbSickLeaves() +
            ", nbAnnualLeaves=" + getNbAnnualLeaves() +
            ", nbSpecialLeaves=" + getNbSpecialLeaves() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}

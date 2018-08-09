package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User approver;

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

    public User getApprover() {
        return approver;
    }

    public LeaveConfig approver(User user) {
        this.approver = user;
        return this;
    }

    public void setApprover(User user) {
        this.approver = user;
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
            "}";
    }
}

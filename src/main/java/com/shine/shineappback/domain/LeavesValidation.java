package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LeavesValidation.
 */
@Entity
@Table(name = "leaves_validation")
public class LeavesValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "validated")
    private Boolean validated;

    @Column(name = "date_modification")
    private ZonedDateTime dateModification;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

    @OneToOne(mappedBy = "leavesValidation")
    @JsonIgnore
    private Leaves leaves;

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

    public Boolean isValidated() {
        return validated;
    }

    public LeavesValidation validated(Boolean validated) {
        this.validated = validated;
        return this;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public LeavesValidation dateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public LeavesValidation dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Leaves getLeaves() {
        return leaves;
    }

    public LeavesValidation leaves(Leaves leaves) {
        this.leaves = leaves;
        return this;
    }

    public void setLeaves(Leaves leaves) {
        this.leaves = leaves;
    }

    public User getUserModification() {
        return userModification;
    }

    public LeavesValidation userModification(User user) {
        this.userModification = user;
        return this;
    }

    public void setUserModification(User user) {
        this.userModification = user;
    }

    public User getUserCreation() {
        return userCreation;
    }

    public LeavesValidation userCreation(User user) {
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
        LeavesValidation leavesValidation = (LeavesValidation) o;
        if (leavesValidation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesValidation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesValidation{" +
            "id=" + getId() +
            ", validated='" + isValidated() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}

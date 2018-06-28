package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LeavesSubmission.
 */
@Entity
@Table(name = "leaves_submission")
public class LeavesSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submitted")
    private Boolean submitted;

    @Column(name = "date_modification")
    private ZonedDateTime dateModification;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User userModification;

    @OneToOne(mappedBy = "leavesSubmission")
    @JsonIgnore
    private Leaves leaves;

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

    public Boolean isSubmitted() {
        return submitted;
    }

    public LeavesSubmission submitted(Boolean submitted) {
        this.submitted = submitted;
        return this;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public LeavesSubmission dateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public LeavesSubmission dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public User getUserModification() {
        return userModification;
    }

    public LeavesSubmission userModification(User user) {
        this.userModification = user;
        return this;
    }

    public void setUserModification(User user) {
        this.userModification = user;
    }

    public Leaves getLeaves() {
        return leaves;
    }

    public LeavesSubmission leaves(Leaves leaves) {
        this.leaves = leaves;
        return this;
    }

    public void setLeaves(Leaves leaves) {
        this.leaves = leaves;
    }

    public User getUserCreation() {
        return userCreation;
    }

    public LeavesSubmission userCreation(User user) {
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
        LeavesSubmission leavesSubmission = (LeavesSubmission) o;
        if (leavesSubmission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesSubmission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesSubmission{" +
            "id=" + getId() +
            ", submitted='" + isSubmitted() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}

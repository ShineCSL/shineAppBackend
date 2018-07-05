package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A LeavesValidation.
 */
@Entity
@Table(name = "leaves_validation")
public class LeavesValidation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "validated")
    private Boolean validated;

    @NotNull
    @Column(name = "leaves_date", nullable = false)
    private LocalDate leavesDate;

    @OneToOne(mappedBy = "leavesValidation")
    @JsonIgnore
    private Leaves leaves;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

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

    public LocalDate getLeavesDate() {
        return leavesDate;
    }

    public LeavesValidation leavesDate(LocalDate leavesDate) {
        this.leavesDate = leavesDate;
        return this;
    }

    public void setLeavesDate(LocalDate leavesDate) {
        this.leavesDate = leavesDate;
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

    public User getUser() {
        return user;
    }

    public LeavesValidation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
            ", leavesDate='" + getLeavesDate() + "'" +
            "}";
    }
}

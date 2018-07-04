package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A LeavesRejection.
 */
@Entity
@Table(name = "leaves_rejection")
public class LeavesRejection extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rejected")
    private Boolean rejected;

    @OneToOne(mappedBy = "leavesRejection")
    @JsonIgnore
    private Leaves leaves;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isRejected() {
        return rejected;
    }

    public LeavesRejection rejected(Boolean rejected) {
        this.rejected = rejected;
        return this;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public Leaves getLeaves() {
        return leaves;
    }

    public LeavesRejection leaves(Leaves leaves) {
        this.leaves = leaves;
        return this;
    }

    public void setLeaves(Leaves leaves) {
        this.leaves = leaves;
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
        LeavesRejection leavesRejection = (LeavesRejection) o;
        if (leavesRejection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leavesRejection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeavesRejection{" +
            "id=" + getId() +
            ", rejected='" + isRejected() + "'" +
            "}";
    }
}

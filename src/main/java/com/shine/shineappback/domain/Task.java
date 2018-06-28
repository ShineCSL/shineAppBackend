package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_leave")
    private Boolean leave;

    @Column(name = "date_modification")
    private ZonedDateTime dateModification;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()[\\\\]+-_*/%]*$")
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "label_en")
    private String labelEn;

    @Column(name = "label_fr")
    private String labelFr;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private ZonedDateTime dateCreation;

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

    public Boolean isLeave() {
        return leave;
    }

    public Task leave(Boolean leave) {
        this.leave = leave;
        return this;
    }

    public void setLeave(Boolean leave) {
        this.leave = leave;
    }

    public ZonedDateTime getDateModification() {
        return dateModification;
    }

    public Task dateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(ZonedDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public String getCode() {
        return code;
    }

    public Task code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public Task labelEn(String labelEn) {
        this.labelEn = labelEn;
        return this;
    }

    public void setLabelEn(String labelEn) {
        this.labelEn = labelEn;
    }

    public String getLabelFr() {
        return labelFr;
    }

    public Task labelFr(String labelFr) {
        this.labelFr = labelFr;
        return this;
    }

    public void setLabelFr(String labelFr) {
        this.labelFr = labelFr;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public Task dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public User getUserCreation() {
        return userCreation;
    }

    public Task userCreation(User user) {
        this.userCreation = user;
        return this;
    }

    public void setUserCreation(User user) {
        this.userCreation = user;
    }

    public User getUserModification() {
        return userModification;
    }

    public Task userModification(User user) {
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
        Task task = (Task) o;
        if (task.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", leave='" + isLeave() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", code='" + getCode() + "'" +
            ", labelEn='" + getLabelEn() + "'" +
            ", labelFr='" + getLabelFr() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}

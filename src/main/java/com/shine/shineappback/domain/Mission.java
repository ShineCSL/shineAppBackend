package com.shine.shineappback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Mission.
 */
@Entity
@Table(name = "mission")
public class Mission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_label")
    private String label;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()\\[\\]#$+*%\\-_/\\\\]*$")
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Client client;

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

    public Mission label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public Mission code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Client getClient() {
        return client;
    }

    public Mission client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
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
        Mission mission = (Mission) o;
        if (mission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mission{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}

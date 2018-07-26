package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Team entity.
 */
public class TeamDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()\\[\\]#$+*%\\-_/\\\\]*$")
    private String code;

    private String label;

    private Long supervisorId;

    private String supervisorLogin;

    private Set<UserDTO> resources = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long userId) {
        this.supervisorId = userId;
    }

    public String getSupervisorLogin() {
        return supervisorLogin;
    }

    public void setSupervisorLogin(String userLogin) {
        this.supervisorLogin = userLogin;
    }

    public Set<UserDTO> getResources() {
        return resources;
    }

    public void setResources(Set<UserDTO> users) {
        this.resources = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;
        if (teamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", label='" + getLabel() + "'" +
            ", supervisor=" + getSupervisorId() +
            ", supervisor='" + getSupervisorLogin() + "'" +
            "}";
    }
}

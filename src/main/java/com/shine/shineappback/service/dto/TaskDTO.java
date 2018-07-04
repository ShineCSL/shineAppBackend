package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Task entity.
 */
public class TaskDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Boolean leave;

    private String labelEn;

    private String labelFr;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9()\\[\\]#$+*%\\-_/\\\\]*$")
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isLeave() {
        return leave;
    }

    public void setLeave(Boolean leave) {
        this.leave = leave;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public void setLabelEn(String labelEn) {
        this.labelEn = labelEn;
    }

    public String getLabelFr() {
        return labelFr;
    }

    public void setLabelFr(String labelFr) {
        this.labelFr = labelFr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", leave='" + isLeave() + "'" +
            ", labelEn='" + getLabelEn() + "'" +
            ", labelFr='" + getLabelFr() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}

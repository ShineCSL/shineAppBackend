package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LeaveConfig entity.
 */
public class LeaveConfigDTO implements Serializable {

    private Long id;

    private Integer nbSickLeaves;

    private Integer nbAnnualLeaves;

    private Integer nbSpecialLeaves;

    private Long userId;

    private String userLogin;

    private Long approverId;

    private String approverLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbSickLeaves() {
        return nbSickLeaves;
    }

    public void setNbSickLeaves(Integer nbSickLeaves) {
        this.nbSickLeaves = nbSickLeaves;
    }

    public Integer getNbAnnualLeaves() {
        return nbAnnualLeaves;
    }

    public void setNbAnnualLeaves(Integer nbAnnualLeaves) {
        this.nbAnnualLeaves = nbAnnualLeaves;
    }

    public Integer getNbSpecialLeaves() {
        return nbSpecialLeaves;
    }

    public void setNbSpecialLeaves(Integer nbSpecialLeaves) {
        this.nbSpecialLeaves = nbSpecialLeaves;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long userId) {
        this.approverId = userId;
    }

    public String getApproverLogin() {
        return approverLogin;
    }

    public void setApproverLogin(String userLogin) {
        this.approverLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LeaveConfigDTO leaveConfigDTO = (LeaveConfigDTO) o;
        if (leaveConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaveConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeaveConfigDTO{" +
            "id=" + getId() +
            ", nbSickLeaves=" + getNbSickLeaves() +
            ", nbAnnualLeaves=" + getNbAnnualLeaves() +
            ", nbSpecialLeaves=" + getNbSpecialLeaves() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", approver=" + getApproverId() +
            ", approver='" + getApproverLogin() + "'" +
            "}";
    }
}

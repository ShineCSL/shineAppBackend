package com.shine.shineappback.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InvoiceConfig entity.
 */
public class InvoiceConfigDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

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

        InvoiceConfigDTO invoiceConfigDTO = (InvoiceConfigDTO) o;
        if (invoiceConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceConfigDTO{" +
            "id=" + getId() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", approver=" + getApproverId() +
            ", approver='" + getApproverLogin() + "'" +
            "}";
    }
}

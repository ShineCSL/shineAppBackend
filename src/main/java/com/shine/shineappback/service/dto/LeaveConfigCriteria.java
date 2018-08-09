package com.shine.shineappback.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the LeaveConfig entity. This class is used in LeaveConfigResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /leave-configs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaveConfigCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter nbSickLeaves;

    private IntegerFilter nbAnnualLeaves;

    private IntegerFilter nbSpecialLeaves;

    private LongFilter userId;

    private LongFilter approverId;

    public LeaveConfigCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNbSickLeaves() {
        return nbSickLeaves;
    }

    public void setNbSickLeaves(IntegerFilter nbSickLeaves) {
        this.nbSickLeaves = nbSickLeaves;
    }

    public IntegerFilter getNbAnnualLeaves() {
        return nbAnnualLeaves;
    }

    public void setNbAnnualLeaves(IntegerFilter nbAnnualLeaves) {
        this.nbAnnualLeaves = nbAnnualLeaves;
    }

    public IntegerFilter getNbSpecialLeaves() {
        return nbSpecialLeaves;
    }

    public void setNbSpecialLeaves(IntegerFilter nbSpecialLeaves) {
        this.nbSpecialLeaves = nbSpecialLeaves;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getApproverId() {
        return approverId;
    }

    public void setApproverId(LongFilter approverId) {
        this.approverId = approverId;
    }

    @Override
    public String toString() {
        return "LeaveConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nbSickLeaves != null ? "nbSickLeaves=" + nbSickLeaves + ", " : "") +
                (nbAnnualLeaves != null ? "nbAnnualLeaves=" + nbAnnualLeaves + ", " : "") +
                (nbSpecialLeaves != null ? "nbSpecialLeaves=" + nbSpecialLeaves + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (approverId != null ? "approverId=" + approverId + ", " : "") +
            "}";
    }

}

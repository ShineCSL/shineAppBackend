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
 * Criteria class for the Team entity. This class is used in TeamResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /teams?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter code;

    private StringFilter label;

    private LongFilter supervisorId;

    private LongFilter resourcesId;

    public TeamCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public LongFilter getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(LongFilter supervisorId) {
        this.supervisorId = supervisorId;
    }

    public LongFilter getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(LongFilter resourcesId) {
        this.resourcesId = resourcesId;
    }

    @Override
    public String toString() {
        return "TeamCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (supervisorId != null ? "supervisorId=" + supervisorId + ", " : "") +
                (resourcesId != null ? "resourcesId=" + resourcesId + ", " : "") +
            "}";
    }

}

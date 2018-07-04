package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceSubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceSubmission and its DTO InvoiceSubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InvoiceSubmissionMapper extends EntityMapper<InvoiceSubmissionDTO, InvoiceSubmission> {


    @Mapping(target = "invoice", ignore = true)
    InvoiceSubmission toEntity(InvoiceSubmissionDTO invoiceSubmissionDTO);

    default InvoiceSubmission fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceSubmission invoiceSubmission = new InvoiceSubmission();
        invoiceSubmission.setId(id);
        return invoiceSubmission;
    }
}

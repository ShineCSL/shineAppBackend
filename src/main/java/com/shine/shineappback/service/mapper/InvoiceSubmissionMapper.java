package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceSubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceSubmission and its DTO InvoiceSubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InvoiceSubmissionMapper extends EntityMapper<InvoiceSubmissionDTO, InvoiceSubmission> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    InvoiceSubmissionDTO toDto(InvoiceSubmission invoiceSubmission);

    @Mapping(target = "invoice", ignore = true)
    @Mapping(source = "userId", target = "user")
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

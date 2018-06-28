package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceSubmissionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceSubmission and its DTO InvoiceSubmissionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InvoiceSubmissionMapper extends EntityMapper<InvoiceSubmissionDTO, InvoiceSubmission> {

    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    InvoiceSubmissionDTO toDto(InvoiceSubmission invoiceSubmission);

    @Mapping(target = "invoice", ignore = true)
    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
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

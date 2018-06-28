package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceRejectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceRejection and its DTO InvoiceRejectionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InvoiceRejectionMapper extends EntityMapper<InvoiceRejectionDTO, InvoiceRejection> {

    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    InvoiceRejectionDTO toDto(InvoiceRejection invoiceRejection);

    @Mapping(target = "invoice", ignore = true)
    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
    InvoiceRejection toEntity(InvoiceRejectionDTO invoiceRejectionDTO);

    default InvoiceRejection fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceRejection invoiceRejection = new InvoiceRejection();
        invoiceRejection.setId(id);
        return invoiceRejection;
    }
}

package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceValidation and its DTO InvoiceValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InvoiceValidationMapper extends EntityMapper<InvoiceValidationDTO, InvoiceValidation> {

    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    InvoiceValidationDTO toDto(InvoiceValidation invoiceValidation);

    @Mapping(target = "invoice", ignore = true)
    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
    InvoiceValidation toEntity(InvoiceValidationDTO invoiceValidationDTO);

    default InvoiceValidation fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceValidation invoiceValidation = new InvoiceValidation();
        invoiceValidation.setId(id);
        return invoiceValidation;
    }
}

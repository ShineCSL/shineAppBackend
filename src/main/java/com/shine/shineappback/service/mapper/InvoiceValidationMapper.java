package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceValidationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceValidation and its DTO InvoiceValidationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InvoiceValidationMapper extends EntityMapper<InvoiceValidationDTO, InvoiceValidation> {


    @Mapping(target = "invoice", ignore = true)
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

package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Invoice and its DTO InvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {TypeInvoiceMapper.class, InvoiceSubmissionMapper.class, InvoiceValidationMapper.class, InvoiceRejectionMapper.class, CurrencyMapper.class, UserMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mapping(source = "typeInvoice.id", target = "typeInvoiceId")
    @Mapping(source = "invoiceSubmission.id", target = "invoiceSubmissionId")
    @Mapping(source = "invoiceValidation.id", target = "invoiceValidationId")
    @Mapping(source = "invoiceRejection.id", target = "invoiceRejectionId")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.code", target = "currencyCode")
    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "typeInvoiceId", target = "typeInvoice")
    @Mapping(source = "invoiceSubmissionId", target = "invoiceSubmission")
    @Mapping(source = "invoiceValidationId", target = "invoiceValidation")
    @Mapping(source = "invoiceRejectionId", target = "invoiceRejection")
    @Mapping(source = "currencyId", target = "currency")
    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(id);
        return invoice;
    }
}

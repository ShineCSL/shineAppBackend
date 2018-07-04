package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Invoice and its DTO InvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {CurrencyMapper.class, InvoiceRejectionMapper.class, InvoiceSubmissionMapper.class, InvoiceValidationMapper.class, TypeInvoiceMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.code", target = "currencyCode")
    @Mapping(source = "invoiceRejection.id", target = "invoiceRejectionId")
    @Mapping(source = "invoiceRejection.rejected", target = "invoiceRejectionRejected")
    @Mapping(source = "invoiceSubmission.id", target = "invoiceSubmissionId")
    @Mapping(source = "invoiceSubmission.submitted", target = "invoiceSubmissionSubmitted")
    @Mapping(source = "invoiceValidation.id", target = "invoiceValidationId")
    @Mapping(source = "invoiceValidation.validated", target = "invoiceValidationValidated")
    @Mapping(source = "typeInvoice.id", target = "typeInvoiceId")
    @Mapping(source = "typeInvoice.code", target = "typeInvoiceCode")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "currencyId", target = "currency")
    @Mapping(source = "invoiceRejectionId", target = "invoiceRejection")
    @Mapping(source = "invoiceSubmissionId", target = "invoiceSubmission")
    @Mapping(source = "invoiceValidationId", target = "invoiceValidation")
    @Mapping(source = "typeInvoiceId", target = "typeInvoice")
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

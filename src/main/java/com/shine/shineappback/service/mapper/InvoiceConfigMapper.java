package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.InvoiceConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceConfig and its DTO InvoiceConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InvoiceConfigMapper extends EntityMapper<InvoiceConfigDTO, InvoiceConfig> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "approver.id", target = "approverId")
    @Mapping(source = "approver.login", target = "approverLogin")
    InvoiceConfigDTO toDto(InvoiceConfig invoiceConfig);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "approverId", target = "approver")
    InvoiceConfig toEntity(InvoiceConfigDTO invoiceConfigDTO);

    default InvoiceConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceConfig invoiceConfig = new InvoiceConfig();
        invoiceConfig.setId(id);
        return invoiceConfig;
    }
}

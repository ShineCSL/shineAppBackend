package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.AccountDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountDetails and its DTO AccountDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class, InvoiceMapper.class, CurrencyMapper.class})
public interface AccountDetailsMapper extends EntityMapper<AccountDetailsDTO, AccountDetails> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModfication.id", target = "userModficationId")
    @Mapping(source = "userModfication.login", target = "userModficationLogin")
    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "invoice.label", target = "invoiceLabel")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.code", target = "currencyCode")
    AccountDetailsDTO toDto(AccountDetails accountDetails);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModficationId", target = "userModfication")
    @Mapping(source = "invoiceId", target = "invoice")
    @Mapping(source = "currencyId", target = "currency")
    AccountDetails toEntity(AccountDetailsDTO accountDetailsDTO);

    default AccountDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setId(id);
        return accountDetails;
    }
}

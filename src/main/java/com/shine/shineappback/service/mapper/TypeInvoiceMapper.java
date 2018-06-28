package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.TypeInvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TypeInvoice and its DTO TypeInvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TypeInvoiceMapper extends EntityMapper<TypeInvoiceDTO, TypeInvoice> {

    @Mapping(source = "userCreation.id", target = "userCreationId")
    @Mapping(source = "userCreation.login", target = "userCreationLogin")
    @Mapping(source = "userModification.id", target = "userModificationId")
    @Mapping(source = "userModification.login", target = "userModificationLogin")
    TypeInvoiceDTO toDto(TypeInvoice typeInvoice);

    @Mapping(source = "userCreationId", target = "userCreation")
    @Mapping(source = "userModificationId", target = "userModification")
    TypeInvoice toEntity(TypeInvoiceDTO typeInvoiceDTO);

    default TypeInvoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeInvoice typeInvoice = new TypeInvoice();
        typeInvoice.setId(id);
        return typeInvoice;
    }
}

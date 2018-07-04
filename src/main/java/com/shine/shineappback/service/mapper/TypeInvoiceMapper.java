package com.shine.shineappback.service.mapper;

import com.shine.shineappback.domain.*;
import com.shine.shineappback.service.dto.TypeInvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TypeInvoice and its DTO TypeInvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeInvoiceMapper extends EntityMapper<TypeInvoiceDTO, TypeInvoice> {



    default TypeInvoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeInvoice typeInvoice = new TypeInvoice();
        typeInvoice.setId(id);
        return typeInvoice;
    }
}

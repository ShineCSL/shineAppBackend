package com.shine.shineappback.repository;

import com.shine.shineappback.domain.TypeInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TypeInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeInvoiceRepository extends JpaRepository<TypeInvoice, Long> {

    @Query("select type_invoice from TypeInvoice type_invoice where type_invoice.userCreation.login = ?#{principal.username}")
    List<TypeInvoice> findByUserCreationIsCurrentUser();

    @Query("select type_invoice from TypeInvoice type_invoice where type_invoice.userModification.login = ?#{principal.username}")
    List<TypeInvoice> findByUserModificationIsCurrentUser();

}

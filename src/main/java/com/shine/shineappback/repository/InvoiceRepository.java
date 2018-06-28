package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Invoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("select invoice from Invoice invoice where invoice.userCreation.login = ?#{principal.username}")
    List<Invoice> findByUserCreationIsCurrentUser();

    @Query("select invoice from Invoice invoice where invoice.userModification.login = ?#{principal.username}")
    List<Invoice> findByUserModificationIsCurrentUser();

}

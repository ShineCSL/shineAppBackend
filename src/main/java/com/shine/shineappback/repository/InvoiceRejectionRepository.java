package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceRejection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the InvoiceRejection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRejectionRepository extends JpaRepository<InvoiceRejection, Long> {

    @Query("select invoice_rejection from InvoiceRejection invoice_rejection where invoice_rejection.user.login = ?#{principal.username}")
    List<InvoiceRejection> findByUserIsCurrentUser();

}

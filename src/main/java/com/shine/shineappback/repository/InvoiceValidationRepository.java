package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceValidation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the InvoiceValidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceValidationRepository extends JpaRepository<InvoiceValidation, Long> {

    @Query("select invoice_validation from InvoiceValidation invoice_validation where invoice_validation.user.login = ?#{principal.username}")
    List<InvoiceValidation> findByUserIsCurrentUser();

}

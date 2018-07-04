package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceValidation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvoiceValidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceValidationRepository extends JpaRepository<InvoiceValidation, Long> {

}

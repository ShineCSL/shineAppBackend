package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceRejection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvoiceRejection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRejectionRepository extends JpaRepository<InvoiceRejection, Long> {

}

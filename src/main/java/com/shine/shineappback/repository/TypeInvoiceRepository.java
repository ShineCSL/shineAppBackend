package com.shine.shineappback.repository;

import com.shine.shineappback.domain.TypeInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeInvoiceRepository extends JpaRepository<TypeInvoice, Long> {

}

package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the InvoiceConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceConfigRepository extends JpaRepository<InvoiceConfig, Long> {

    @Query("select invoice_config from InvoiceConfig invoice_config where invoice_config.user.login = ?#{principal.username}")
    List<InvoiceConfig> findByUserIsCurrentUser();

    @Query("select invoice_config from InvoiceConfig invoice_config where invoice_config.approver.login = ?#{principal.username}")
    List<InvoiceConfig> findByApproverIsCurrentUser();

}

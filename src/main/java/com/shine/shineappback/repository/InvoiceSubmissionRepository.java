package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceSubmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the InvoiceSubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceSubmissionRepository extends JpaRepository<InvoiceSubmission, Long> {

    @Query("select invoice_submission from InvoiceSubmission invoice_submission where invoice_submission.user.login = ?#{principal.username}")
    List<InvoiceSubmission> findByUserIsCurrentUser();

}

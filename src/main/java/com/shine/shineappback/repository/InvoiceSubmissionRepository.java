package com.shine.shineappback.repository;

import com.shine.shineappback.domain.InvoiceSubmission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvoiceSubmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceSubmissionRepository extends JpaRepository<InvoiceSubmission, Long> {

}

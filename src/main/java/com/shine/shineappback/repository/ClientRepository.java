package com.shine.shineappback.repository;

import com.shine.shineappback.domain.Client;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select client from Client client where client.userCreation.login = ?#{principal.username}")
    List<Client> findByUserCreationIsCurrentUser();

    @Query("select client from Client client where client.userModification.login = ?#{principal.username}")
    List<Client> findByUserModificationIsCurrentUser();

}

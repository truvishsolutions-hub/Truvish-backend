package com.truvish.truvishbackend.corporateLogin;

import com.truvish.truvishbackend.corporateLogin.ClientLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientLoginRepository extends JpaRepository<ClientLogin, Long> {

    /**
     * Find login by email
     */
    Optional<ClientLogin> findByEmail(String email);

    /**
     * Check email already exists
     */
    boolean existsByEmail(String email);

    /**
     * Find by Client ID
     */
    Optional<ClientLogin> findByClientId(Long clientId);

    /**
     * Check client already has login
     */
    boolean existsByClientId(Long clientId);

}

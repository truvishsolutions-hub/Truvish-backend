package com.truvish.truvishbackend.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByMobileNumber(String mobileNumber);
    Optional<Client> findByMobileNumber(String mobileNumber);
}

package com.truvish.truvishbackend.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminConfigRepository extends JpaRepository<AdminConfig, Long> {

    Optional<AdminConfig> findTopByOrderByIdAsc();
}
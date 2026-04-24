package com.truvish.truvishbackend.DemoRequest.repository;


import com.truvish.truvishbackend.DemoRequest.entity.DemoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRequestRepository extends JpaRepository<DemoRequest, Long> {
}

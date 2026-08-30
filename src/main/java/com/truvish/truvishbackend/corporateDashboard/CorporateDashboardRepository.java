package com.truvish.truvishbackend.corporateDashboard;

import com.truvish.truvishbackend.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporateDashboardRepository
        extends JpaRepository<Client, Long> {

}
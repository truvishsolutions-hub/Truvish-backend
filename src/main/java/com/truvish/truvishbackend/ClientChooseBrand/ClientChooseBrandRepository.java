package com.truvish.truvishbackend.ClientChooseBrand;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientChooseBrandRepository extends JpaRepository<ClientChooseBrand, Long> {

    List<ClientChooseBrand> findAllByOrderByBrandNameAsc();

    List<ClientChooseBrand> findByCategoryOrderByBrandNameAsc(String category);

    boolean existsByBrandNameIgnoreCase(String brandName);

    Optional<ClientChooseBrand> findByBrandNameIgnoreCase(String brandName);
}

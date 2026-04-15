package com.truvish.truvishbackend.redemption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRedemptionRepository extends JpaRepository<UserRedemption, Long> {

    List<UserRedemption> findByUserPhoneNumber(String phone);

    List<UserRedemption> findByUserPhoneNumberOrderByUserBrandTimeTempDesc(String phone);

    List<UserRedemption> findByUserTruvishCodeOrderByUserBrandTimeTempDesc(String code);

    List<UserRedemption> findByUserPhoneNumberOrUserTruvishCodeOrderByUserBrandTimeTempDesc(
            String phone,
            String code
    );

    Optional<UserRedemption> findTopByUserTruvishCodeOrderByUserBrandTimeTempDesc(String code);

    boolean existsByUserTruvishCodeAndUserPhoneNumber(String code, String phone);

    List<UserRedemption> findByUserTruvishCodeOrderByUserBrandTimeTempAsc(String code);

    long countByClientId(Long clientId);

    @Query("select coalesce(sum(u.userBrandValue), 0) from UserRedemption u where u.clientId = :clientId")
    Long sumRedeemedAmountByClientId(Long clientId);

    @Query("select count(distinct u.userPhoneNumber) from UserRedemption u")
    Long countDistinctUsers();

    @Query("select count(distinct u.userPhoneNumber) from UserRedemption u where u.clientId = :clientId")
    Long countDistinctUsersByClientId(Long clientId);
}
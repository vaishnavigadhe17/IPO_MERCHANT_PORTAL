package com.app.ipo.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.ipo.model.MerchantUpdateEntity;

@Repository
public interface MerchantUpdateRepository extends JpaRepository < MerchantUpdateEntity, Long >
{
	@Query(value = "SELECT * FROM merchant_update_details p WHERE p.Status IN(:status)", nativeQuery = true)
	public List<MerchantUpdateEntity> findAllMerchantUpdateByStatus(@Param("status") List<String> status);

	@Query(value = "SELECT COUNT(*) AS c FROM merchant_update_details p WHERE p.Status IN(:status) AND p.Merchant_Id =:merchantId GROUP BY p.Merchant_Id", nativeQuery = true)
	public Optional<BigInteger> findAllMerchantUpdateCount(@Param("status") List<String> status, @Param("merchantId") Long merchantId);

	@Query("SELECT m FROM MerchantUpdateEntity m WHERE m.merchantId =:merchantId AND m.status IN(:status)")
	public Optional<MerchantUpdateEntity> findMerchantUpdateByMerchantId(@Param("merchantId") Long merchantId, @Param("status") List<String> status);
	
	@Query(value = "SELECT * FROM merchant_update_details p WHERE p.Status IN(:status) AND p.Merchant_Id =:merchantId GROUP BY p.Merchant_Id", nativeQuery = true)
	public Optional<MerchantUpdateEntity> findAllMerchantUpdateStatus(@Param("status") List<String> status, @Param("merchantId") Long merchantId);
}
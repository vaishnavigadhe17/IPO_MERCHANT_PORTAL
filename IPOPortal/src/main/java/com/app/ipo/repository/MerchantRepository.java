package com.app.ipo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.ipo.model.MerchantEntity;

@Repository
public interface MerchantRepository extends JpaRepository < MerchantEntity, Long >
{
	@Query("SELECT m FROM MerchantEntity m WHERE m.vpa =:vpa")
	public Optional<MerchantEntity> findByVPA(@Param("vpa") String vpa);

	@Query("SELECT m FROM MerchantEntity m WHERE m.vpa =:vpa")
	public List<MerchantEntity> findMerchantByVPA(@Param("vpa") String vpa);

	@Query(value = "SELECT * FROM merchant_details m ORDER BY Merchant_Id DESC LIMIT :limit", nativeQuery = true)
	public List<MerchantEntity> findAllMerchant(@Param("limit") Integer limit);
	
	@Query(value = "SELECT * FROM merchant_details m WHERE m.Status =:status ORDER BY Merchant_Id DESC LIMIT :limit", nativeQuery = true)
	public List<MerchantEntity> findAllMerchantByStatus(@Param("status") String status, @Param("limit") Integer limit);

	@Query("SELECT m FROM MerchantEntity m WHERE m.merchantId =:merchantId")
	public Optional<MerchantEntity> findMerchantById(@Param("merchantId") Long merchantId);

	@Query(value = "SELECT * FROM merchant_details m WHERE m.Status IN(:status) ORDER BY Merchant_Id DESC LIMIT :limit", nativeQuery = true)
	public List<MerchantEntity> findAllMerchantByProcessedStatus(@Param("status") List<String> status, @Param("limit") Integer limit);
}
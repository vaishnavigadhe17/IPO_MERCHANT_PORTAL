package com.app.ipo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.ipo.model.UploadEntity;

@Repository
public interface UploadRepository extends JpaRepository < UploadEntity, Long >
{
	@Query(value = "SELECT * FROM upload_details m WHERE m.User_Id =:userId ORDER BY Upload_Id DESC LIMIT :limit", nativeQuery = true)
	public List<UploadEntity> findByUserID(@Param("userId") Long userId, @Param("limit") Integer limit);
	
}
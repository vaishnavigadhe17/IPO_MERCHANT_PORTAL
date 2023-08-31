package com.app.ipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.ipo.model.AdminEntity;

@Repository
public interface AdminRepository extends JpaRepository < AdminEntity, Long >
{
	@Query("SELECT a FROM AdminEntity a WHERE a.adminName =:adminName AND a.adminPassword =:adminPassword")
	public AdminEntity loginUser(@Param("adminName") String adminName, @Param("adminPassword") String adminPassword);
}

package com.app.ipo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.ipo.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository < UserEntity, Long >
{
	@Query("SELECT u FROM UserEntity u WHERE u.userPhoneno =:mobilenumber")
	public UserEntity findByMobileNo(@Param("mobilenumber") Long mobilenumber);

	@Query("SELECT u FROM UserEntity u WHERE u.userName =:userName AND u.userPassword =:userPassword")
	public UserEntity loginUser(@Param("userName") String userName, @Param("userPassword") String userPassword);

	@Query("SELECT u FROM UserEntity u WHERE u.userName =:userName")
	public UserEntity findByName(@Param("userName") String userName);
	
	@Query("SELECT u FROM UserEntity u WHERE u.categoryId IN (:categoryId)")
	public List<UserEntity> findUserByCategory(@Param("categoryId") List<Long> categoryId);

	@Query("SELECT u FROM UserEntity u WHERE u.userId =:userId")
	public Optional<UserEntity> findByUserId(@Param("userId") Long userId);

	@Query("SELECT u FROM UserEntity u WHERE u.userPhoneno =:userPhoneno AND u.userEmailId =:userEmailId")
	public Optional<UserEntity> findByPhonenoAndEmail(@Param("userPhoneno") Long userPhoneno, @Param("userEmailId") String userEmailId);

	@Query("SELECT u FROM UserEntity u WHERE u.userPhoneno =:userPhoneno")
	public Optional<UserEntity> findByPhoneno(@Param("userPhoneno") Long userPhoneno);

	@Query("SELECT u FROM UserEntity u WHERE u.userEmailId =:userEmailId")
	public Optional<UserEntity> findByEmailId(@Param("userEmailId") String userEmailId);
}

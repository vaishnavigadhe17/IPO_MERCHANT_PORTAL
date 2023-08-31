package com.app.ipo.dto;

import java.sql.Date;

public class UserDto 
{
	private Long userId;

	private String name;
		
	private Long userPhoneno;

	private String userEmailId;

	private String userAddress;

	private Long userLocation;

	private Long categoryId;
	
	private String categoryName;
	
	private String userName;

	private String userPassword;

	private Date recordedDate;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserPhoneno() {
		return userPhoneno;
	}

	public void setUserPhoneno(Long userPhoneno) {
		this.userPhoneno = userPhoneno;
	}

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public Long getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Long userLocation) {
		this.userLocation = userLocation;
	}
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Date getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
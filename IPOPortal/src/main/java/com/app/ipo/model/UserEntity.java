package com.app.ipo.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_details")
public class UserEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "User_Id")
	private Long userId;

	@Column(name = "Name")
	private String name;
	
	@Column(name = "Phoneno")
	private Long userPhoneno;

	@Column(name = "Email_Id")
	private String userEmailId;

	@Column(name = "Address")
	private String userAddress;

	@Column(name = "Location")
	private Long userLocation;

	@Column(name = "Category_Id")
	private Long categoryId;

	@Column(name = "Username")
	private String userName;
	
	@Column(name = "Password")
	private String userPassword;

	@Column(name = "Recorded_Date")
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
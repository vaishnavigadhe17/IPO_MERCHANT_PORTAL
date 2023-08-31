package com.app.ipo.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "merchant_details")
public class MerchantEntity 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Merchant_Id")
	private Long merchantId;

	@Column(name = "User_Id")
	private Long userId;
	
	@Column(name = "Upload_Id")
	private Long uploadId;

	@Column(name = "Name")
	private String name;
	
	@Column(name = "Marketing_Name")
	private String marketing_name;
	
	@Column(name = "Address")
	private String address;
	
	@Column(name = "City")
	private String city;
	
	@Column(name = "State")
	private String state;
		
	@Column(name = "Pincode")
	private String pincode;
	
	@Column(name = "Mobile_No")
	private String mobile_no;
	
	@Column(name = "Landline_No")
	private String landline_no;
	
	@Column(name = "Director")
	private String director;
	
	@Column(name = "Category")
	private String category;
	
	@Column(name = "MCC")
	private String mcc;
	
	@Column(name = "MCC_Desc")
	private String mcc_desc;
	
	@Column(name = "Type_Of_Integration")
	private String type_of_integration;
	
	@Column(name = "VPA")
	private String vpa;
	
	@Column(name = "GST_Number")
	private String gst_mumber;
	
	@Column(name = "PAN")
	private String pan;
	
	@Column(name = "Merchant_Official_Id")
	private String merchant_official_id;
	
	@Column(name = "Merchant_Website")
	private String merchant_website;
	
	@Column(name = "Bank_Name")
	private String bank_name;
	
	@Column(name = "IFSC_Code")
	private String ifsc_code;
	
	@Column(name = "Nodal_Account_No")
	private String nodal_account_no;
		
	@Column(name = "Pool_Account_No")
	private String pool_account_no;
	
	@Column(name = "Pool_IFSC_Code")
	private String pool_ifsc_code;
	
	@Column(name = "SYM")
	private String sym;
	
	@Column(name = "Status")
	private String status;
	
	@Transient
	private String updatedStatus;
	
	@Column(name = "Reason")
	private String reason;
	
	@Transient
	private Boolean recordStatus;
	
	@Column(name = "Recorded_Date")
	private Date recordedDate;
	
	@Transient
	private Long count;

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

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

	public String getMarketing_name() {
		return marketing_name;
	}

	public void setMarketing_name(String marketing_name) {
		this.marketing_name = marketing_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getLandline_no() {
		return landline_no;
	}

	public void setLandline_no(String landline_no) {
		this.landline_no = landline_no;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMcc_desc() {
		return mcc_desc;
	}

	public void setMcc_desc(String mcc_desc) {
		this.mcc_desc = mcc_desc;
	}

	public String getType_of_integration() {
		return type_of_integration;
	}

	public void setType_of_integration(String type_of_integration) {
		this.type_of_integration = type_of_integration;
	}

	public String getVpa() {
		return vpa;
	}

	public void setVpa(String vpa) {
		this.vpa = vpa;
	}

	public String getGst_mumber() {
		return gst_mumber;
	}

	public void setGst_mumber(String gst_mumber) {
		this.gst_mumber = gst_mumber;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getMerchant_official_id() {
		return merchant_official_id;
	}

	public void setMerchant_official_id(String merchant_official_id) {
		this.merchant_official_id = merchant_official_id;
	}

	public String getMerchant_website() {
		return merchant_website;
	}

	public void setMerchant_website(String merchant_website) {
		this.merchant_website = merchant_website;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getIfsc_code() {
		return ifsc_code;
	}

	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}

	public String getNodal_account_no() {
		return nodal_account_no;
	}

	public void setNodal_account_no(String nodal_account_no) {
		this.nodal_account_no = nodal_account_no;
	}

	public String getPool_account_no() {
		return pool_account_no;
	}

	public void setPool_account_no(String pool_account_no) {
		this.pool_account_no = pool_account_no;
	}

	public String getPool_ifsc_code() {
		return pool_ifsc_code;
	}

	public void setPool_ifsc_code(String pool_ifsc_code) {
		this.pool_ifsc_code = pool_ifsc_code;
	}

	public String getSym() {
		return sym;
	}

	public void setSym(String sym) {
		this.sym = sym;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}

	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getUpdatedStatus() {
		return updatedStatus;
	}

	public void setUpdatedStatus(String updatedStatus) {
		this.updatedStatus = updatedStatus;
	}
}
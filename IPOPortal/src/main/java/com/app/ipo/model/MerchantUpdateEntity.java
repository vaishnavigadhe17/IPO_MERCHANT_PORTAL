package com.app.ipo.model;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "merchant_update_details")
public class MerchantUpdateEntity 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Merchant_Update_Id")
	private Long merchantUpdateId;
	
	@Column(name = "Merchant_Id")
	private Long merchantId;
	
	@Column(name = "Requested_User_Id")
	private Long requestedUserId;
	
	@Column(name = "Reason")
	private String reason;
	
	@Column(name = "Approver_User_Id")
	private Long approverUserId;

	@Column(name = "Status")
	private String status;
	
	@Column(name = "Updated_Date")
	private Date updatedDate;
	
	@Column(name = "Recorded_Date")
	private Date recordedDate;

	public Long getMerchantUpdateId() {
		return merchantUpdateId;
	}

	public void setMerchantUpdateId(Long merchantUpdateId) {
		this.merchantUpdateId = merchantUpdateId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getRequestedUserId() {
		return requestedUserId;
	}

	public void setRequestedUserId(Long requestedUserId) {
		this.requestedUserId = requestedUserId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getApproverUserId() {
		return approverUserId;
	}

	public void setApproverUserId(Long approverUserId) {
		this.approverUserId = approverUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}
}
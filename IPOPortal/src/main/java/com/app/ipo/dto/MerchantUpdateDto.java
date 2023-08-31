package com.app.ipo.dto;

import java.sql.Date;

public class MerchantUpdateDto 
{
	private Long merchantUpdateId;
	private Long merchantId;
	private Long requestedUserId;
	private String reason;
	private Long approverUserId;
	private String status;
	private Date updatedDate;
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
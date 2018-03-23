package com.crawl.dto;

public class crawlingRatioVO {
	private String site;
	private int success;
	private int error;
	private int successAcc;
	private int errorAcc;
	private String dt;
	
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getSuccessAcc() {
		return successAcc;
	}
	public void setSuccessAcc(int successAcc) {
		this.successAcc = successAcc;
	}
	public int getErrorAcc() {
		return errorAcc;
	}
	public void setErrorAcc(int errorAcc) {
		this.errorAcc = errorAcc;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}

}

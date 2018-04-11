package com.crawl.dto;

public class errorRatioVO {
	private String site;
	private int success;
	private int error;
	
	
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

}

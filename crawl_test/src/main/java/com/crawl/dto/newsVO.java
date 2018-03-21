package com.crawl.dto;

public class newsVO {
	private String site;
	private String title;
	private String enrollDT;
	private String newsURL;
	private String saveDT;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEnrollDT() {
		return enrollDT;
	}
	public void setEnrollDT(String enrollDT) {
		this.enrollDT = enrollDT;
	}
	public String getNewsURL() {
		return newsURL;
	}
	public void setNewsURL(String newsURL) {
		this.newsURL = newsURL;
	}
	public String getInsertDbDt() {
		return saveDT;
	}
	public void setInsertDbDt(String saveDT) {
		this.saveDT = saveDT;
	}
}

package com.crawl.dto;

public class searchNewsHistoryVO {
	private String site;
	private String hour;
	private int newDataSum;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public int getNewDataSum() {
		return newDataSum;
	}
	public void setNewDataSum(int newDataSum) {
		this.newDataSum = newDataSum;
	}
}

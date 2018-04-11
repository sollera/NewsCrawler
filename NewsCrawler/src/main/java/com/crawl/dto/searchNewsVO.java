package com.crawl.dto;

public class searchNewsVO {
	private String site;
	private int target;
	private int newData;
	private int allData;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public int getNewData() {
		return newData;
	}
	public void setNewData(int newData) {
		this.newData = newData;
	}
	public int getAllData() {
		return allData;
	}
	public void setAllData(int allData) {
		this.allData = allData;
	}
}

package com.crawl.dto;

public class updateCntVO {
	private String site;
	private int cnt;
	private String updateTime;
	private int allCnt;
	
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getAllCnt() {
		return allCnt;
	}
	public void setAllCnt(int allCnt) {
		this.allCnt = allCnt;
	}
}

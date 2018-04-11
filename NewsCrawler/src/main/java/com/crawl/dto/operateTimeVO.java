package com.crawl.dto;

public class operateTimeVO {
	private String site;
	private String startTime;
	private String endTime;
	private int spendTime;
	private int sumTime;
	private int cnt;

	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getSpendTime() {
		return spendTime;
	}
	public void setSpendTime(int spendTime) {
		this.spendTime = spendTime;
	}
	public int getSumTime() {
		return sumTime;
	}
	public void setSumTime(int sumTime) {
		this.sumTime = sumTime;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
}

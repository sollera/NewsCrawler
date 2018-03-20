package com.crawl.dao;

import java.util.List;

import com.crawl.dto.newsVO;
import com.crawl.dto.statusVO;

public interface NewsDAO {
	public List<newsVO> selectNews(int first);	//뉴스 정보 불러오기
	public List<statusVO> selectStatus();	//뉴스 갱신 상태 확인
	public void updateStatus();	//뉴스 갱신
	public int newsCnt(String site);	//뉴스 수 세기
}

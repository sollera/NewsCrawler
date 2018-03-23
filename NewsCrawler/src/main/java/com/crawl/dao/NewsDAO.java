package com.crawl.dao;

import java.util.List;

import com.crawl.dto.crawlingRatioVO;
import com.crawl.dto.errLogVO;
import com.crawl.dto.newsVO;
import com.crawl.dto.numberOfCasesVO;
import com.crawl.dto.statusVO;

public interface NewsDAO {
	public List<newsVO> selectNews(int first);	//뉴스 정보 불러오기
	public statusVO selectStatus();	//뉴스 갱신 상태 확인
	public List<numberOfCasesVO> newsCnt(String col);	//조건부 뉴스 수 세기
	public int totalNewsCnt();	//전체 기사 수 세기(페이징)
	public List<crawlingRatioVO> selectErrCnt();	//크롤링 성공실패 세기
	public List<errLogVO> selectLog();
}

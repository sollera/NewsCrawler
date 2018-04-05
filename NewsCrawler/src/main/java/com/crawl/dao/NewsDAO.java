package com.crawl.dao;

import java.util.List;

import com.crawl.dto.newsVO;
import com.crawl.dto.numberOfCasesVO;

public interface NewsDAO {
	
	//뉴스 페이지
	public List<newsVO> selectNews(int first);	//뉴스 정보 불러오기
	public int totalNewsCnt();	//전체 기사 수 세기(페이징)
	public List<numberOfCasesVO> newsCnt(String col);	//hot랭킹
	//필터O,검색X 뉴스 정보,뉴스 수
	public List<newsVO> selectNews1(String site,String type,int first);
	public List<newsVO> selectNews2(String site,int first);
	public List<newsVO> selectNews3(String type,int first);
	public int totalNewsCnt1(String site,String type);
	public int totalNewsCnt2(String site);
	public int totalNewsCnt3(String type);
	//필터X,검색O
	public List<newsVO> searchNews(String keyword,int first);
	public int searchCnt(String keyword);
	//필터O,검색O
	public List<newsVO> allConditionNews1(String site,String type,String keyword,int first);
	public List<newsVO> allConditionNews2(String site,String keyword,int first);
	public List<newsVO> allConditionNews3(String type,String keyword,int first);
	public int allConditionCnt1(String site,String type,String keyword);
	public int allConditionCnt2(String site,String keyword);
	public int allConditionCnt3(String type,String keyword);
	
}

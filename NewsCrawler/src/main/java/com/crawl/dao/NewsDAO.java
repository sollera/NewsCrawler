package com.crawl.dao;

import java.util.List;

import com.crawl.dto.errorRatioVO;
import com.crawl.dto.errLogVO;
import com.crawl.dto.newsVO;
import com.crawl.dto.numberOfCasesVO;
import com.crawl.dto.operateTimeVO;
import com.crawl.dto.searchNewsHistoryVO;
import com.crawl.dto.searchNewsVO;
import com.crawl.dto.statusVO;
import com.crawl.dto.updateCntVO;

public interface NewsDAO {
	//뉴스 페이지
	public List<newsVO> selectNews(int first);	//뉴스 정보 불러오기
	public statusVO selectStatus();	//뉴스 갱신 상태 확인
	public List<numberOfCasesVO> newsCnt(String col);	//조건부 뉴스 수 세기
	public int totalNewsCnt();	//전체 기사 수 세기(페이징)
	public List<errorRatioVO> selectErrCnt();	//크롤링 성공실패 세기
	public List<errLogVO> selectLog();
	public List<updateCntVO> updateLog();
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
	
	public int powerChk();
	public List<operateTimeVO> operatingTime();
	public List<searchNewsVO> searchNewsCnt();
	
	public List<searchNewsHistoryVO> searchNewsHistory();
	public List<errorRatioVO> errorHistory();
}

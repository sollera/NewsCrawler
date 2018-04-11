package com.crawl.dao;

import java.util.List;

import com.crawl.dto.errLogVO;
import com.crawl.dto.errorRatioVO;
import com.crawl.dto.operateTimeVO;
import com.crawl.dto.searchNewsHistoryVO;
import com.crawl.dto.searchNewsVO;
import com.crawl.dto.statusVO;

public interface StatusDAO {
	
	//관리 페이지 - instant
	public int powerChk();
	public statusVO selectStatus();	//뉴스 갱신 상태 확인
	public List<operateTimeVO> operatingTime();	//걸린 시간
	public List<searchNewsVO> searchNewsCnt();	//새로 수집한 기사 수
	public List<errLogVO> selectLog();	//에러 로그
	
	//관리페이지 - history
	public List<searchNewsHistoryVO> searchNewsHistory();	//새로 수집 기사 수
	public List<errorRatioVO> errorHistory();	//에러 발생율
	
}

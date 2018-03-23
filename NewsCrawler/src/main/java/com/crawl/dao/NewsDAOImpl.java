package com.crawl.dao;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.crawl.dto.crawlingRatioVO;
import com.crawl.dto.errLogVO;
import com.crawl.dto.newsVO;
import com.crawl.dto.numberOfCasesVO;
import com.crawl.dto.statusVO;

@Repository
public class NewsDAOImpl implements NewsDAO {
	@Inject
	private SqlSession sqlSession;
	
	private static final String Namespace = "com.news.mapper.newsMapper";
	
	@Override
	public List<newsVO> selectNews(int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);
		
		Map<String,Object> selectCondition = new HashMap<String,Object>();
		selectCondition.put("firstNews", first);
		selectCondition.put("today",date);
		
		return sqlSession.selectList(Namespace+".newsList",selectCondition);	//뉴스 보기
	}

	@Override
	public statusVO selectStatus() {
		return sqlSession.selectOne(Namespace+".statusList");	//크롤링 상태
	}

	@Override
	public List<numberOfCasesVO> newsCnt(String col) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String todate = dayTimeForURL.format(time);
		
		if(col.equals("site")) return sqlSession.selectList(Namespace+".cntPerSite",todate);	//경우 별 기사 수(hot / 새로 올라온 기사 수)
		else if(col.equals("type"))return sqlSession.selectList(Namespace+".cntPerType",todate);	//경우 별 기사 수(hot / 새로 올라온 기사 수)
		else return null;
	}

	@Override
	public int totalNewsCnt() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		return sqlSession.selectOne(Namespace+".newsCnt",today);	//당일 기사 수(페이징)
	}

	@Override
	public List<crawlingRatioVO> selectErrCnt() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		return sqlSession.selectList(Namespace+".errCnt",today);	//사이트 별 크롤링 성공/실패 개수 + 누적
	}

	@Override
	public List<errLogVO> selectLog() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		return sqlSession.selectList(Namespace+".errLog",today);
	}
	
}

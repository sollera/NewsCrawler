package com.crawl.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.crawl.dto.updateCntVO;

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

	@Override
	public List<newsVO> selectNews1(String site,String type,int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstNews", first);
		map.put("today",date);
		map.put("site", site);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectList(Namespace+".specificNews3",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectList(Namespace+".specificNews5",map);
		}
	}

	@Override
	public List<newsVO> selectNews2(String site,int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);
				
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstNews", first);
		map.put("today",date);
		map.put("site", site);
		return sqlSession.selectList(Namespace+".specificNews1",map);
	}

	@Override
	public List<newsVO> selectNews3(String type,int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstNews", first);
		map.put("today",date);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectList(Namespace+".specificNews2",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectList(Namespace+".specificNews4",map);
		}
	}

	@Override
	public int totalNewsCnt1(String site, String type) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		map.put("site", site);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectOne(Namespace+".newsCnt3",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectOne(Namespace+".newsCnt5",map);
		}
	}

	@Override
	public int totalNewsCnt2(String site) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		map.put("site", site);
		
		return sqlSession.selectOne(Namespace+".newsCnt1",map);
	}

	@Override
	public int totalNewsCnt3(String type) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectOne(Namespace+".newsCnt2",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectOne(Namespace+".newsCnt4",map);
		}
	}

	@Override
	public List<updateCntVO> updateLog() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);

		ArrayList<updateCntVO> updateN = new ArrayList<updateCntVO>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		String[] site = {"조선일보","동아일보","서울신문","YTN","세계일보","한겨례"};
		for(int i = 0; i < site.length; i++) {
			map.put("site", site[i]);
			updateCntVO updateN1 = sqlSession.selectOne(Namespace+".cntNewNews",map);
			updateN.add(updateN1);
		}
		
		return updateN;
	}
	
}

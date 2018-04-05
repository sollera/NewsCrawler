package com.crawl.dao;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.crawl.dto.newsVO;
import com.crawl.dto.numberOfCasesVO;

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
	public List<numberOfCasesVO> newsCnt(String col) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String todate = dayTimeForURL.format(time);
		
		if(col.equals("site")) return sqlSession.selectList(Namespace+".cntPerSite",todate);	//hot 기사 랭킹
		else if(col.equals("type"))return sqlSession.selectList(Namespace+".cntPerType",todate);	//hot 기사 랭킹
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
	public List<newsVO> searchNews(String keyword, int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		map.put("firstNews", first);
		map.put("titleSearch", keyword);
		
		return sqlSession.selectList(Namespace+".searchNews",map);
	}

	@Override
	public int searchCnt(String keyword) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("titleSearch", keyword);
		map.put("today", today);
		
		return sqlSession.selectOne(Namespace+".searchCnt",map);
	}

	@Override
	public List<newsVO> allConditionNews1(String site, String type, String keyword, int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstNews", first);
		map.put("today",date);
		map.put("site", site);
		map.put("titleSearch", keyword);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectList(Namespace+".allConditionNews3",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectList(Namespace+".allConditionNews5",map);
		}
	}

	@Override
	public List<newsVO> allConditionNews2(String site, String keyword, int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);
				
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstNews", first);
		map.put("today",date);
		map.put("site", site);
		map.put("titleSearch", keyword);
		
		return sqlSession.selectList(Namespace+".allConditionNews1",map);
	}

	@Override
	public List<newsVO> allConditionNews3(String type, String keyword, int first) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String date = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("firstNews", first);
		map.put("today",date);
		map.put("titleSearch", keyword);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectList(Namespace+".allConditionNews2",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectList(Namespace+".allConditionNews4",map);
		}
	}

	@Override
	public int allConditionCnt1(String site, String type, String keyword) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		map.put("site", site);
		map.put("titleSearch", keyword);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectOne(Namespace+".allConditionCnt3",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectOne(Namespace+".allConditionCnt5",map);
		}
	}

	@Override
	public int allConditionCnt2(String site, String keyword) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		map.put("site", site);
		map.put("titleSearch", keyword);
		
		return sqlSession.selectOne(Namespace+".allConditionCnt1",map);
	}

	@Override
	public int allConditionCnt3(String type, String keyword) {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);

		String exceptEtc = "";
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("today", today);
		map.put("titleSearch", keyword);
		if(type.indexOf("기타") == -1) {
			map.put("type", type);
			return sqlSession.selectOne(Namespace+".allConditionCnt2",map);
		}else {
			if(type.indexOf(",") == -1) {
				map.put("type","");
			}else {
				exceptEtc = type.substring(0,type.indexOf(",'기타"));
				map.put("type", exceptEtc);
			}
			return sqlSession.selectOne(Namespace+".allConditionCnt4",map);
		}
	}

	

}

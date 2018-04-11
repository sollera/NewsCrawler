package com.crawl.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.crawl.dto.errLogVO;
import com.crawl.dto.errorRatioVO;
import com.crawl.dto.operateTimeVO;
import com.crawl.dto.searchNewsHistoryVO;
import com.crawl.dto.searchNewsVO;
import com.crawl.dto.statusVO;

@Repository
public class StatusDAOImpl implements StatusDAO {
	@Inject
	private SqlSession sqlSession;
	
	private static final String Namespace = "com.news.mapper.statusMapper";
	
	@Override
	public statusVO selectStatus() {
		return sqlSession.selectOne(Namespace+".statusList");	//크롤링 상태
	}

	@Override
	public List<errLogVO> selectLog() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		return sqlSession.selectList(Namespace+".errLog",today);
	}
	
	@Override
	public int powerChk() {
		return sqlSession.selectOne(Namespace+".powerChk");
	}

	@Override
	public List<operateTimeVO> operatingTime() {
		return sqlSession.selectList(Namespace+".operateTime");
	}

	@Override
	public List<searchNewsVO> searchNewsCnt() {
		return sqlSession.selectList(Namespace+".searchNewsCnt");
	}
	
	@Override
	public List<searchNewsHistoryVO> searchNewsHistory() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		return sqlSession.selectList(Namespace+".searchNewsHistory", today);
	}

	@Override
	public List<errorRatioVO> errorHistory() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String today = dayTimeForURL.format(time);
		
		return sqlSession.selectList(Namespace+".errPerTry", today);
	}

}

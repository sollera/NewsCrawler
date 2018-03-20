package com.crawl.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.crawl.dto.newsVO;
import com.crawl.dto.statusVO;

@Repository
public class NewsDAOImpl implements NewsDAO {
	@Inject
	private SqlSession sqlSession;
	
	private static final String Namespace = "com.test.mapper.newsMapper";
	
	@Override
	public List<newsVO> selectNews(int first) {
		return sqlSession.selectList(Namespace+".newsList",first);
	}

	@Override
	public List<statusVO> selectStatus() {
		return sqlSession.selectList(Namespace+".statusList");
	}

	@Override
	public void updateStatus() {
		sqlSession.update(Namespace+".statusUpdate");
	}

	@Override
	public int newsCnt(String site) {
		if(site.equals("")) return sqlSession.selectOne(Namespace+".newsCnt");
		else return sqlSession.selectOne(Namespace+".specificNewsCnt",site);
	}
	
}

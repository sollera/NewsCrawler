package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawl.crawler.Bot_R;
import com.crawl.dao.NewsDAO;
import com.crawl.dao.StatusDAO;
import com.crawl.dto.errorRatioVO;
import com.crawl.dto.errLogVO;
import com.crawl.dto.numberOfCasesVO;
import com.crawl.dto.operateTimeVO;
import com.crawl.dto.searchNewsHistoryVO;
import com.crawl.dto.searchNewsVO;
import com.crawl.dto.statusVO;


@RestController
public class RestAPIController {
	
	@Inject
	private NewsDAO news;
	@Inject
	private StatusDAO status;
	
	Bot_R newsBot = new Bot_R();
	
	//뉴스 페이지 - 탑랭킹1
	@RequestMapping("/topSite.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<numberOfCasesVO> topSite(){
		List<numberOfCasesVO> listS = news.newsCnt("site");
		return listS;
	}
	
	//뉴스 페이지 - 탑랭킹2
	@RequestMapping("/topType.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<numberOfCasesVO> topType(){		
		List<numberOfCasesVO> listT = news.newsCnt("type");
		return listT;
	}

	//instant page - 크롤러 작동 상태 확인
	@RequestMapping("/dbCheck.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public statusVO dbCheck(){
		return status.selectStatus();
	}
	
	//instant page - 크롤러 컨트롤
	@RequestMapping("/bot/{doing}")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public void botControl(@PathVariable String doing){
		if(doing.equals("start")) newsBot.crawlingBot(180);	//크롤링 3분에 한번씩 반복
		else if(doing.equals("end")) {
			newsBot.crawlingBot(0);	//크롤러 할당 쓰레드 강제 종료
		}
		else System.out.println("--------------------------------- 크롤링 핸들러 비정상 접근 ---------------------------------");
	}
	
	//instant page - 크롤링에 걸린 시간
	@RequestMapping("/operatingTime.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<operateTimeVO> operatingTime(){
		List<operateTimeVO> timeVO = status.operatingTime();
		return timeVO;
	}
	
	//instant page - 새로 수집한 데이터
	@RequestMapping("/searchNews.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<searchNewsVO> searchNews(){
		List<searchNewsVO> searchNews = status.searchNewsCnt();
		return searchNews;
	}

	//instant page - 에러로그
	@RequestMapping("/errLog.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<errLogVO> newLog(){
		List<errLogVO> listLog = status.selectLog();
		return listLog;
	}
	
	//history page - 새로 수집한 기사
	@RequestMapping("/collectHistory.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<searchNewsHistoryVO> newDataHistory(){
		List<searchNewsHistoryVO> newsHistory = status.searchNewsHistory();
		return newsHistory;
	}
	
	//history page - 에러율
	@RequestMapping("/errorHistory.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<errorRatioVO> errorHistory(){
		List<errorRatioVO> errCnt = status.errorHistory();
		return errCnt;
	}
	
}

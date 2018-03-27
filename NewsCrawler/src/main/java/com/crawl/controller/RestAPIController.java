package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawl.crawler.Bot_R;
import com.crawl.dao.NewsDAO;
import com.crawl.dto.crawlingRatioVO;
import com.crawl.dto.errLogVO;
import com.crawl.dto.numberOfCasesVO;
import com.crawl.dto.statusVO;


@RestController
public class RestAPIController {
	
	@Inject
	NewsDAO dao;
	
	Bot_R newsBot = new Bot_R();
	
	@RequestMapping("/topSite.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<numberOfCasesVO> topSite(){
		List<numberOfCasesVO> listS = dao.newsCnt("site");
		return listS;
	}
	
	@RequestMapping("/topType.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<numberOfCasesVO> topType(){		
		List<numberOfCasesVO> listT = dao.newsCnt("type");
		return listT;
	}
	
	@RequestMapping("/dbCheck.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public statusVO dbCheck(){
		return dao.selectStatus();
	}
	
	@RequestMapping("/onOff.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public int onOffCheck(){
		int onOff = 1;
		statusVO vo = new statusVO();
		vo = dao.selectStatus();
		if(vo == null) onOff = -1;
		else {
			if(vo.getChosun() == -1 && vo.getDonga() == -1 && vo.getSeoul() == -1 && vo.getYtn() == -1 && vo.getSegye() == -1 && vo.getHangyeorye() == -1) onOff = -1;
		}
		return onOff;
	}
	
	@RequestMapping("/bot/{doing}")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public void botStart(@PathVariable String doing){
		if(doing.equals("start")) newsBot.crawlingBot(180);	//크롤링 3분에 한번씩 반복
		else if(doing.equals("end")) {
			newsBot.crawlingBot(0);	//크롤러 할당 쓰레드 강제 종료
		}
		else System.out.println("--------------------------------- 크롤링 핸들러 비정상 접근 ---------------------------------");
	}
	
	@RequestMapping("/errCnt.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<crawlingRatioVO> errCnt(){
		return dao.selectErrCnt();
	}
	
	@RequestMapping("/newCnt.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<numberOfCasesVO> newCnt(){
		List<numberOfCasesVO> listN = dao.newsCnt("site");
		return listN;
	}
	
	@RequestMapping("/errLog.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<errLogVO> newLog(){
		List<errLogVO> listLog = dao.selectLog();
		return listLog;
	}
	
}

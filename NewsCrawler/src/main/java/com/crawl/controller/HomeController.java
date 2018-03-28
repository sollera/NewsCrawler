package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crawl.dao.NewsDAO;
import com.crawl.dto.newsVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Inject
	private NewsDAO dao;
	
	// Simply selects the home view to render by returning its name.
	@RequestMapping(value = "/news", method = RequestMethod.GET)
	public String viewNewsList(Model model, HttpServletRequest request) {
		String curPage1 = "1";
		if(request.getParameter("p") != null) curPage1 = request.getParameter("p");
		int curPage = 1;
		String errMsg = "";
		
		int allNewsCnt = 0;
		if(request.getParameter("s") == null && request.getParameter("c") == null) allNewsCnt = dao.totalNewsCnt();	//전체 뉴스 수
		
		//필터 있을 때
		String typeS = "";
		String typeC = "";
		if(request.getParameter("s") != null) {
			if(request.getParameter("c") != null) {	//site,type 둘다 필터 적용
				typeS = request.getParameter("s");
				typeS = typeS.replaceAll("-", "','");
				typeC = request.getParameter("c");
				typeC = typeC.replaceAll("-", "','");
				
				allNewsCnt = dao.totalNewsCnt1(typeS, typeC);
			}else {	//site 만 필터 적용
				typeS = request.getParameter("s");
				typeS = typeS.replaceAll("-", "','");
				
				allNewsCnt = dao.totalNewsCnt2(typeS);
			}
		}else if(request.getParameter("c") != null) {	//type만 필터 적용
			typeC = request.getParameter("c");
			typeC = typeC.replaceAll("-", "','");
			
			allNewsCnt = dao.totalNewsCnt3(typeC);
		}
		
		int realLastPage = (allNewsCnt+19) / 20;	//전체 페이지 수
		
		//페이지 패스 에러
		try {curPage = Integer.parseInt(curPage1);} catch(Exception numErr) {errMsg = "잘못된 주소로의 접근입니다.";}
		if(curPage < 1) {curPage = 1; errMsg = "잘못된 주소로의 접근입니다.";}			
		if(curPage > realLastPage) {curPage = 1; errMsg = "잘못된 주소로의 접근입니다.";}

		int firstNews = (curPage-1)*20;	//해당 페이지의 첫 기사 번호(0번부터 시작)
		
		List<newsVO> newsList = null;
		if(request.getParameter("s") == null && request.getParameter("c") == null) newsList = dao.selectNews(firstNews);	//해당 페이지의 전체 뉴스 객체
		
		if(request.getParameter("s") != null) {	//필터 있을 때
			if(request.getParameter("c") != null) newsList = dao.selectNews1(typeS,typeC,firstNews);
			else newsList = dao.selectNews2(typeS,firstNews);
		} else if(request.getParameter("c") != null) newsList = dao.selectNews3(typeC,firstNews);
		
		int firstPage = ((curPage-1)/10)*10 + 1;	//해당 페이지 블럭의 첫 페이지
		
		//페이징 블록 이동 정보 생성
		String previous = "yes";
		if(firstPage == 1) previous = "no";
		String next = "yes";
		if((firstPage+10) > realLastPage) next = "no";
		
		//뉴스 정보 객체 보내기
		model.addAttribute("newsList",newsList);
		
		//페이징 정보 객체 보내기
		model.addAttribute("firstPage",firstPage);
		model.addAttribute("lastPage",realLastPage);
		model.addAttribute("curPage",curPage);
		model.addAttribute("previous",previous);
		model.addAttribute("next",next);		

		//잘못된 패스 접근 에러메세지 보내기
		model.addAttribute("errMsg",errMsg);
		
		
		return "totalNews";
	}
	@RequestMapping(value = {"/","/news"})
	public String reNewsList() {
		return "redirect:/news/1";
	}
	
	// Simply selects the home view to render by returning its name.
//	@RequestMapping(value = "/news/{curPage1}", method = RequestMethod.GET)
//	public String viewNewsList(Model model,@PathVariable String curPage1) {
//		int curPage = 1;
//		String errMsg = "";
//		try {curPage = Integer.parseInt(curPage1);} catch(Exception numErr) {errMsg = "잘못된 주소로의 접근입니다.";}	//페이지 패스 에러
//		
//		int allNewsCnt = dao.totalNewsCnt();	//전체 뉴스 수
//		int firstPage = ((curPage-1)/10)*10 + 1;
//		int realLastPage = (allNewsCnt+19) / 20;	//전체 페이지 수
//		
//		if(curPage < 1) {curPage = 1; errMsg = "잘못된 주소로의 접근입니다.";}			//페이지 패스 에러
//		if(curPage > realLastPage) {curPage = 1; errMsg = "잘못된 주소로의 접근입니다.";}	//페이지 패스 에러
//		
//		//페이징 블록 이동 정보 생성
//		String previous = "yes";
//		if(firstPage == 1) previous = "no";
//		String next = "yes";
//		if((curPage+10) > realLastPage) next = "no";
//		
//		int firstNews = (curPage-1)*20;	//해당 페이지의 첫 기사 번호(0번부터 시작)
//		List<newsVO> newsList = dao.selectNews(firstNews);	//해당 페이지의 뉴스 객체
//		
//		//뉴스 정보 객체 보내기
//		model.addAttribute("newsList",newsList);
//		
//		//페이징 정보 객체 보내기
//		model.addAttribute("firstPage",firstPage);
//		model.addAttribute("lastPage",realLastPage);
//		model.addAttribute("curPage",curPage);
//		model.addAttribute("previous",previous);
//		model.addAttribute("next",next);		
//
//		//잘못된 패스 접근 에러메세지 보내기
//		model.addAttribute("errMsg",errMsg);
//		
//		return "totalNews";
//	}
	
	
	@RequestMapping(value = "/status")
	public String statusPage() {
		return "statusPage";
	}
	
	/*
	@RequestMapping(value = "/test")
	public void test() {
		Bot_R b = new Bot_R();
		b.crawlingBot(300);
	}
	*/
}

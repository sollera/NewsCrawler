package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crawl.dao.NewsDAO;
import com.crawl.dao.StatusDAO;
import com.crawl.dto.newsVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Inject
	private NewsDAO news;
	@Inject
	private StatusDAO status;
	
	// Simply selects the home view to render by returning its name.
	@RequestMapping(value = "/news", method = RequestMethod.GET)
	public String viewNewsList(Model model, HttpServletRequest request) {
		String curPage1 = "1";
		if(request.getParameter("p") != null) curPage1 = request.getParameter("p");
		int curPage = 1;
		String errMsg = "";
		
		int allNewsCnt = 0;
		String typeS = "";
		String typeC = "";
		//전체 뉴스 기사 수 받기 ----------------------//
		
		//필터 없고 검색도 아닐 때
//		if(request.getParameter("s") == null && request.getParameter("c") == null) allNewsCnt = dao.totalNewsCnt();
				
		if(request.getParameter("w") == null) {	//검색X
			//필터O , 검색X
			if(request.getParameter("s") != null) {
				if(request.getParameter("c") != null) {
					//신문사,카테고리 필터 둘다 적용
					typeS = request.getParameter("s");
					typeS = typeS.replaceAll("-", "','");
					typeC = request.getParameter("c");
					typeC = typeC.replaceAll("-", "','");
					
					allNewsCnt = news.totalNewsCnt1(typeS, typeC);
				} else {
					//신문사 필터만 적용
					typeS = request.getParameter("s");
					typeS = typeS.replaceAll("-", "','");
					
					allNewsCnt = news.totalNewsCnt2(typeS);
				}
			} else if(request.getParameter("c") != null) {
				//카테고리 필터만 적용
				typeC = request.getParameter("c");
				typeC = typeC.replaceAll("-", "','");
				
				allNewsCnt = news.totalNewsCnt3(typeC);
			} else {
				//필터X , 검색X
				allNewsCnt = news.totalNewsCnt();
			}
		} else {	//검색O
			String keyword = request.getParameter("w");
			
			if(keyword.contains(" ")) {
				String[] word = keyword.split(" ");
				keyword = "";
				for(int i = 0; i < word.length; i++) {
					keyword += "|"+ word[i];
				}
				keyword = keyword.substring(1);
			}
			
			//필터O , 검색O
			if(request.getParameter("s") != null) {
				if(request.getParameter("c") != null) {
					//신문사,카테고리 필터 둘다 적용
					typeS = request.getParameter("s");
					typeS = typeS.replaceAll("-", "','");
					typeC = request.getParameter("c");
					typeC = typeC.replaceAll("-", "','");
					
					allNewsCnt = news.allConditionCnt1(typeS, typeC, keyword);
				}else {
					//신문사 필터만 적용
					typeS = request.getParameter("s");
					typeS = typeS.replaceAll("-", "','");
					
					allNewsCnt = news.allConditionCnt2(typeS, keyword);
				}
			}else if(request.getParameter("c") != null) {
				//카테고리 필터만 적용
				typeC = request.getParameter("c");
				typeC = typeC.replaceAll("-", "','");
				
				allNewsCnt = news.allConditionCnt3(typeC, keyword);
			} else {
				//필터X , 검색O
				allNewsCnt = news.searchCnt(keyword);
			}
		}
		
		//--------------------------------------//
		
		int realLastPage = (allNewsCnt+19) / 20;	//전체 페이지 수
		
		//페이지 패스 에러
		try {curPage = Integer.parseInt(curPage1);} catch(Exception numErr) {errMsg = "잘못된 주소로의 접근입니다.";}
		if(curPage < 1) {curPage = 1; errMsg = "잘못된 주소로의 접근입니다.";}			
		if(curPage > realLastPage) {curPage = 1; errMsg = "잘못된 주소로의 접근입니다.";}

		int firstNews = (curPage-1)*20;	//해당 페이지의 첫 기사 번호(0번부터 시작)
		
		List<newsVO> newsList = null;
		
		
		//전체 뉴스 기사 정보 객체 받기 -----------//
		
		//필터 없고 검색 아닐 때
//		if(request.getParameter("s") == null && request.getParameter("c") == null) newsList = dao.selectNews(firstNews);
		
		if(request.getParameter("w") == null) {	//검색X
			//필터O , 검색X
			if(request.getParameter("s") != null) {
				if(request.getParameter("c") != null) {
					//신문사,카테고리 필터 둘다 적용
					newsList = news.selectNews1(typeS,typeC,firstNews);
				} else {
					//신문사 필터만 적용
					newsList = news.selectNews2(typeS,firstNews);
				}
			} else if(request.getParameter("c") != null) {
				//카테고리 필터만 적용
				newsList = news.selectNews3(typeC,firstNews);
			} else {
				//필터X , 검색X
				newsList = news.selectNews(firstNews);
			}
		}else {	//검색O
			String keyword = request.getParameter("w");
			
			if(keyword.contains(" ")) {
				String[] word = keyword.split(" ");
				keyword = "";
				for(int i = 0; i < word.length; i++) {
					keyword += "|"+ word[i];
				}
				keyword = keyword.substring(1);
			}
			
			//필터O , 검색O
			if(request.getParameter("s") != null) {
				if(request.getParameter("c") != null) {
					//신문사,카테고리 필터 둘다 적용
					newsList = news.allConditionNews1(typeS, typeC, keyword, firstNews);
				} else {
					//신문사 필터만 적용
					newsList = news.allConditionNews2(typeS, keyword, firstNews);
				}
			} else if(request.getParameter("c") != null) {
				//카테고리 필터만 적용
				newsList = news.allConditionNews3(typeC, keyword, firstNews);
			} else {
				//필터X , 검색O
				newsList = news.searchNews(keyword, firstNews);
			}
		}
		
		//--------------------------------//
		
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
	
	
	@RequestMapping(value = "/status")
	public String statusPage(Model model) {
		model.addAttribute("power",status.powerChk());
		return "crawlerStatus";
	}
}

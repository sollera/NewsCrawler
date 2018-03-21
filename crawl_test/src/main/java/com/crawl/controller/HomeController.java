package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crawl.crawler.NewsBot;
import com.crawl.dao.NewsDAO;
import com.crawl.dto.newsVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Inject
	private NewsDAO dao;
	
	NewsBot bot = new NewsBot();
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/news/{curPage1}", method = RequestMethod.GET)
	public String viewNewsList(Model model,@PathVariable String curPage1) {
		int curPage = 1;
		String errMsg = "";
		try {curPage = Integer.parseInt(curPage1);} catch(Exception numErr) {errMsg = "잘못된 접근입니다.";}
		
		int allNewsCnt = dao.newsCnt("");	//전체 뉴스 수
		int firstPage = curPage/10 + 1;
		int realLastPage = (allNewsCnt+19) / 20;	//전체 페이지 수
		
		if(curPage < 1) {curPage = 1; errMsg = "잘못된 접근입니다.";}	//페이지 패스 에러 잡기
		if(curPage > realLastPage) {curPage = 1; errMsg = "잘못된 접근입니다.";}
		
		//페이징 블록 이동 정보 생성
		String previous = "yes";
		if(firstPage == 1) previous = "no";
		String next = "yes";
		if((curPage+10) > realLastPage) next = "no";
		
		int firstNews = (curPage-1)*20;	//해당 페이지의 첫 기사 번호(0번부터 시작)
		List<newsVO> newsList = dao.selectNews(firstNews);	//해당 페이지의 뉴스 객체
		
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
	@RequestMapping(value = "/")
	public String viewNewsList() {
		return "redirect:/news/1";
	}
	
	@RequestMapping(value = "/status/{power}")
	public String statusPage(@PathVariable String power, Model model, HttpServletRequest httpServletRequest) {
		String ajaxStart = "";
		if(httpServletRequest.getParameter("ajax") != null) ajaxStart = httpServletRequest.getParameter("ajax");
		model.addAttribute("ajaxStart",ajaxStart);
		model.addAttribute("power",power);
		return "statusPage";
	}
	@RequestMapping(value = "/status")
	public String statusPage1() {
		return "redirect:/status/powerOff";
	}
	
	@RequestMapping(value = "/powerOn")
	public String botStart() {
		bot.crawlingBot(60);
		return "redirect:/status/powerOn?ajax=go";
	}
	@RequestMapping(value = "/powerOff")
	public String botEnd() {
		bot.crawlingBot(0);
		return "redirect:/status/powerOff";
	}
}

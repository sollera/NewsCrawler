package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/news/{firstNews}", method = RequestMethod.GET)
	public String viewNewsList(Model model,@PathVariable int firstNews) {
		if(firstNews < 1) firstNews = 1;
		List<newsVO> newsList = dao.selectNews(firstNews-1);
		model.addAttribute("newsList",newsList);
		//페이징 정보 객체 보내기
		
		return "totalNews";
	}
	@RequestMapping(value = "/")
	public String viewNewsList() {
		return "redirect:/news/1";
	}
	
	@RequestMapping(value = "/status")
	public String statusPage() {
		
		return "statusPage";
	}
}

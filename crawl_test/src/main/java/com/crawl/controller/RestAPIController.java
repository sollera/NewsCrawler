package com.crawl.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawl.dao.NewsDAO;
import com.crawl.dto.statusVO;


@RestController
public class RestAPIController {
	
	@Inject
	NewsDAO dao;
	
	@RequestMapping("/status.do")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<statusVO> listStatus(){		
		List<statusVO> listS = dao.selectStatus();
		return listS;
	}
	
	/*
	@RequestMapping("/news.do/{firstNews}")
	@ResponseBody // 리턴데이터를 json으로 변환(생략가능)
	public List<newsVO> listNews(@PathVariable int firstNews){
		List<newsVO> listN = dao.selectNews(firstNews);
	     return listN;
	}
	*/
}

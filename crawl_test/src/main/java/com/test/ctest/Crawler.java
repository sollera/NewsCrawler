package com.test.ctest;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	//조선일보
	public ArrayList<String[]> chosun() throws IOException {
		Document docChosun = Jsoup.connect("http://www.chosun.com/").get();
		Element newsContentChosun = docChosun.select("div.sec_con").first();
		Elements linksChosun = newsContentChosun.select("dl.art_list_item dt a");	//탑뉴스 제외, 관련기사 제외한 주요 기사들의 헤드라인만
		
		ArrayList<String[]> chosunList = new ArrayList<String[]>();
		
		for(Element newslinkChosun : linksChosun) {
			if(newslinkChosun.text().equals("") == false) {
				String headlineTxt = newslinkChosun.text();
				if(headlineTxt.indexOf("[사설]") == 0) headlineTxt = headlineTxt.substring(4);
				else if(headlineTxt.indexOf("[속보]") == 0) headlineTxt = headlineTxt.substring(4);
				String[] chosunNews = {headlineTxt,newslinkChosun.attr("abs:href")};
				chosunList.add(chosunNews);
			}
		}
		
		return chosunList;
	}
	
	//동아일보
	public ArrayList<String[]> donga() throws IOException {
		Document docDonga = Jsoup.connect("http://www.donga.com/").get();
		Element newsContentDonga = docDonga.select("div.mNewsLi").first();
		Elements linksDonga = newsContentDonga.select("span.txt_li a");	//탑뉴스 제외, 관련기사 제외한 주요 기사들의 헤드라인만
		
		ArrayList<String[]> dongaList = new ArrayList<String[]>();
		
		for(Element newslinkDonga : linksDonga) {
			if(newslinkDonga.text().equals("") == false) {
				String[] dongaNews = {newslinkDonga.text(),newslinkDonga.attr("abs:href")};
				dongaList.add(dongaNews);
			}
		}
		
		return dongaList;
	}
	
	//서울신문  -- 일부 기사는 연관기사 존재
	public ArrayList<String[]> seoul() throws IOException {
		Document docSeoul = Jsoup.connect("http://www.seoul.co.kr/").get();
		Element newsContentSeoul = docSeoul.select("div.inx_Alist ul.alllist_part").first();
		Elements linksSeoul = newsContentSeoul.select("li div.alllist_txt2 div.Narticle div.NAtitle2 a");	//탑뉴스 제외, 관련기사 제외한 주요 기사들의 헤드라인만
		
		ArrayList<String[]> seoulList = new ArrayList<String[]>();
		
		for(Element newslinkSeoul : linksSeoul) {
			if(newslinkSeoul.text().equals("") == false) {
				String[] seoulNews = {newslinkSeoul.text(),newslinkSeoul.attr("abs:href")};
				seoulList.add(seoulNews);
			}
		}
		
		return seoulList;
	}
	
	//YTN
	public ArrayList<String[]> ytn() throws IOException {
		Document docYtn = Jsoup.connect("http://www.ytn.co.kr/").get();
		Element newsContentYtn = docYtn.select("div.arti_center_box").first();
		Elements linksYtn1 = newsContentYtn.select("dl dd.arti_text a");
		Elements linksYtn2 = newsContentYtn.select("div.unit_box p a");	//탑뉴스 제외, 관련기사 제외한 주요 기사들의 헤드라인만
		
		ArrayList<String[]> ytnList = new ArrayList<String[]>();
		for(Element newslinkYtn1 : linksYtn1) {
			if(newslinkYtn1.text().equals("") == false) {
				String headlineTxt = newslinkYtn1.text();
				if(headlineTxt.indexOf("[영상]") == 0) headlineTxt = headlineTxt.substring(4);
				String[] ytnNews = {headlineTxt,newslinkYtn1.attr("abs:href")};
				ytnList.add(ytnNews);
			}
		}
		for(Element newslinkYtn2 : linksYtn2) {
			if(newslinkYtn2.text().equals("") == false) {
				String headlineTxt = newslinkYtn2.text();
				if(headlineTxt.indexOf("[영상]") == 0) headlineTxt = headlineTxt.substring(4);
				String[] ytnNews = {headlineTxt,newslinkYtn2.attr("abs:href")};
				ytnList.add(ytnNews);
			}
		}
		
		return ytnList;
	}
	
	//세계일보
	public ArrayList<String[]> segye() throws IOException {
		Document docSegye = Jsoup.connect("http://www.segye.com/main").get();
		Element newsContentSegye = docSegye.select("div.juyoNews").first();
		Elements linksSegye = newsContentSegye.select("ul.line5box li span.headline a");
		
		ArrayList<String[]> segyeList = new ArrayList<String[]>();
		for(Element newslinkSegye : linksSegye) {
			if(newslinkSegye.text().equals("") == false) {
				String headlineTxt = newslinkSegye.text();
				if(headlineTxt.indexOf("단독") == 0) headlineTxt = headlineTxt.substring(2);
				else if(headlineTxt.indexOf("[이슈+]") == 0) headlineTxt = headlineTxt.substring(6);
				String[] segyeNews = {headlineTxt,newslinkSegye.attr("abs:href")};
				segyeList.add(segyeNews);
			}
		}
		
		return segyeList;
	}
	
	//NewDaily
	public ArrayList<String[]> newDaily() throws IOException {
		Document docNewDaily = Jsoup.connect("http://www.newdaily.co.kr/").get();
		Element newsContentNewDaily = docNewDaily.select("div#arttype33").first();
		Elements linksNewDaily = newsContentNewDaily.select("div.arttype6 div.title a");
		
		ArrayList<String[]> newDailyList = new ArrayList<String[]>();
		for(Element newslinkNewDaily : linksNewDaily) {
			if(newslinkNewDaily.text().equals("") == false) {
				String[] newDailyNews = {newslinkNewDaily.text(),newslinkNewDaily.attr("abs:href")};
				newDailyList.add(newDailyNews);
			}
		}
		
		return newDailyList;
	}
}

package com.crawl.crawler;

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
		
		String headlineTxt = null;
		String url = null;
		String dt = null;
		
		for(Element newslinkChosun : linksChosun) {
			if(newslinkChosun.text().equals("") == false && newslinkChosun.attr("abs:href").contains("site/data/html_dir")) {
				headlineTxt = newslinkChosun.text().trim();
				if(headlineTxt.indexOf("[사설]") == 0) headlineTxt = headlineTxt.substring(4);
				else if(headlineTxt.indexOf("[속보]") == 0) headlineTxt = headlineTxt.substring(4);
				headlineTxt = headlineTxt.replaceAll("'", "");
				headlineTxt = headlineTxt.replaceAll("\"", "");
				url = newslinkChosun.attr("abs:href").trim();
				dt = url.substring(url.indexOf("html_dir/")+9,url.indexOf("html_dir/")+19);
				String[] chosunNews = {"조선일보",headlineTxt,url,dt};
				
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
		
		String headlineTxt = null;
		String url = null;
		String dt = null;
		
		for(Element newslinkDonga : linksDonga) {
			if(newslinkDonga.text().equals("") == false) {
				headlineTxt = newslinkDonga.text().trim();
				headlineTxt = headlineTxt.replaceAll("'", "");
				headlineTxt = headlineTxt.replaceAll("\"", "");
				url = newslinkDonga.attr("abs:href").trim();
				dt = url.substring(33,37)+"/"+url.substring(37,39)+"/"+url.substring(39,41);
				String[] dongaNews = {"동아일보",headlineTxt,url,dt};
				
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
		
		String headlineTxt = null;
		String url = null;
		String dt = null;
		
		for(Element newslinkSeoul : linksSeoul) {
			if(newslinkSeoul.text().equals("") == false) {
				headlineTxt = newslinkSeoul.text().trim();
				headlineTxt = headlineTxt.replaceAll("'", "");
				headlineTxt = headlineTxt.replaceAll("\"", "");
				url = newslinkSeoul.attr("abs:href").trim();
				dt = url.substring(44,48)+"/"+url.substring(48,50)+"/"+url.substring(50,52);
				String[] seoulNews = {"서울신문",headlineTxt,url,dt};
				
				seoulList.add(seoulNews);
			}
		}
		
		return seoulList;
	}
	
	//YTN
	public ArrayList<String[]> ytn() throws IOException {
		Document docYtn = Jsoup.connect("http://www.ytn.co.kr/").get();
		Element newsContentYtn = docYtn.select("div.arti_center_box").first();
		
		ArrayList<String[]> ytnList = new ArrayList<String[]>();

		String headlineTxt = null;
		String url = null;
		String dt = null;
		
		try {
			Elements linksYtn1 = newsContentYtn.select("div.unit_box dl dd.arti_text a");
			for(Element newslinkYtn1 : linksYtn1) {
				if(newslinkYtn1.text().equals("") == false) {
					headlineTxt = newslinkYtn1.text().trim();
					headlineTxt = headlineTxt.replaceAll("'", "");
					headlineTxt = headlineTxt.replaceAll("\"", "");
					if(headlineTxt.indexOf("[영상]") == 0) headlineTxt = headlineTxt.substring(4);
					url = newslinkYtn1.attr("abs:href").trim();
					dt = url.substring(30,34)+"/"+url.substring(34,36)+"/"+url.substring(36,38);
					String[] ytnNews = {"YTN",headlineTxt,url,dt};

					ytnList.add(ytnNews);
				}
			}
		}catch(Exception eY) {}
		
		try {
			Elements linksYtn2 = newsContentYtn.select("div.unit_box p a");	//탑뉴스 제외, 관련기사 제외한 주요 기사들의 헤드라인만
			for(Element newslinkYtn2 : linksYtn2) {
				if(newslinkYtn2.text().equals("") == false) {
					headlineTxt = newslinkYtn2.text();
					headlineTxt = headlineTxt.replaceAll("'", "");
					headlineTxt = headlineTxt.replaceAll("\"", "");
					if(headlineTxt.indexOf("[영상]") == 0) headlineTxt = headlineTxt.substring(4);
					url = newslinkYtn2.attr("abs:href").trim();
					dt = url.substring(30,34)+"/"+url.substring(34,36)+"/"+url.substring(36,38);
					String[] ytnNews = {"YTN",headlineTxt,url,dt};
					
					ytnList.add(ytnNews);
				}
			}
		}catch(Exception eY1) {}
		
		return ytnList;
	}
	
	//세계일보
	public ArrayList<String[]> segye() throws IOException {
		Document docSegye = Jsoup.connect("http://www.segye.com/main").get();
		Element newsContentSegye = docSegye.select("div.juyoNews").first();
		Elements linksSegye = newsContentSegye.select("ul.line5box li span.headline a");
		
		ArrayList<String[]> segyeList = new ArrayList<String[]>();

		String headlineTxt = null;
		String url = null;
		String dt = null;
		
		for(Element newslinkSegye : linksSegye) {
			if(newslinkSegye.text().equals("") == false) {
				headlineTxt = newslinkSegye.text();
				headlineTxt = headlineTxt.replaceAll("'", "");
				headlineTxt = headlineTxt.replaceAll("\"", "");
				if(headlineTxt.indexOf("단독") == 0) headlineTxt = headlineTxt.substring(2);
				else if(headlineTxt.indexOf("[이슈+]") == 0) headlineTxt = headlineTxt.substring(6);
				url = newslinkSegye.attr("abs:href").trim();
				dt = url.substring(30,34)+"/"+url.substring(34,36)+"/"+url.substring(36,38);
				String[] segyeNews = {"YTN",headlineTxt,url,dt};
				
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
		
		String headlineTxt = null;
		String url = null;
		String dt = null;
		
		for(Element newslinkNewDaily : linksNewDaily) {
			if(newslinkNewDaily.text().equals("") == false && newslinkNewDaily.text().indexOf("http://www") == 0) {
				headlineTxt = newslinkNewDaily.text().trim();
				headlineTxt = headlineTxt.replaceAll("'", "\"");
				url = newslinkNewDaily.attr("abs:href").trim();
				dt = url.substring(41,51);
				String[] newDailyNews = {"NewDaily",headlineTxt,url,dt};
				newDailyList.add(newDailyNews);
			}
		}
		
		return newDailyList;
	}
}

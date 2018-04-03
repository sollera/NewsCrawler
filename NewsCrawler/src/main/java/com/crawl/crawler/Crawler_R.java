package com.crawl.crawler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler_R {
	
	private int[] chosunCnt;
	private int[] dongaCnt;
	private int[] seoulCnt;
	private int[] ytnCnt;
	private int[] segyeCnt;
	private int[] hangyeoryeCnt;
	
	public int[] getChosunCnt() {
		return chosunCnt;
	}

	public void setChosunCnt(int[] chosunCnt) {
		this.chosunCnt = chosunCnt;
	}

	public int[] getDongaCnt() {
		return dongaCnt;
	}

	public void setDongaCnt(int[] dongaCnt) {
		this.dongaCnt = dongaCnt;
	}

	public int[] getSeoulCnt() {
		return seoulCnt;
	}

	public void setSeoulCnt(int[] seoulCnt) {
		this.seoulCnt = seoulCnt;
	}

	public int[] getYtnCnt() {
		return ytnCnt;
	}

	public void setYtnCnt(int[] ytnCnt) {
		this.ytnCnt = ytnCnt;
	}

	public int[] getSegyeCnt() {
		return segyeCnt;
	}

	public void setSegyeCnt(int[] segyeCnt) {
		this.segyeCnt = segyeCnt;
	}

	public int[] getHangyeoryeCnt() {
		return hangyeoryeCnt;
	}

	public void setHangyeoryeCnt(int[] hangyeoryeCnt) {
		this.hangyeoryeCnt = hangyeoryeCnt;
	}

	
	
	//조선일보
	public ArrayList<String[]> chosun() throws Exception {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyyMMdd");
		String todayDate = dayTimeForURL.format(time);
		
		String ChosunTotalNews = "http://news.chosun.com/svc/list_in/list_title.html?indate="+todayDate+"&source=1&pn=";
		Document docChosun;
		Element newsGroupChosun;
		Elements newsLinksChosun;
		
		Document docNewsCon;
		Element elNewsType;
		Element elNewsTitle;
		Element elNewsTime;
		String newsType;
		String newsTitle;
		String url;
		String uploadTime;
		
		ArrayList<String[]> chosunNews = new ArrayList<String[]>();
		
		int i = 0;
		int success = 0;
		int error = 0;
		while(true){
			i++;
			docChosun = Jsoup.connect(ChosunTotalNews+i).get();
			newsGroupChosun = docChosun.select("article.news_list div.list_body div.list_title").first();
			if(newsGroupChosun == null) break;
			if(newsGroupChosun.text().trim().equals("")) break;
			newsLinksChosun = newsGroupChosun.select("dl.list_item dt a");

			for(Element linkChosun : newsLinksChosun) {
				url = linkChosun.attr("abs:href").trim();
				docNewsCon = Jsoup.connect(url).get();
				elNewsType = docNewsCon.select("head title").first();
				if(elNewsType == null) {
					error++;	//에러 카운트
					continue;
				}
				newsType = elNewsType.text().trim();
				if(newsType.indexOf("- 조선닷컴 -") == -1) {
					error++;	//에러 카운트
					continue;
				}
				newsType = newsType.substring(newsType.indexOf("조선닷컴 -")+7).trim();
				if(newsType.indexOf(" ") != -1) newsType = newsType.substring(0,newsType.indexOf(" ")).trim();
				
				newsType = typeConsistency(newsType);
				
				elNewsTitle = docNewsCon.select("header.news_title div.news_title_text h1").first();
				if(elNewsTitle == null) {
					error++;	//에러 카운트
					continue;
				}
				newsTitle = elNewsTitle.text().trim();
//				newsTitle = newsTitle.replaceAll("'", "\"");	//DB 삽입 시 에러방지
				elNewsTime = docNewsCon.select("article.news_article div.news_body div.news_date div p").first();
				if(elNewsTime == null) {
					error++;	//에러 카운트
					continue;
				}
				uploadTime = elNewsTime.text();
				if(uploadTime.indexOf("수정 :") == -1) {
					uploadTime = uploadTime.replaceFirst("입력 :", "").trim();
				}else {
					uploadTime = uploadTime.substring(uploadTime.indexOf("수정 :")+4).trim();
				}
				
				uploadTime = uploadTime.substring(0,4)+"-"+uploadTime.substring(5,7)+"-"+uploadTime.substring(8);

				String[] newsData = {"조선일보",newsType,newsTitle,url,uploadTime};
				
				chosunNews.add(newsData);
				success++;
			}
		}
		int[] cnt = {success,error};
		setChosunCnt(cnt);
		return chosunNews;
	}
	
	//동아일보
	public ArrayList<String[]> donga() throws Exception {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyyMMdd");
		String todayDate = dayTimeForURL.format(time);
		
		String DongaTotalNews1 = "http://news.donga.com/List?p=";
		String DongaTotalNews2 = "&prod=news&ymd="+todayDate+"&m=NP";
		Document docDonga;
		Element newsGroupDonga;
		Elements newsLinksDonga;
		
		Document docNewsCon;
		Element elNewsType;
		Element elNewsTitle;
		Elements elNewsTimes;
		String newsType;
		String newsTitle;
		String url;
		String uploadTime = "";
		
		ArrayList<String[]> dongaNews = new ArrayList<String[]>();
		
		int i = 0;
		int success = 0;
		int error = 0;
		while(true){
			i++;
			docDonga = Jsoup.connect(DongaTotalNews1+((i-1)*20+1)+DongaTotalNews2).get();
			newsGroupDonga = docDonga.select("div#container div#contents").first();
			if(newsGroupDonga == null) {
				error++;	//에러 카운트
				continue;
			}
			if(newsGroupDonga.select("div.articleList").first().text().trim().equals("해당 기사가 없습니다.")) break;
			newsLinksDonga = newsGroupDonga.select("div.articleList div.rightList a");

			for(Element linkDonga : newsLinksDonga) {
				url = linkDonga.attr("abs:href").trim();
				docNewsCon = Jsoup.connect(url).get();
				elNewsType = docNewsCon.select("div.article_title div.location a").first();
				if(elNewsType == null) {
					error++;	//에러 카운트
					continue;
				}
				newsType = elNewsType.text().trim();
				
				newsType = typeConsistency(newsType);
				
				elNewsTitle = docNewsCon.select("div.article_title h2.title").first();
				if(elNewsTitle == null) {
					error++;	//에러 카운트
					continue;
				}
				newsTitle = elNewsTitle.text().trim();
//				newsTitle = newsTitle.replaceAll("'", "\"");	//DB 삽입 시 에러방지
				elNewsTimes = docNewsCon.select("div.article_title div.title_foot span.date01");
				if(elNewsTimes == null) {
					error++;	//에러 카운트
					continue;
				}
				for(Element elNewsTime : elNewsTimes) {
					String dt = elNewsTime.text().trim();
					uploadTime = dt;
					if(dt.indexOf("수정") != -1) uploadTime = dt.substring(dt.indexOf("수정")+2).trim();
				}

				String[] newsData = {"동아일보",newsType,newsTitle,url,uploadTime};
				
				dongaNews.add(newsData);
				success++;
			}
		}
		int[] cnt = {success,error};
		setDongaCnt(cnt);
		return dongaNews;
	}
	
	//서울신문
	public ArrayList<String[]> seoul() throws Exception {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = dayTimeForURL.format(time);
		
		String SeoulTotalNews1 = "http://www.seoul.co.kr/news/newsList.php?section=";
		String SeoulTotalNews2 = "&date="+todayDate+"&type=&page=";
		String SeoulTotalNews = "";
		Document docSeoul;
		Element newsGroupSeoul;
		Elements newsLinksSeoul;
		
		Document docNewsCon;
		Element elNewsTitle;
		Element elNewsTime;
		String newsType = "";
		String newsTitle;
		String url;
		String uploadTime = "";
		
		ArrayList<String[]> seoulNews = new ArrayList<String[]>();
		String[] sections = {"politics","economy","society","editOpinion","sport","entertainment"};

		int success = 0;
		int error = 0;
		for(int s = 0; s < sections.length; s++) {
			SeoulTotalNews = SeoulTotalNews1 + sections[s] + SeoulTotalNews2;
			int i = 0;
			while(true){
				i++;
				docSeoul = Jsoup.connect(SeoulTotalNews+i).get();
				newsGroupSeoul = docSeoul.select("div.middle_inner div.division_outer div.division.divisionLeft div.innerDiv div#list_area").first();
				if(newsGroupSeoul == null) break;
				if(newsGroupSeoul.text().trim().equals("")) break;
				newsLinksSeoul = newsGroupSeoul.select("dl.s_article dt span a");
	
				for(Element linkSeoul : newsLinksSeoul) {
					url = linkSeoul.attr("abs:href").trim();
					docNewsCon = Jsoup.connect(url).get();
					if(s == 0) newsType = "정치";
					else if(s == 1) newsType = "경제";
					else if(s == 2) newsType = "사회";
					else if(s == 3) newsType = "오피니언";
					else if(s == 4) newsType = "스포츠";
					else if(s == 5) newsType = "연예";
					else newsType = "기타";
					elNewsTitle = docNewsCon.select("div.title div.title_inner_art div.article_tit h1.atit2").first();
					if(elNewsTitle == null) {
						error++;	//에러 카운트
						continue;
					}
					newsTitle = elNewsTitle.text().trim();
//					newsTitle = newsTitle.replaceAll("'", "\"");	//DB 삽입 시 에러방지
					elNewsTime = docNewsCon.select("div.title div.title_inner_art div.article_tit div.v_snt div.v_journal p.v_days").first();
					if(elNewsTime == null) {
						error++;	//에러 카운트
						continue;
					}
					uploadTime = elNewsTime.text();
					uploadTime = uploadTime.substring(uploadTime.indexOf("수정 :")+4).trim();
	
					String[] newsData = {"서울신문",newsType,newsTitle,url,uploadTime};

					seoulNews.add(newsData);
					success++;
				}
			}
		}
		int[] cnt = {success,error};
		setSeoulCnt(cnt);
		return seoulNews;
	}
	
	//YTN
	public ArrayList<String[]> ytn() throws Exception {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = dayTimeForURL.format(time);
		
		String YtnTotalNews1 = "http://www.ytn.co.kr/news/news_list.php?page=";
		String YtnTotalNews2 = "&s_mcd=";
		String YtnTotalNews3 = "";
		Document docYtn;
		Element newsGroupYtn;
		Elements newsLinksYtn;
		
		Document docNewsCon;
		Element elNewsTitle;
		Element elNewsTime;
		String newsType = "";
		String newsTitle;
		String url;
		String uploadTime = "";
		
		ArrayList<String[]> ytnNews = new ArrayList<String[]>();
		String[] sections = {"0101","0102","0103","0106"};

		int success = 0;
		int error = 0;
		for(int s = 0; s < sections.length; s++) {
			YtnTotalNews3 = YtnTotalNews2 + sections[s];
			boolean go = true;
			int i = 0;
			while(true){
				if(go == false) break;		
				i++;
				docYtn = Jsoup.connect(YtnTotalNews1+i+YtnTotalNews3).get();
				newsGroupYtn = docYtn.select("div#czone div#zone1 div#ytn_list_v2014").first();
				if(newsGroupYtn == null) {
					error++;	//에러 카운트
					continue;
				}
				newsLinksYtn = newsGroupYtn.select("dl.news_list_v2014 dt a");
	
				for(Element linkYtn : newsLinksYtn) {
					url = linkYtn.attr("abs:href").trim();
					docNewsCon = Jsoup.connect(url).get();
					if(sections[s].equals("0101")) newsType = "정치";
					else if(sections[s].equals("0102")) newsType = "경제";
					else if(sections[s].equals("0103")) newsType = "사회";
					else if(sections[s].equals("0106")) newsType = "문화";
					else newsType = "기타";
					elNewsTitle = docNewsCon.select("div#czone div#zone1 div.article_tit").first();
					if(elNewsTitle == null) {
						error++;	//에러 카운트
						continue;
					}
					newsTitle = elNewsTitle.text().trim();
//					newsTitle = newsTitle.replaceAll("'", "\"");	//DB 삽입 시 에러방지
					elNewsTime = docNewsCon.select("div#czone div#zone1 div.article_wrap div.extra_info").first();
					if(elNewsTime == null) {
						error++;	//에러 카운트
						continue;
					}
					uploadTime = elNewsTime.text();
					uploadTime = uploadTime.substring(uploadTime.indexOf("Posted :")+8).trim();
					if(uploadTime.substring(0,10).equals(todayDate) == false) {
						go = false;
						break; 
					}
	
					String[] newsData = {"YTN",newsType,newsTitle,url,uploadTime};
					
					ytnNews.add(newsData);
					success++;
				}
				if(i == 50) break;	//ytn은 기사목록 50페이지까지만 노출
			}
		}
		int[] cnt = {success,error};
		setYtnCnt(cnt);
		return ytnNews;
	}
	
	//세계일보
	public ArrayList<String[]> segye() throws Exception {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = dayTimeForURL.format(time);
		
		//정치,경제,사회,문화,사설
		String SegyeTotalNews1 = "http://www.segye.com/newsList/";
		String SegyeTotalNews2 = "";
		String SegyeTotalNews3 = "?curPage=";
		Document docSegye;
		Element newsGroupSegye;
		Elements newsLinksSegye;
		
		Document docNewsCon;
		Element elNewsTitle;
		Element elNewsTime;
		String newsType = "";
		String newsTitle;
		String url;
		String uploadTime = "";
		
		ArrayList<String[]> segyeNews = new ArrayList<String[]>();
		String[] sections = {"0101010000000","0101030000000","0101080000000","0101050000000","0101100300000"};

		int success = 0;
		int error = 0;
		for(int s = 0; s < sections.length; s++) {
			SegyeTotalNews2 = SegyeTotalNews1 + sections[s];
			boolean go = true;
			int i = 0;
			while(true){
				if(go == false) break;		
				i++;
				docSegye = Jsoup.connect(SegyeTotalNews2+SegyeTotalNews3+i).get();
				newsGroupSegye = docSegye.select("div#content div.newslist_area div.bd").first();
				newsLinksSegye = newsGroupSegye.select("div.area_box dl.r_txt dt.title_cr a");
	
				for(Element linkSegye : newsLinksSegye) {
					url = linkSegye.attr("abs:href").trim();
					docNewsCon = Jsoup.connect(url).get();
					if(sections[s].equals("0101010000000")) newsType = "정치";
					else if(sections[s].equals("0101030000000")) newsType = "경제";
					else if(sections[s].equals("0101080000000")) newsType = "사회";
					else if(sections[s].equals("0101050000000")) newsType = "문화";
					else if(sections[s].equals("0101100300000")) newsType = "사설";
					else newsType= "기타";
					elNewsTitle = docNewsCon.select("div#content div.news_article div.article_head div.subject h1.headline").first();
					if(elNewsTitle == null) {
						error++;	//에러 카운트
						continue;
					}
					newsTitle = elNewsTitle.text().trim();
//					newsTitle = newsTitle.replaceAll("'", "\"");	//DB 삽입 시 에러방지
					elNewsTime = docNewsCon.select("div#content div.news_article div.article_head div.clearfx div.data").first();
					if(elNewsTime == null) {
						error++;	//에러 카운트
						continue;
					}
					uploadTime = elNewsTime.text();
					uploadTime = uploadTime.substring(uploadTime.indexOf("수정 :")+4).trim();
					try {
					uploadTime = uploadTime.substring(0,16);
					}catch(Exception e) {
						uploadTime = todayDate;
					}
					if(uploadTime.substring(0,10).equals(todayDate) == false) {
						go = false;
						break; 
					}
	
					String[] newsData = {"세계일보",newsType,newsTitle,url,uploadTime};

					segyeNews.add(newsData);
					success++;
				}
			}
		}	
		int[] cnt = {success,error};
		setSegyeCnt(cnt);	
		return segyeNews;
	}
	
	//한겨례
	public ArrayList<String[]> hangyeorye() throws Exception {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTimeForURL = new SimpleDateFormat("yyyy-MM-dd");
		String todayDate = dayTimeForURL.format(time);
		
		//정치,경제,사회,문화,사설
		String HangyeoryeTotalNews1 = "http://www.hani.co.kr/arti/";
		String HangyeoryeTotalNews2 = "";
		String HangyeoryeTotalNews3 = "/list";
		String HangyeoryeTotalNews4 = ".html";
		Document docHangyeorye;
		Element newsGroupHangyeorye;
		Elements newsLinksHangyeorye;
		
		Document docNewsCon;
		Element elNewsTitle;
		Element elNewsTime;
		String newsType = "";
		String newsTitle;
		String url;
		String uploadTime = "";
		
		ArrayList<String[]> hangyeoryeNews = new ArrayList<String[]>();
		String[] sections = {"politics/politics_general","economy/economy_general","society/society_general","culture/culture_general","opinion/editorial","sports/sports_general"};

		int success = 0;
		int error = 0;
		for(int s = 0; s < sections.length; s++) {
			HangyeoryeTotalNews2 = HangyeoryeTotalNews1 + sections[s];
			boolean go = true;
			int i = 0;
			while(true){
				if(go == false) break;		
				i++;
				docHangyeorye = Jsoup.connect(HangyeoryeTotalNews2+HangyeoryeTotalNews3+i+HangyeoryeTotalNews4).get();
				newsGroupHangyeorye = docHangyeorye.select("div.section-left div#section-left-scroll-start div#section-left-scroll-in div.section-list-area").first();
				newsLinksHangyeorye = newsGroupHangyeorye.select("div.list div.article-area h4.article-title a");
	
				for(Element linkHangyeorye : newsLinksHangyeorye) {
					url = linkHangyeorye.attr("abs:href").trim();
					docNewsCon = Jsoup.connect(url).get();
					if(sections[s].equals("politics/politics_general")) newsType = "정치";
					else if(sections[s].equals("economy/economy_general")) newsType = "경제";
					else if(sections[s].equals("society/society_general")) newsType = "사회";
					else if(sections[s].equals("culture/culture_general")) newsType = "문화";
					else if(sections[s].equals("opinion/editorial")) newsType = "사설";
					else if(sections[s].equals("sports/sports_general")) newsType = "스포츠";
					else newsType = "기타";
					elNewsTitle = docNewsCon.select("div.article-head h4 span.title").first();
					if(elNewsTitle == null) {
						error++;	//에러 카운트
						continue;
					}
					newsTitle = elNewsTitle.text().trim();
//					newsTitle = newsTitle.replaceAll("'", "\"");	//DB 삽입 시 에러방지
					elNewsTime = docNewsCon.select("div.article-head p.date-time").first();
					if(elNewsTime == null) {
						error++;	//에러 카운트
						continue;
					}
					uploadTime = elNewsTime.text();
					uploadTime = uploadTime.trim();
					uploadTime = uploadTime.substring(uploadTime.indexOf("등록 :")+4).trim();
					if(uploadTime.indexOf("수정 :") != -1) uploadTime = uploadTime.substring(uploadTime.indexOf("수정 :")+4).trim();
					if(uploadTime.substring(0,10).equals(todayDate) == false) {
						go = false;
						break; 
					}
	
					String[] newsData = {"한겨례",newsType,newsTitle,url,uploadTime};

					hangyeoryeNews.add(newsData);
					success++;
				}
			}
		}
		int[] cnt = {success,error};
		setHangyeoryeCnt(cnt);	
		return hangyeoryeNews;
	}
	
	private String typeConsistency(String type) {
		String convertedType = "";
		if(type.equals("정치") || type.equals("경제") || type.equals("사회") || type.equals("문화") || type.equals("오피니언")) convertedType = type;
		else {
			if(type.equals("사고")) convertedType = "사회";
			else if(type.contains("사설") || type.contains("컬럼")) convertedType = "오피니언";
			else if(type.contains("스포츠") && type.contains("연예")) convertedType = "스포츠ㆍ연예";
			else convertedType = "기타";
			//새로운 카테고리 발견 시 추가할 것
		}
		
		return convertedType;
	}
	
}

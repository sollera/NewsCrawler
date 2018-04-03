package com.crawl.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Bot_R {
	
	Crawler_R crawler = new Crawler_R();

	private ScheduledThreadPoolExecutor exec1;	//조선일보 쓰레드  ,  주기적인 작업 실행(linux에서 daemon 돌리듯이..)
	private ScheduledThreadPoolExecutor exec2;	//동아일보
	private ScheduledThreadPoolExecutor exec3;	//서울신문
	private ScheduledThreadPoolExecutor exec4;	//YTN
	private ScheduledThreadPoolExecutor exec5;	//세계일보
	private ScheduledThreadPoolExecutor exec6;	//한겨례
	
	
	public void crawlingBot(int sleepSec) {
		
		if(sleepSec == 0) {	//크롤링 서버 종료
			
    		execEnd();	//쓰레드 강제 종료
    		
    		btnCtrl(0);
    		crawlerStop();
    		
    	}else {	//크롤링 서버 구동
    		
    		execEnd();	//크롤러가 돌아가는 중인데 다시 돌릴 경우를 대비한 쓰레드 강제 종료
    		execStart();	//쓰레드 초기화
    		
    		statusTbCheck();
    		
    		btnCtrl(1);
    		crawlerStart(sleepSec);	//크롤러 작동 시작(지정한 주기로 무한반복)
    		
    	}
		
	}
	
	//쓰레드풀 할당
	private void execStart() {
		exec1 = new ScheduledThreadPoolExecutor(1);
		exec2 = new ScheduledThreadPoolExecutor(1);
		exec3 = new ScheduledThreadPoolExecutor(1);
		exec4 = new ScheduledThreadPoolExecutor(1);
		exec5 = new ScheduledThreadPoolExecutor(1);
		exec6 = new ScheduledThreadPoolExecutor(1);
    }
    
	//쓰레드풀 끝내기 - 크롤러 중지
    private void execEnd() {
    	if(exec1 != null) exec1.shutdownNow();
    	if(exec2 != null) exec2.shutdownNow();
    	if(exec3 != null) exec3.shutdownNow();
    	if(exec4 != null) exec4.shutdownNow();
    	if(exec5 != null) exec5.shutdownNow();
    	if(exec6 != null) exec6.shutdownNow();
    }
    
    //크롤러 시작
    private void crawlerStart(int sleepSec) {
    	exec1.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//조선일보
                	//jdbc 연결
                	statusUpdate("chosun",1);
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,type,title,newsURL,enrollDT) VALUES(?,?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> chosun = crawler.chosun();
            		String category = "";
                	for(int i = 0; i < chosun.size(); i++) {
                		stmt.setString(1, chosun.get(i)[0]);
                		
                		if(chosun.get(i)[1].equals("society")) category = "사회";
                		else if(chosun.get(i)[1].equals("politics")) category = "정치";
                		else if(chosun.get(i)[1].equals("economy")) category = "경제";
                		else if(chosun.get(i)[1].equals("sport")) category = "스포츠";
                		else if(chosun.get(i)[1].equals("entertainment")) category = "연예";
                		else if(chosun.get(i)[1].equals("사설")) category = "오피니언";
                		else if(chosun.get(i)[1].equals("IT/의학")) category = "사회";
                		else if(chosun.get(i)[1].equals("사람속으로")) category = "사회";
                		else if(chosun.get(i)[1].equals("")) category = "기타";
                		else category = chosun.get(i)[1];
                		
                		stmt.setString(2, category);
                		stmt.setString(3, chosun.get(i)[2]);
                		stmt.setString(4, chosun.get(i)[3]);
                		stmt.setString(5, chosun.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
                	conn.close();

        			statusUpdate("chosun",2);
                	errHistory(true,"조선일보");
                	updateTime("조선일보");
                	
                	updateTimeLog("조선일보",crawler.getChosunCnt()[0]);
                	
                }catch (Exception e) {
                	statusUpdate("chosun",0);
                	errHistory(false,"조선일보");
            		errLog("조선일보",e.toString());
                	
                    System.out.println("조선일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    	exec2.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//동아일보
                	//jdbc 연결
                	statusUpdate("donga",1);
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,type,title,newsURL,enrollDT) VALUES(?,?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> donga = crawler.donga();
            		String category = "";
                	for(int i = 0; i < donga.size(); i++) {
                		stmt.setString(1, donga.get(i)[0]);
                		
                		if(donga.get(i)[1].equals("society")) category = "사회";
                		else if(donga.get(i)[1].equals("politics")) category = "정치";
                		else if(donga.get(i)[1].equals("economy")) category = "경제";
                		else if(donga.get(i)[1].equals("sport")) category = "스포츠";
                		else if(donga.get(i)[1].equals("entertainment")) category = "연예";
                		else if(donga.get(i)[1].equals("사설")) category = "오피니언";
                		else if(donga.get(i)[1].equals("IT/의학")) category = "사회";
                		else if(donga.get(i)[1].equals("사람속으로")) category = "사회";
                		else if(donga.get(i)[1].equals("")) category = "기타";
                		else category = donga.get(i)[1];
                		
                		stmt.setString(2, category);
                		stmt.setString(3, donga.get(i)[2]);
                		stmt.setString(4, donga.get(i)[3]);
                		stmt.setString(5, donga.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
                	conn.close();

        			statusUpdate("donga",2);
                	errHistory(true,"동아일보");
                	updateTime("동아일보");

                	updateTimeLog("동아일보",crawler.getDongaCnt()[0]);
                	
                }catch (Exception e) {
                	statusUpdate("donga",0);
                	errHistory(false,"동아일보");
            		errLog("동아일보",e.toString());
                	
                    System.out.println("동아일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    	exec3.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//서울신문
                	//jdbc 연결
                	statusUpdate("seoul",1);
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,type,title,newsURL,enrollDT) VALUES(?,?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> seoul = crawler.seoul();
            		String category = "";
                	for(int i = 0; i < seoul.size(); i++) {
                		stmt.setString(1, seoul.get(i)[0]);
                		
                		if(seoul.get(i)[1].equals("society")) category = "사회";
                		else if(seoul.get(i)[1].equals("politics")) category = "정치";
                		else if(seoul.get(i)[1].equals("economy")) category = "경제";
                		else if(seoul.get(i)[1].equals("sport")) category = "스포츠";
                		else if(seoul.get(i)[1].equals("entertainment")) category = "연예";
                		else if(seoul.get(i)[1].equals("사설")) category = "오피니언";
                		else if(seoul.get(i)[1].equals("IT/의학")) category = "사회";
                		else if(seoul.get(i)[1].equals("사람속으로")) category = "사회";
                		else if(seoul.get(i)[1].equals("")) category = "기타";
                		else category = seoul.get(i)[1];
                		
                		stmt.setString(2, category);
                		stmt.setString(3, seoul.get(i)[2]);
                		stmt.setString(4, seoul.get(i)[3]);
                		stmt.setString(5, seoul.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
                	conn.close();

        			statusUpdate("seoul",2);
                	errHistory(true,"서울신문");
                	updateTime("서울신문");

                	updateTimeLog("서울신문",crawler.getSeoulCnt()[0]);
                	
                }catch (Exception e) {
                	statusUpdate("seoul",0);
                	errHistory(false,"서울신문");
            		errLog("서울신문",e.toString());
                	
                    System.out.println("서울신문 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    	exec4.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//YTN
                	//jdbc 연결
                	statusUpdate("ytn",1);
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,type,title,newsURL,enrollDT) VALUES(?,?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> ytn = crawler.ytn();
            		String category = "";
                	for(int i = 0; i < ytn.size(); i++) {
                		stmt.setString(1, ytn.get(i)[0]);
                		
                		if(ytn.get(i)[1].equals("society")) category = "사회";
                		else if(ytn.get(i)[1].equals("politics")) category = "정치";
                		else if(ytn.get(i)[1].equals("economy")) category = "경제";
                		else if(ytn.get(i)[1].equals("sport")) category = "스포츠";
                		else if(ytn.get(i)[1].equals("entertainment")) category = "연예";
                		else if(ytn.get(i)[1].equals("사설")) category = "오피니언";
                		else if(ytn.get(i)[1].equals("IT/의학")) category = "사회";
                		else if(ytn.get(i)[1].equals("사람속으로")) category = "사회";
                		else if(ytn.get(i)[1].equals("")) category = "기타";
                		else category = ytn.get(i)[1];
                		
                		stmt.setString(2, category);
                		stmt.setString(3, ytn.get(i)[2]);
                		stmt.setString(4, ytn.get(i)[3]);
                		stmt.setString(5, ytn.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
                	conn.close();

        			statusUpdate("ytn",2);
                	errHistory(true,"YTN");
                	updateTime("YTN");

                	updateTimeLog("YTN",crawler.getYtnCnt()[0]);
                	
                }catch (Exception e) {
                	statusUpdate("ytn",0);
                	errHistory(false,"YTN");
            		errLog("YTN",e.toString());
                	
                    System.out.println("YTN Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    	exec5.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//세계일보
                	//jdbc 연결
                	statusUpdate("segye",1);
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	statusUpdate("segye",1);
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,type,title,newsURL,enrollDT) VALUES(?,?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> segye = crawler.segye();
            		String category = "";
                	for(int i = 0; i < segye.size(); i++) {
                		stmt.setString(1, segye.get(i)[0]);
                		
                		if(segye.get(i)[1].equals("society")) category = "사회";
                		else if(segye.get(i)[1].equals("politics")) category = "정치";
                		else if(segye.get(i)[1].equals("economy")) category = "경제";
                		else if(segye.get(i)[1].equals("sport")) category = "스포츠";
                		else if(segye.get(i)[1].equals("entertainment")) category = "연예";
                		else if(segye.get(i)[1].equals("사설")) category = "오피니언";
                		else if(segye.get(i)[1].equals("IT/의학")) category = "사회";
                		else if(segye.get(i)[1].equals("사람속으로")) category = "사회";
                		else if(segye.get(i)[1].equals("")) category = "기타";
                		else category = segye.get(i)[1];
                		
                		stmt.setString(2, category);
                		stmt.setString(3, segye.get(i)[2]);
                		stmt.setString(4, segye.get(i)[3]);
                		stmt.setString(5, segye.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
                	conn.close();

        			statusUpdate("segye",2);
                	errHistory(true,"세계일보");
                	updateTime("세계일보");

                	updateTimeLog("세계일보",crawler.getSegyeCnt()[0]);

                }catch (Exception e) {
                	statusUpdate("segye",0);
                	errHistory(false,"세계일보");
            		errLog("세계일보",e.toString());
                	
                    System.out.println("세계일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    	exec6.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//한겨례
                	//jdbc 연결
                	statusUpdate("hangyeorye",1);
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,type,title,newsURL,enrollDT) VALUES(?,?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> hangyeorye = crawler.hangyeorye();
            		String category = "";
                	for(int i = 0; i < hangyeorye.size(); i++) {
                		stmt.setString(1, hangyeorye.get(i)[0]);
                		
                		if(hangyeorye.get(i)[1].equals("society")) category = "사회";
                		else if(hangyeorye.get(i)[1].equals("politics")) category = "정치";
                		else if(hangyeorye.get(i)[1].equals("economy")) category = "경제";
                		else if(hangyeorye.get(i)[1].equals("sport")) category = "스포츠";
                		else if(hangyeorye.get(i)[1].equals("entertainment")) category = "연예";
                		else if(hangyeorye.get(i)[1].equals("사설")) category = "오피니언";
                		else if(hangyeorye.get(i)[1].equals("IT/의학")) category = "사회";
                		else if(hangyeorye.get(i)[1].equals("사람속으로")) category = "사회";
                		else if(hangyeorye.get(i)[1].equals("")) category = "기타";
                		else category = hangyeorye.get(i)[1];
                		
                		stmt.setString(2, category);
                		stmt.setString(3, hangyeorye.get(i)[2]);
                		stmt.setString(4, hangyeorye.get(i)[3]);
                		stmt.setString(5, hangyeorye.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
                	conn.close();

                	statusUpdate("hangyeorye",2);
                	errHistory(true,"한겨례");
                	updateTime("한겨례");

                	updateTimeLog("한겨례",crawler.getHangyeoryeCnt()[0]);

                }catch (Exception e) {
                	statusUpdate("hangyeorye",0);
                	errHistory(false,"한겨례");
            		errLog("한겨례",e.toString());
                	
                    System.out.println("한겨례 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    }
    
    //크롤러 중지를 DB에 저장
    private void crawlerStop() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	try {
	    	Connection conn2 = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
			Statement stmt2 = conn2.createStatement();
	    	stmt2.executeUpdate("UPDATE statusNews SET chosun=-1,donga=-1,seoul=-1,ytn=-1,segye=-1,hangyeorye=-1,modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
	    	stmt2.close();
	    	conn2.close();
    	}catch(Exception eEnd) {
    		eEnd.printStackTrace();
    	}
    }
    
    //크롤러 작동 시작,완료,에러를 DB에 저장
    private void statusUpdate(String site,int status) {
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.executeUpdate("UPDATE statusNews SET "+site+"="+status);	//크롤링 수행 중 문제 발생을 알리기 위해 DB 정보 수정(0이면 크롤링 실패 혹은 수행 전이라는 뜻)
	    	stmt1.close();
	    	conn.close();
	    	
	    	timeChk(site, status);
	    	
    	}catch(Exception e) {
    		errLog(site,e.toString());
    	}
    }
    
    //크롤러 동작 시간 DB에 저장
    private void timeChk(String site,int status) {
    	String site1 = "";
    	if(site.equals("chosun")) site1 = "조선일보";
    	if(site.equals("donga")) site1 = "동아일보";
    	if(site.equals("seoul")) site1 = "서울신문";
    	if(site.equals("ytn")) site1 = "YTN";
    	if(site.equals("segye")) site1 = "세계일보";
    	if(site.equals("hangyeorye")) site1 = "한겨례";
    	
    	long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt = conn.createStatement();
    		Statement stmt1 = conn.createStatement();
    		ResultSet rs = null;
    		if(status == 1) {
    			stmt1.executeUpdate("UPDATE operatingTime SET startTime='"+dayTime.format(new Date(time)).toString()+"' WHERE site='"+site1+"'");
    		}else if(status == 2) {
    			stmt1.executeUpdate("UPDATE operatingTime SET endTime='"+dayTime.format(new Date(time)).toString()+"' WHERE site='"+site1+"'");
    			rs = stmt.executeQuery("SELECT startTime,endTime FROM operatingTime WHERE site='"+site1+"'");
    			long last = 0;
    			long first = 0;
    			if(rs.next()) {
    				last = rs.getTimestamp(2).getTime();
    				first = rs.getTimestamp(1).getTime();
    			}
    			int spend = (int) ((last-first) / 1000);
    			stmt1.executeUpdate("UPDATE operatingTime SET spendTime="+spend+",sumTime=(sumtime+"+spend+"),cnt=(cnt+1) WHERE site='"+site1+"'");
            	rs.close();
    		}else if(status == 0) {
    			stmt1.executeUpdate("UPDATE operatingTime SET endTime='"+dayTime.format(new Date(time)).toString()+"',spendTime=0 WHERE site='"+site1+"'");
    		}
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception eEnd) {
    		eEnd.printStackTrace();
    	}
    }
    
    //크롤링 완료 시간 DB에 저장
    private void updateTime(String site) {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");	//크롤링 시간 업데이트
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		errLog(site,e.toString());
    	}
    }
    
    //에러 메세지 저장
    private void errLog(String site,String errLog) {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.execute("INSERT INTO errLog(errTime,site,error) VALUES('"+dayTime.format(new Date(time)).toString()+"','"+site+"','"+errLog+"')");	//에러 로그 저장
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
    //크롤러 상태 테이블에 레코드가 한줄 도 없을 때 기본값 삽입
    private void statusTbCheck() {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * from statusNews");
    		if(rs.next() == false) {
	    		Statement stmt1 = conn.createStatement();
	        	stmt1.execute("INSERT INTO statusNews(chosun,donga,seoul,ytn,segye,hangyeorye,modifyTime) "
	        			+ "VALUES(-1,-1,-1,-1,-1,-1,'"+dayTime.format(new Date(time)).toString()+"')");	//크롤링 상태 테이블 레코드 없을 시 추가
		    	stmt1.close();
    		}
	    	rs.close();
	    	stmt.close();
	    	conn.close();
    	}catch(Exception e) {}
    }
    
    //새로 수집한 기사 수 저장
    private void updateTimeLog(String site, int target) {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	SimpleDateFormat dayTime1 = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt = conn.createStatement();
    		String query = "SELECT count(title) FROM news WHERE site='"+site+"'";
    		ResultSet rs = stmt.executeQuery(query);
    		if(rs.next()) {
				int cnt = rs.getInt(1);
				Statement stmt1 = conn.createStatement();
				Statement stmt2 = conn.createStatement();
				Statement stmt3 = conn.createStatement();
				ResultSet rs1 = stmt2.executeQuery("SELECT allData,chkTime FROM searchNews WHERE site='"+site+"' and lastDataChk=1 LIMIT 1");
				int cnt1 = 0;
				String date = "";
				String today = dayTime1.format(new Date(time)).toString();
				if(rs1.next()) {
					cnt1 = rs1.getInt(1);
					date = rs1.getString(2).trim().substring(0,10).trim();
	    			System.out.println("*********************************************************크롤링 전까지 기존 기사 수 체크 "+site+"-"+cnt1);
				}
				stmt3.executeUpdate("UPDATE searchNews SET lastDataChk=0 WHERE site='"+site+"'");
				if(date.equals(today)) {
					stmt1.execute("INSERT INTO searchNews(site,target,newData,allData,chkTime) VALUES('"+site+"',"+target+","+(cnt-cnt1)+","+cnt+",'"+dayTime.format(new Date(time)).toString()+"')");
				}else {
					stmt1.execute("INSERT INTO searchNews(site,target,newData,allData,chkTime) VALUES('"+site+"',"+target+","+cnt+","+cnt+",'"+dayTime.format(new Date(time)).toString()+"')");
				}
				rs1.close();
				stmt3.close();
				stmt2.close();
				stmt1.close();
    		}
	    	rs.close();
	    	stmt.close();
	    	conn.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private void btnCtrl(int power) {
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt = conn.createStatement();
    		String query = "UPDATE crawlerPowerCheck SET power=";
    		stmt.executeUpdate(query+power);
	    	stmt.close();
	    	conn.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void errHistory(boolean a,String site) {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT * FROM errCnt WHERE dt = '"+dayTime.format(new Date(time)).toString()+"' and site='"+site+"'");
    		Statement stmt1 = conn.createStatement();
    		if(a == true) {	//정상 작동 카운트
    			if(rs.next()) {
        			stmt1.execute("UPDATE errCnt SET success=(success+1) WHERE dt = '"+dayTime.format(new Date(time)).toString()+"' and site='"+site+"'");
        		}else {
        			stmt1.execute("INSERT INTO errCnt(site,success,error,dt) VALUES('"+site+"',1,0,'"+dayTime.format(new Date(time)).toString()+"')");
        		}
        	}else if(a == false) {	//에러 발생 카운트
        		if(rs.next()) {
        			stmt1.execute("UPDATE errCnt SET error=(error+1) WHERE dt = '"+dayTime.format(new Date(time)).toString()+"' and site='"+site+"'");
        		}else {
        			stmt1.execute("INSERT INTO errCnt(site,success,error,dt) VALUES('"+site+"',0,1,'"+dayTime.format(new Date(time)).toString()+"')");
        		}
        	}
    		rs.close();
	    	stmt.close();
    		stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
}

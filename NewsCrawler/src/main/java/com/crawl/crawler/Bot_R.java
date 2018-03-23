package com.crawl.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
			System.out.println("끄러 오긴 했음.");
    		execEnd();	//쓰레드 강제 종료
    		
    		crawlerStop();
    		System.out.println("끄고 갔음.");
    	}else {	//크롤링 서버 구동
    		
    		execEnd();	//크롤러가 돌아가는 중인데 다시 돌릴 경우를 대비한 쓰레드 강제 종료
        	
    		execStart();	//쓰레드 초기화
    		crawlerStart(sleepSec);	//크롤러 작동 시작(지정한 주기로 무한반복)
    		
    	}
		
	}
	
	private void execStart() {
		exec1 = new ScheduledThreadPoolExecutor(1);
		exec2 = new ScheduledThreadPoolExecutor(1);
		exec3 = new ScheduledThreadPoolExecutor(1);
		exec4 = new ScheduledThreadPoolExecutor(1);
		exec5 = new ScheduledThreadPoolExecutor(1);
		exec6 = new ScheduledThreadPoolExecutor(1);
    }
    
    private void execEnd() {
    	if(exec1 != null) exec1.shutdownNow();
    	if(exec2 != null) exec2.shutdownNow();
    	if(exec3 != null) exec3.shutdownNow();
    	if(exec4 != null) exec4.shutdownNow();
    	if(exec5 != null) exec5.shutdownNow();
    	if(exec6 != null) exec6.shutdownNow();
    }
    
    
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
                		else if(chosun.get(i)[1].equals("")) System.out.println();
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
                	updateTime("조선일보");
                	
                	errCntUpdate("조선일보",crawler.getChosunCnt()[0],crawler.getChosunCnt()[1]);
                	
                }catch (Exception e) {
                	statusUpdate("chosun",0);
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
                		else if(donga.get(i)[1].equals("")) System.out.println();
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
                	updateTime("동아일보");
                	
                	errCntUpdate("동아일보",crawler.getDongaCnt()[0],crawler.getDongaCnt()[1]);
                	
                }catch (Exception e) {
                	statusUpdate("donga",0);
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
                		else if(seoul.get(i)[1].equals("")) System.out.println();
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
                	updateTime("서울신문");
                	
                	errCntUpdate("서울신문",crawler.getSeoulCnt()[0],crawler.getSeoulCnt()[1]);
                	
                }catch (Exception e) {
                	statusUpdate("seoul",0);
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
                		else if(ytn.get(i)[1].equals("")) System.out.println();
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
                	updateTime("YTN");
                	
                	errCntUpdate("YTN",crawler.getYtnCnt()[0],crawler.getYtnCnt()[1]);
                	
                }catch (Exception e) {
                	statusUpdate("ytn",0);
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
                		else if(segye.get(i)[1].equals("")) System.out.println();
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
                	updateTime("세계일보");
                	
                	errCntUpdate("세계일보",crawler.getSegyeCnt()[0],crawler.getSegyeCnt()[1]);

                }catch (Exception e) {
                	statusUpdate("segye",0);
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
                		else if(hangyeorye.get(i)[1].equals("")) System.out.println();
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
                	updateTime("한겨례");
                	
                	errCntUpdate("한겨례",crawler.getHangyeoryeCnt()[0],crawler.getHangyeoryeCnt()[1]);

                }catch (Exception e) {
                	statusUpdate("hangyeorye",0);
            		errLog("한겨례",e.toString());
                	
                    System.out.println("한겨례 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    }
    
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
    
    private void statusUpdate(String site,int status) {
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.executeUpdate("UPDATE statusNews SET "+site+"="+status);	//크롤링 수행 중 문제 발생을 알리기 위해 DB 정보 수정(0이면 크롤링 실패 혹은 수행 전이라는 뜻)
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		errLog(site,e.toString());
    	}
    }
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
    
    private void errCntUpdate(String site,int success,int error) {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.execute("INSERT INTO crawlingLog(site,success,error,successAcc,errorAcc,dt)"
        			+ " VALUES('"+site+"',"+success+","+error+",0,0,'"+dayTime.format(new Date(time)).toString()+"')"
        			+ " ON DUPLICATE KEY UPDATE"
        			+ " success="+success+",error="+error+",successAcc=(successAcc+"+success+"),errorAcc=(errorAcc+"+error+")");	//크롤링 성공/실패 업데이트, 없으면 추가
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		errLog(site,e.toString());
    	}
    }
    
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
    
}

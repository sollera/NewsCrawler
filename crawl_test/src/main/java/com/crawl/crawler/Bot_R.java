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
    		
    		execEnd();	//쓰레드 강제 종료
        	
    		crawlerStop();
    		
    	}else {	//크롤링 서버 구동
    		
    		if(exec1 != null) execEnd();	//크롤러가 돌아가는 중인데 다시 돌릴 경우를 대비한 쓰레드 강제 종료
        	
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
    	exec1.shutdownNow();
    	exec2.shutdownNow();
    	exec3.shutdownNow();
    	exec4.shutdownNow();
    	exec5.shutdownNow();
    	exec6.shutdownNow();
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
                	for(int i = 0; i < chosun.size(); i++) {
                		stmt.setString(1, chosun.get(i)[0]);
                		stmt.setString(2, chosun.get(i)[1]);
                		stmt.setString(3, chosun.get(i)[2]);
                		stmt.setString(4, chosun.get(i)[3]);
                		stmt.setString(5, chosun.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			statusUpdate("chosun",2);
                	updateTime();
                	
                	conn.close();
//                	System.out.println("조선일보 : "+crawler.getChosunCnt()[0]+"/"+crawler.getChosunCnt()[1]);
                }catch (Exception e) {
                	statusUpdate("chosun",0);
                	
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
                	for(int i = 0; i < donga.size(); i++) {
                		stmt.setString(1, donga.get(i)[0]);
                		stmt.setString(2, donga.get(i)[1]);
                		stmt.setString(3, donga.get(i)[2]);
                		stmt.setString(4, donga.get(i)[3]);
                		stmt.setString(5, donga.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			statusUpdate("donga",2);
                	updateTime();
                	
                	conn.close();
//                	System.out.println("동아일보 : "+crawler.getDongaCnt()[0]+"/"+crawler.getDongaCnt()[1]);
                }catch (Exception e) {
                	statusUpdate("donga",0);
                	
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
                	for(int i = 0; i < seoul.size(); i++) {
                		stmt.setString(1, seoul.get(i)[0]);
                		stmt.setString(2, seoul.get(i)[1]);
                		stmt.setString(3, seoul.get(i)[2]);
                		stmt.setString(4, seoul.get(i)[3]);
                		stmt.setString(5, seoul.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			statusUpdate("seoul",2);
                	updateTime();
                	
                	conn.close();
//                	System.out.println("서울신문 : "+crawler.getSeoulCnt()[0]+"/"+crawler.getSeoulCnt()[1]);
                }catch (Exception e) {
                	statusUpdate("seoul",0);
                	
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
                	for(int i = 0; i < ytn.size(); i++) {
                		stmt.setString(1, ytn.get(i)[0]);
                		stmt.setString(2, ytn.get(i)[1]);
                		stmt.setString(3, ytn.get(i)[2]);
                		stmt.setString(4, ytn.get(i)[3]);
                		stmt.setString(5, ytn.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			statusUpdate("ytn",2);
                	updateTime();
                	
                	conn.close();
//                	System.out.println("YTN : "+crawler.getYtnCnt()[0]+"/"+crawler.getYtnCnt()[1]);
                }catch (Exception e) {
                	statusUpdate("ytn",0);
                	
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
                	for(int i = 0; i < segye.size(); i++) {
                		stmt.setString(1, segye.get(i)[0]);
                		stmt.setString(2, segye.get(i)[1]);
                		stmt.setString(3, segye.get(i)[2]);
                		stmt.setString(4, segye.get(i)[3]);
                		stmt.setString(5, segye.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			statusUpdate("segye",2);
                	updateTime();
                	
                	conn.close();
//                	System.out.println("세계일보 : "+crawler.getSegyeCnt()[0]+"/"+crawler.getSegyeCnt()[1]);
                }catch (Exception e) {
                	statusUpdate("segye",0);
                	
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
                	for(int i = 0; i < hangyeorye.size(); i++) {
                		stmt.setString(1, hangyeorye.get(i)[0]);
                		stmt.setString(2, hangyeorye.get(i)[1]);
                		stmt.setString(3, hangyeorye.get(i)[2]);
                		stmt.setString(4, hangyeorye.get(i)[3]);
                		stmt.setString(5, hangyeorye.get(i)[4]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

                	statusUpdate("hangyeorye",2);
                	updateTime();
                	
                	conn.close();
                	System.out.println("한겨례 : "+crawler.getHangyeoryeCnt()[0]+"/"+crawler.getHangyeoryeCnt()[1]);
                }catch (Exception e) {
                	statusUpdate("hangyeorye",0);
                	
                    System.out.println("한겨례 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    	
    }
    
    private void crawlerStop() {
		long time = System.currentTimeMillis();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
    	try {
	    	Connection conn2 = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
			Statement stmt2 = conn2.createStatement();
	    	stmt2.executeUpdate("UPDATE statusNews SET chosun=0,donga=0,seoul=0,ytn=0,segye=0,hangyeorye=0,modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
	    	stmt2.close();
	    	conn2.close();
    	}catch(Exception eEnd) {eEnd.printStackTrace();}
    }
    
    private void statusUpdate(String site,int status) {
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.executeUpdate("UPDATE statusNews SET "+site+"="+status);	//크롤링 수행 중 문제 발생을 알리기 위해 DB 정보 수정(0이면 크롤링 실패 혹은 수행 전이라는 뜻)
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
    private void updateTime() {
    	long time = System.currentTimeMillis();
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
    	try {
    		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
    		Statement stmt1 = conn.createStatement();
        	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");	//크롤링 시간 업데이트
	    	stmt1.close();
	    	conn.close();
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
}

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




public class NewsBot {
	
	Crawler crawler = new Crawler();

	private ScheduledThreadPoolExecutor exec;	 // 주기적인 작업 실행(linux에서 daemon 돌리듯이..), 조선일보 쓰레드
	private ScheduledThreadPoolExecutor exec1;	 // 동아일보 쓰레드
	private ScheduledThreadPoolExecutor exec2;	 // 서울신문 쓰레드
	private ScheduledThreadPoolExecutor exec3;	 // YTN 쓰레드
	private ScheduledThreadPoolExecutor exec4;	 // 세계일보 쓰레드
	private ScheduledThreadPoolExecutor exec5;	 // NewDaily 쓰레드
	
	private long time = System.currentTimeMillis(); 
	private SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/MM/dd kk:mm");
    
    public void crawlingBot(int sleepSec) {
    	
    	if(sleepSec == 0) {	//크롤링 서버 종료
    		
    		execEnd();	//쓰레드 강제 종료
        	
    		crawlerStop();	//크롤러 종료 서버에 반영
    		
    	}else {	//크롤링 서버 구동
    		
    		if(exec != null) execEnd();	//쓰레드 강제 종료
        	
    		execStart();	//쓰레드 초기화
        	
    		//크롤러 동작 시작
    		chosunCrawlerStart(sleepSec);
    		
    		dongaCrawlerStart(sleepSec);
	    	
	    	seoulCrawlerStart(sleepSec);
	        
	        ytnCrawlerStart(sleepSec);
	        
	        segyeCrawlerStart(sleepSec);
	        
	        newdailyCrawlerStart(sleepSec);
	        
    	}
    }
    
    
    
    
    private void execStart() {
    	exec = new ScheduledThreadPoolExecutor(1);
    	exec1 = new ScheduledThreadPoolExecutor(1);
    	exec2 = new ScheduledThreadPoolExecutor(1);
    	exec3 = new ScheduledThreadPoolExecutor(1);
    	exec4 = new ScheduledThreadPoolExecutor(1);
    	exec5 = new ScheduledThreadPoolExecutor(1);
    }
    
    private void execEnd() {
    	exec.shutdownNow();
    	exec1.shutdownNow();
    	exec2.shutdownNow();
    	exec3.shutdownNow();
    	exec4.shutdownNow();
    	exec5.shutdownNow();
    }
    
    
    private void chosunCrawlerStart(int sleepSec) {
    	exec.scheduleAtFixedRate(new Runnable(){
            public void run() {
                try {	//조선일보
                	//jdbc 연결
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET chosun=1");	//크롤러 동작 시작을 알리기 위해 DB 정보 수정(1이면 크롤링 중이라는 뜻)
            		conn.setAutoCommit(false);	//배치 처리를 위해 오토커밋 끄기
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> chosun = crawler.chosun();
                	for(int i = 0; i < chosun.size(); i++) {
                		stmt.setString(1, chosun.get(i)[0]);
                		stmt.setString(2, chosun.get(i)[1]);
                		stmt.setString(3, chosun.get(i)[2]);
                		stmt.setString(4, chosun.get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);	//업데이트는 한건의 레코드라 편의를 위해 오토커밋 켬
                	stmt1.executeUpdate("UPDATE statusNews SET chosun=2");	//크롤링 수행 완료를 알리기 위해 DB 정보 수정(2면 크롤링 성공이라는 뜻)
                	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
                	//크롤링 수행 완료 시간 반영, 각 크롤러가 같은 필드에 데이터를 수정하기때문에 마지막으로 크롤링 된 시간이 남음.
                	stmt1.close();
                	conn.close();
                	System.out.println("Good~1");
                }catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET chosun=0");	//크롤링 수행 중 문제 발생을 알리기 위해 DB 정보 수정(0이면 크롤링 실패 혹은 수행 전이라는 뜻)
            	    	stmt1.close();
            	    	conn.close();
                	}catch(Exception e1) {}
                	
                    System.out.println("조선일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec.shutdown() ;
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);	//지정된 시간마다 크롤링 무한 반복
    }
    
    private void dongaCrawlerStart(int sleepSec) {
    	exec1.scheduleAtFixedRate(new Runnable(){
            public void run(){
                
                try {	//동아일보
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET donga=1");
            		//?useSSL=false 를 데이터베이스 이름 뒤에 쓰면 에러메시지 안뜸
            		conn.setAutoCommit(false);
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> donga = crawler.donga();
                	for(int i = 0; i < donga.size(); i++) {
                		stmt.setString(1, donga.get(i)[0]);
                		stmt.setString(2, donga.get(i)[1]);
                		stmt.setString(3, donga.get(i)[2]);
                		stmt.setString(4, donga.get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();
        			
        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET donga=2");
                	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
        	    	stmt1.close();
        	    	conn.close();
        			System.out.println("Good~2");
                }catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET donga=0");
            	    	stmt1.close();
            	    	conn.close();
                	}catch(Exception e1) {}
                	System.out.println("동아일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec1.shutdown() ;
                }            	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
    }
    
    private void seoulCrawlerStart(int sleepSec) {
    	exec2.scheduleAtFixedRate(new Runnable(){
            public void run(){
                
            	try {	//서울신문
            		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET seoul=1");
            		//?useSSL=false 를 데이터베이스 이름 뒤에 쓰면 에러메시지 안뜸
            		conn.setAutoCommit(false);
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
            		PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> seoul = crawler.seoul();
                	for(int i = 0; i < seoul.size(); i++) {
                		stmt.setString(1, seoul.get(i)[0]);
                		stmt.setString(2, seoul.get(i)[1]);
                		stmt.setString(3, seoul.get(i)[2]);
                		stmt.setString(4, seoul.get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET seoul=2");
                	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
        	    	stmt1.close();
        	    	conn.close();
        			System.out.println("Good~3");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET seoul=0");
            	    	stmt1.close();
            	    	conn.close();
                	}catch(Exception e1) {}
                	System.out.println("서울신문 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec2.shutdown() ;
                }          	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
    }
    
    private void ytnCrawlerStart(int sleepSec) {
    	exec3.scheduleAtFixedRate(new Runnable(){
            public void run(){
                
            	try {	//YTN
            		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET ytn=1");
            		//?useSSL=false 를 데이터베이스 이름 뒤에 쓰면 에러메시지 안뜸
            		conn.setAutoCommit(false);
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
            		PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> ytn = crawler.ytn();
                	for(int i = 0; i < ytn.size(); i++) {
                		stmt.setString(1, ytn.get(i)[0]);
                		stmt.setString(2, ytn.get(i)[1]);
                		stmt.setString(3, ytn.get(i)[2]);
                		stmt.setString(4, ytn.get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET ytn=2");
                	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
        	    	stmt1.close();
        	    	conn.close();
        			System.out.println("Good~4");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET ytn=0");
            	    	stmt1.close();
            	    	conn.close();
                	}catch(Exception e1) {}
                	System.out.println("YTN Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec3.shutdown() ;
                }         	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
    }
    
    private void segyeCrawlerStart(int sleepSec) {
    	exec4.scheduleAtFixedRate(new Runnable(){
            public void run(){
                
            	try {	//세계일보
            		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET segye=1");
            		//?useSSL=false 를 데이터베이스 이름 뒤에 쓰면 에러메시지 안뜸
            		conn.setAutoCommit(false);
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
            		PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> segye = crawler.segye();
                	for(int i = 0; i < segye.size(); i++) {
                		stmt.setString(1, segye.get(i)[0]);
                		stmt.setString(2, segye.get(i)[1]);
                		stmt.setString(3, segye.get(i)[2]);
                		stmt.setString(4, segye.get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET segye=2");
                	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
        	    	stmt1.close();
        	    	conn.close();
        			System.out.println("Good~5");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET segye=0");
            	    	stmt1.close();
            	    	conn.close();
                	}catch(Exception e1) {}
                	System.out.println("세계일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec4.shutdown() ;
                }       	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
    }
    
    private void newdailyCrawlerStart(int sleepSec) {
    	exec5.scheduleAtFixedRate(new Runnable(){
            public void run(){
                
            	try {	//NewDaily
            		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET newdaily=1");
            		//?useSSL=false 를 데이터베이스 이름 뒤에 쓰면 에러메시지 안뜸
            		conn.setAutoCommit(false);
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
            		PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
            		List<String[]> newDaily = crawler.newDaily();
                	for(int i = 0; i < newDaily.size(); i++) {
                		stmt.setString(1, newDaily.get(i)[0]);
                		stmt.setString(2, newDaily.get(i)[1]);
                		stmt.setString(3, newDaily.get(i)[2]);
                		stmt.setString(4, newDaily.get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET newdaily=2");
                	stmt1.executeUpdate("UPDATE statusNews SET modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
        	    	stmt1.close();
        	    	conn.close();
        			System.out.println("Good~6");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET newdaily=0");
            	    	stmt1.close();
            	    	conn.close();
                	}catch(Exception e1) {}
                	System.out.println("NewDaily Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec5.shutdown() ;
                }   	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
    }
    
    private void crawlerStop() {
    	try {
	    	Connection conn2 = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
			Statement stmt2 = conn2.createStatement();
	    	stmt2.executeUpdate("UPDATE statusNews SET chosun=0,donga=0,seoul=0,ytn=0,segye=0,newdaily=0,modifyTime='"+dayTime.format(new Date(time)).toString()+"'");
	    	stmt2.close();
	    	conn2.close();
    	}catch(Exception eEnd) {eEnd.printStackTrace();}
    }
}

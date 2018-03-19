package com.crawl.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.crawl.dto.statusVO;



public class NewsBot {

	public static void main(String[] args) throws Exception {
		
		Class.forName("com.mysql.jdbc.Driver");  //드라이버 호출
		
    	final Crawler crawler = new Crawler();
    	final statusVO vo = new statusVO();
        
        int sleepSec = 60 ;	// 실행간격 지정(1분)
        final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);	 // 주기적인 작업 실행(linux에서 daemon 돌리듯이..), 조선일보 쓰레드
        final ScheduledThreadPoolExecutor exec1 = new ScheduledThreadPoolExecutor(1);	 // 동아일보 쓰레드
        final ScheduledThreadPoolExecutor exec2 = new ScheduledThreadPoolExecutor(1);	 // 서울신문 쓰레드
        final ScheduledThreadPoolExecutor exec3 = new ScheduledThreadPoolExecutor(1);	 // YTN 쓰레드
        final ScheduledThreadPoolExecutor exec4 = new ScheduledThreadPoolExecutor(1);	 // 세계일보 쓰레드
        final ScheduledThreadPoolExecutor exec5 = new ScheduledThreadPoolExecutor(1);	 // NewDaily 쓰레드
        
        exec.scheduleAtFixedRate(new Runnable(){
            public void run() {
            	
                try {	//조선일보
                	Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                	Statement stmt1 = conn.createStatement();
                	stmt1.executeUpdate("UPDATE statusNews SET chosun=1");
            		//?useSSL=false 를 데이터베이스 이름 뒤에 쓰면 에러메시지 안뜸
            		conn.setAutoCommit(false);
            		String query = "INSERT IGNORE INTO news(site,title,newsURL,enrollDT) VALUES(?,?,?,?);";
                	PreparedStatement stmt = conn.prepareStatement(query);  //스테이트먼트 객체 생성
                	for(int i = 0; i < crawler.chosun().size(); i++) {
                		stmt.setString(1, crawler.chosun().get(i)[0]);
                		stmt.setString(2, crawler.chosun().get(i)[1]);
                		stmt.setString(3, crawler.chosun().get(i)[2]);
                		stmt.setString(4, crawler.chosun().get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
                	stmt.clearBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET chosun=2");
                	System.out.println("Good~1");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET chosun=0");
                	}catch(Exception e1) {}
                    System.out.println("조선일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec.shutdown() ;
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
        
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
                	for(int i = 0; i < crawler.donga().size(); i++) {
                		stmt.setString(1, crawler.donga().get(i)[0]);
                		stmt.setString(2, crawler.donga().get(i)[1]);
                		stmt.setString(3, crawler.donga().get(i)[2]);
                		stmt.setString(4, crawler.donga().get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
                	stmt.clearBatch();
        			conn.commit();
        			stmt.close();
        			
        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET donga=2");
        			System.out.println("Good~2");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET donga=0");
                	}catch(Exception e1) {}
                	System.out.println("동아일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec1.shutdown() ;
                }            	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
        
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
                	for(int i = 0; i < crawler.seoul().size(); i++) {
                		stmt.setString(1, crawler.seoul().get(i)[0]);
                		stmt.setString(2, crawler.seoul().get(i)[1]);
                		stmt.setString(3, crawler.seoul().get(i)[2]);
                		stmt.setString(4, crawler.seoul().get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
                	stmt.clearBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET seoul=2");
        			System.out.println("Good~3");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET seoul=0");
                	}catch(Exception e1) {}
                	System.out.println("서울신문 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec2.shutdown() ;
                }          	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
        
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
                	for(int i = 0; i < crawler.ytn().size(); i++) {
                		stmt.setString(1, crawler.ytn().get(i)[0]);
                		stmt.setString(2, crawler.ytn().get(i)[1]);
                		stmt.setString(3, crawler.ytn().get(i)[2]);
                		stmt.setString(4, crawler.ytn().get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
                	stmt.clearBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET ytn=2");
        			System.out.println("Good~4");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET ytn=0");
                	}catch(Exception e1) {}
                	System.out.println("YTN Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec3.shutdown() ;
                }         	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
        
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
                	for(int i = 0; i < crawler.segye().size(); i++) {
                		stmt.setString(1, crawler.segye().get(i)[0]);
                		stmt.setString(2, crawler.segye().get(i)[1]);
                		stmt.setString(3, crawler.segye().get(i)[2]);
                		stmt.setString(4, crawler.segye().get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
                	stmt.clearBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET segye=2");
        			System.out.println("Good~5");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET segye=0");
                	}catch(Exception e1) {}
                	System.out.println("세계일보 Executor error----------------------------------");
                    e.printStackTrace();
                    System.out.println("------------------------------------------------------");
                    
                    // 에러 발생시 Executor를 중지시킨다
                    exec4.shutdown() ;
                }       	
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
        
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
                	for(int i = 0; i < crawler.newDaily().size(); i++) {
                		stmt.setString(1, crawler.newDaily().get(i)[0]);
                		stmt.setString(2, crawler.newDaily().get(i)[1]);
                		stmt.setString(3, crawler.newDaily().get(i)[2]);
                		stmt.setString(4, crawler.newDaily().get(i)[3]);
                		
                		stmt.addBatch();
                	}
                	stmt.executeBatch();
                	stmt.clearBatch();
        			conn.commit();
        			stmt.close();

        			conn.setAutoCommit(true);
                	stmt1.executeUpdate("UPDATE statusNews SET newdaily=2");
        			System.out.println("Good~6");
                } catch (Exception e) {
                	try {
                		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.103:3306/kopoctc?autoReconnect=true&useSSL=false","root","12345678");
                		Statement stmt1 = conn.createStatement();
                    	stmt1.executeUpdate("UPDATE statusNews SET newdaily=0");
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
}

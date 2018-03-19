package com.test.ctest;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class NewsBot {

	public static void main(String[] args) {
    	final Crawler crawler = new Crawler();
    	final newsVO vo = new newsVO();
        
        int sleepSec = 10 ;	// 실행간격 지정(10초)
        final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);	 // 주기적인 작업 실행(linux에서 daemon 돌리듯이..), 조선일보 쓰레드
        final ScheduledThreadPoolExecutor exec1 = new ScheduledThreadPoolExecutor(1);	 // 동아일보 쓰레드
        final ScheduledThreadPoolExecutor exec2 = new ScheduledThreadPoolExecutor(1);	 // 서울신문 쓰레드
        final ScheduledThreadPoolExecutor exec3 = new ScheduledThreadPoolExecutor(1);	 // YTN 쓰레드
        final ScheduledThreadPoolExecutor exec4 = new ScheduledThreadPoolExecutor(1);	 // 세계일보 쓰레드
        final ScheduledThreadPoolExecutor exec5 = new ScheduledThreadPoolExecutor(1);	 // NewDaily 쓰레드
        
        exec.scheduleAtFixedRate(new Runnable(){
            public void run(){
            	
                try {	//조선일보
                	vo.setChosunNewsList(crawler.chosun());
                	
                } catch (Exception e) {
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
                	vo.setDongaNewsList(crawler.donga());
                	
                     
                } catch (Exception e) {
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
            		vo.setSeoulNewsList(crawler.seoul());
                	
                     
                } catch (Exception e) {
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
            		vo.setYtnNewsList(crawler.ytn());
                	
                     
                } catch (Exception e) {
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
            		vo.setSegyeNewsList(crawler.segye());
                	
                     
                } catch (Exception e) {
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
            		vo.setNewDailyNewsList(crawler.newDaily());
                	
                     
                } catch (Exception e) {
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
package com.kh.spring.common.run;

import javax.swing.JOptionPane;

public class ThreadRun {

	public static void main(String[] args) throws InterruptedException {
		
		//빨래,청소기돌리기,설거지
		//30분  20분    15분 
		//65분 
		//세탁기,로봇청소기,식기세척기
		//25분, 20분  , 15분
		//25분
		
		//Thread를 생성하여 작업 처리를 나눠보기 
		
		for(int i=0; i<10; i++) {
			System.out.println(i);
			
			//작업주체(쓰레드를 1초 재우기) 
			Thread.sleep(1000); 
			
		}
		
		
		//알림창 
		JOptionPane.showMessageDialog(null,"안녕하세요");

	}

}

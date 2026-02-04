package com.kh.spring.common.run;

import javax.swing.JOptionPane;

public class ThreadRun2 {
	public static void main(String[] args) {
		//Thread를 추가 생성하여 작업주체 늘려보기 
		
		Thread t =  new Thread() {
			
			@Override
			public void run() {//쓰레드의 동작을 결정하는 메소드 
				
				for(int i=0; i<10; i++) {
					System.out.println(i);
					try {
						//작업주체 1초 재우기
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		//쓰레드 동작시키기
		t.start(); 
		//알림창 
		JOptionPane.showMessageDialog(null, "반갑습니다.");
		
		
		
	}
}

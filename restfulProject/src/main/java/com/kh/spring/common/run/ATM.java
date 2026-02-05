package com.kh.spring.common.run;

public class ATM {
	//돈을 인출하는 메소드
	
	//만약 해당 메소드가 동시에 접근되어서는 안되는 기능이라면
	//메소드에 동기화 처리를 해야한다.
	//접근제한자 synchronized 반환형 메소드명
	
	public  void getMoney(String name) {
		System.out.println(name +"인출 시작!");
		
		
		try {
			
			Thread.sleep(3000); //3초 기다리기
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(name +"인출 끝!");
		
	}
}

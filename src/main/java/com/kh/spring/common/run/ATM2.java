package com.kh.spring.common.run;

import lombok.Getter;

@Getter
public class ATM2 {
	//잔고 필드를 만들어 10000 으로 설정하고 
	//쓰레드가 호출할때 매개변수로 이름과,출금액을 전달할 수 있도록 정의
	//전달받은 출금액이 잔고보다 적을 경우 OOO원 출금되었습니다.를 출력
	//출금액이 잔고보다 클 경우 ATM의 잔고가 부족합니다를 출력해보세요
	//동기화처리를 하기전에 확인하고 동기화처리를 한 후에도 확인해보기 
	//접근은 쓰레드 3개로 접근처리
	
	private int money = 10000;
	
	
	public synchronized void getMoney(String name,int out) {
		
		System.out.println(name+"님 인출 시작!");
		System.out.println("현재 ATM 잔고 :"+money);
		System.out.println("출금액 :"+out+"원");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(money>=out) {
			System.out.println(name+"님 "+out+"원 출금되었습니다.");
			money = money-out;//잔고에서 출금액 제외하기
		}else {
			System.out.println(name+"님 "+"ATM의 잔고가 부족합니다.");
		}
		
	}
	
}

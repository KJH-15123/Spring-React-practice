package com.kh.spring.common.run;

public class ThreadRun3 {

	public static void main(String[] args) {
		// ATM에 3개의 쓰레드가 접근하도록 처리해보기

		ATM atm = new ATM();

		// 작업주체1
		Thread t = new Thread() {
			@Override
			public void run() {
				atm.getMoney("김출금");
			}
		};
		// 작업주체2
		Thread t2 = new Thread() {
			@Override
			public void run() {
				atm.getMoney("박은행");
			}
		};

		// 작업주체3
		Thread t3 = new Thread() {
			@Override
			public void run() {
				atm.getMoney("최현금");
			}
		};
		
		//위에 만든 3개의 쓰레드를 동작시키기
		t.start();
		t2.start();
		t3.start();
		
		

	}

}

package com.kh.spring.common.run;

public class ThreadRun4 {
	public static void main(String[] args) throws InterruptedException {

		ATM2 atm = new ATM2();

		Thread t = new Thread() {
			@Override
			public void run() {

				atm.getMoney("김스레드", 5000);
			}
		};
		Thread t2 = new Thread() {
			@Override
			public void run() {

				atm.getMoney("박스레드", 3000);
			}
		};
		Thread t3 = new Thread() {
			@Override
			public void run() {

				atm.getMoney("최스레드", 3000);
			}
		};
		
		
		//start() : 스레드를 생성하여 동작시킴
		t.start();
		t2.start();
		t3.start();
		
		Thread.sleep(12000);
		
		System.out.println(atm.getMoney());
		

	}

}

package com.kh.spring.common.scheduler.template;

import java.time.Instant;
import java.util.Date;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

//해당 클래스를 트리거로 만들기 위해 Trigger 인터페이스 구현 및 메소드 재정의
public class CustomTrigger implements Trigger{
	
	
	@Override
	public Date nextExecutionTime(TriggerContext triggerContext) {
		
		//마지막 실행 시점 확인하기
		Date lastTime = triggerContext.lastCompletionTime();
		
		System.out.println(lastTime);//마지막 실행시점 결과가 null이라는것은 처음 실행되었다 라는것 
		
		if(lastTime==null) {//처음 실행이라면 
			return new Date(System.currentTimeMillis()+3000); //첫실행은 3초뒤에 수행되도록 처리 
			
		}
		
		//null이 아닌 경우 (첫실행이 아닌 경우)
		return new Date(lastTime.getTime()+5000); //마지막 실행 이후 5초뒤 
	}

	@Override
	public Instant nextExecution(TriggerContext triggerContext) {
		// TODO Auto-generated method stub
		return null;
	}


}

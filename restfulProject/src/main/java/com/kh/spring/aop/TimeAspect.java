package com.kh.spring.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Aspect //해당 클래스를 Aspect 지정하기 
@Component //component 등록하기
@Slf4j //log4j를 스프링이 관리하는 slf4j 어노테이션 (로그 사용 어노테이션) boot에선 log4j 대신 logback 사용 (내장)
public class TimeAspect {
	
	/*
	 * 간섭 메소드 (Advice) - 특수한 경우가 아니라면 메소드의 형태는 정해져있다.
	 * 어떠한 대상을 어떠한 시점에 간섭할것인지 어노테이션을 이용하여 등록한다.
	 * 
	 * target : 특정 인터페이스와 그의 자식 클래스의 메소드를 지정한다. 
	 * within : 특정 패키지 or 클래스의 모든 메소드를 지정한다. 
	 * excution : 표현식으로 형태를 지정하여 간섭한다. excution[접근제한] 반환형 풀클래스명.메소드명(매개변수형태)
	 * -매개변수 형태 - (*) : 매개변수가 하나인 경우 / (..) : 매개변수가 0개 이상(개수 상관 X)
	 * 단독 * : 와일드카드(모든것) 
	 * 
	 * 
	 * 주요 어노테이션 
	 * @Before : 지정한 메소드 실행 전 간섭
	 * 
	 * @After : 지정한 메소드 실행 후 
	 * 
	 * @AfterReturning :  지정한 메소드 정상 실행 후 간섭
	 * 
	 * @AfterThrowing : 지정한 메소드에서 예외가 발생한 후 간섭 
	 * 
	 * @Around : 지정한 메소드 실행 전후로 간섭
	 * 
	 * */
	
	//실행전
	//@Before("target(com.kh.spring.board.model.dao.BoardDao)")
	public void before(JoinPoint joinPoint){
		
		log.debug("boardDao 실행 전 간섭!");
		log.debug("join Point : {}",joinPoint); //{} 로 작성한 위치에 넣을 값을 , 이후 작성
		log.debug("실행 클래스 : {}",joinPoint.getTarget().getClass()); 
		log.debug("메소드명 : {}",joinPoint.getSignature().getName()); 
		log.debug("전달값 : {} ",Arrays.toString(joinPoint.getArgs()));
		
	}
	
	//실행후
	//@After("target(com.kh.spring.member.model.dao.MemberDao)")
	public void after(JoinPoint joinPoint) { //joinPoint : 어드바이스가 접근한 실행 시점(메소드)
		log.debug("memberDao 실행 후 간섭!");
		log.debug("joinPoint : {}",joinPoint);
		log.debug("실행 클래스 : {}",joinPoint.getTarget().getClass()); 
		log.debug("메소드명 : {}",joinPoint.getSignature().getName()); 
		log.debug("전달값 : {} ",Arrays.toString(joinPoint.getArgs()));
	}
	
	
	//실행전후 
	//@Around("target(com.kh.spring.board.model.dao.BoardDao)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		
		log.debug("around로 실행 전 간섭");
		
		//joinPoint.proceed() 를 사용하면 기존 메소드를 수행한다
		//수행한 이후 접근하기 
		Object obj = joinPoint.proceed();
		log.debug("수행결과 : {}",obj);
		
		log.debug("around로 실행 후 간섭");
		
		return obj;
	}
	
	//실행 전 후로 간섭이 가능하기 때문에 해당 수행작업이 얼마나 걸리는지 시간 측정해보기 
	//간섭 대상 : BoardService
	//시작시간과 끝 시간을 측정하여 각 메소드 처리 시간이 얼마나 걸리는지 log로 찍어서 확인해보기 
	//출력형식 수행대상 : OOO(메소드명) 
	//걸린시간 : O초 
	//@Around("target(com.kh.spring.board.model.service.BoardService)")
	public Object time(ProceedingJoinPoint joinPoint) throws Throwable {
		
		long start = System.currentTimeMillis(); //시작시간 측정 
		
		Object obj = joinPoint.proceed();//기존 대상 메소드 수행 
		
		long finish = System.currentTimeMillis(); //끝 시간 측정
		
		
		log.debug("수행 대상 : {}",joinPoint.getSignature().getName());
		log.debug("걸린 시간 : {}초",(finish-start)/1000.0);
		
		
		return obj;//기존 수행 메소드 결과 반환
	}
	
	//Around를 이용하여 전달값과 반환값을 확인해보기
	
	//@Around("target(com.kh.spring.member.model.service.MemberService)")
	public Object memberAop(ProceedingJoinPoint joinPoint) throws Throwable {
		
		//간섭당하는 대상 메소드 
		String methodName = joinPoint.getSignature().getName(); 
		Object[] args = joinPoint.getArgs(); //해당 메소드에 전달되는 전달값들 
		
		log.debug("메소드명 : {}",methodName);
		log.debug("전달값 : {}",Arrays.toString(args));
		
		//전달값중 첫번째 전달값에 접근 
		
		Member m = (Member)args[0]; //첫번째 전달값인 Member객체 접근 
		
		m.setUserId("admin");//아이디값 고정 
		
		args[0] = m; //전달값 객체에 m 다시 넣기 (변경된 아이디)
		
		Object obj = joinPoint.proceed(args);//대상 메소드에 변경된 인자값 전달 및 수행
		
		log.debug("반환값 : {}",obj);
		
		return obj;
	}
	
	//BoardService 를 간섭하여 조회된 목록을 빈 목록으로 처리해보기 
	//원본 전달값을 로그로 찍고 수행된 결과는 화면으로 확인하기
	//@Around("target(com.kh.spring.board.model.service.BoardService)")
	public Object emptyList(ProceedingJoinPoint joinPoint) throws Throwable {
		
		log.debug("메소드 : {}",joinPoint.getSignature().getName());
		
		log.debug("전달값 : {}",joinPoint.getArgs());
		
		Object obj = joinPoint.proceed(); //결과값
		
		log.debug("결과값: {}",obj);
		
		log.debug("결과값 타입 : {}",obj.getClass());
		
		//리턴할 객체를 원하는 객체로 변경
		//결과값이 list일때 빈 리스트로 변경하기 
		//if(obj.getClass()==ArrayList.class)
		if(obj instanceof List) {
			log.debug("리스트 결과값 : {}",obj);
			
			obj = new ArrayList<>();//빈 리스트 넣기 
		}
		return obj;
	}
	
	//@Around("within(com.kh.spring.board.controller.*)")//특정 패키지 내 모든 클래스 메소드에 간섭
	//@Around("execution(* com.kh.spring.board.model.dao.*.*(..))") //해당 패키지안에 모든클래스 모든 메소드
	//@Around("execution(* com.kh.spring.board.model.dao.BoardDao.boardList(..))")//특정 메소드 이름 지정(매개변수 형태 상관없음)
	//@Around("execution(int com.kh.spring.board.model.dao.BoardDao.*(..))")//특정 반환형을 가진 메소드만 지정
	@Around("execution(string com.kh.spring.board..*.BoardDao.*(..))")// 패키지..* : 하위 패키지 0개이상 (하위패키지 여러개일때 모두포함)
	public Object pointCut(ProceedingJoinPoint joinPoint) throws Throwable {
		
		log.debug("around로 실행 전 간섭 대상 {}",joinPoint.getSignature().getName());
		//joinPoint.proceed로 기존 메소드 수행 
		Object obj = joinPoint.proceed();
		
		log.debug("around로 실행 후 간섭 및 결과 {}",obj);
		
		
		return obj;
	}
	
	
	
	
	
	
	

}

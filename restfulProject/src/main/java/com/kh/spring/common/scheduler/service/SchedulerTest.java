package com.kh.spring.common.scheduler.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;

@Service
public class SchedulerTest {
	
	
	//보드서비스 필드 추가
	@Autowired
	private BoardService service;
	
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	/*
	 * 스케쥴러 사용법
	 * 메소드에 @Scheduled() 어노테이션을 부여하여 처리
	 * 이때 어떠한 간격(규칙)을 갖고 동작시킬것인지를 추가한다.
	 * initialDelay : 시작하고 ~뒤에 실행 (1000 = 1초) ms단위
	 * fixedDelay : 고정값으로 설정된 시간마다 실행
	 * 위와 같은 기본 설정과
	 * 상세 설정을 할 수 있는 cron 표기식이 이용된다
	 * 
	 * cron 표기식 표현법 
	 *  * : 모든 값을 의미 (매시/매분/매초)
	 *  ? : 사용하지 않음(미지정) - day of month나 day of week필드에서 사용
	 *  - : 특정 기간을 의미  ex) 10-13은 10시~13시를 의미
	 *  / : 반복 단위 ex) 별/5 는 매 5단위
	 *  L : 마지막 날짜에 동작 (day of month / day of week) 에서 동작
	 *  W : 가까운 평일에 동작 (day of month)에서 사용 
	 *  LW : 그 달의 마지막 평일 (day of month)에서 사용
	 *  # : 몇번째 주인지와 요일 설정 day of week에서 사용 ex) 5#2 면 2번째주 목요일 
	 * 
	 *  cron 표기법 각 위치는 
	 *  초 분 시 일 월 요일 년도(선택) 으로 이루어진다.
	 * 
	 * */
	
//	시작하고 5초뒤 수행 3초마다 수행
//	@Scheduled(initialDelay = 5000,fixedDelay = 3000)
//	@Scheduled(initialDelay = 1000,fixedDelay = 2000)
//	public void schedulerTest() {
//		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
//		
//		System.out.println("첫번째 테스트 : "+time);
//	}
//	
//	@Scheduled(initialDelay = 1000,fixedDelay = 2000)
//	public void schedulerTest2() {
//		
//		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
//		
//		System.out.println("두번째 테스트 : "+time);
//	}
	
	//@Scheduled(cron = "* * * * * *")//매초마다 수행
	//@Scheduled(cron = "*/2 * * * * *")//2초마다 수행
	//@Scheduled(cron = "0 * * * * *")//매분마다 수행
	//@Scheduled(cron = "0 0 * * * *")//매시마다 수행
	//@Scheduled(cron = "0 0 0 * * *")//매일 자정마다 24:00 수행
	//@Scheduled(cron = "0 0 6 * * *")//매일 아침 6시에 수행
	//@Scheduled(cron = "0 0 8 1 * *")//매달 1일 아침 8시에 수행
	public void schedulerTest() {
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		
		System.out.println("첫번째 테스트 : "+time);
	}
	
	//10초마다 1번 게시글을 조회하는 작업을 스케쥴러를 이용해서 처리해보세요 
	//조회된 게시글을 출력문으로 출력해보기
	
	//@Scheduled(cron = "*/10 * * * * *")
	public void selectBoard() {
		
		Board b = service.boardDetail(1);
		
		System.out.println(b);
	}
	
	//1)
	//매 30초마다 가장 오래된 게시글의 status를 N 으로 변경하는 작업 수행해보기 
	//boardMapper sql구문 id = statusN 으로 작성해서 처리해보기
	//SqlSesison으로 바로 처리하시오 
	//@Scheduled(cron = "*/30 * * * * *")
	public void statusN() {
		int result = sqlSession.update("boardMapper.statusN");
		
		if(result>0) {
			System.out.println("변경 성공!");
		}else {
			System.out.println("변경 실패!");
		}
	}
	//2)
	//매분마다 status 가 N인 게시글 목록을 조회해오기 
	//nList 로 작성하여 처리하기 
	//목록은 반복문으로 출력
	//@Scheduled(cron="0 * * * * *")
	public void nList() {
		
		List<Board> nList = sqlSession.selectList("boardMapper.nList");
		
		for(Board b : nList) {
			System.out.println(b);
		}
		
	}
	
	
}

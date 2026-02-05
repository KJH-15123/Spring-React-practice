package com.kh.spring.common.scheduler.template;

import java.util.Date;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class TaskTest {
	// 사용자 정의 스케쥴러 작성해보기
	// 미리 정해놓은 스케쥴러를 원하는 시점에 동작시켜보기
	// 스케쥴러 사용하기 위한 di 주입받기 (root-context에 등록한 id 사용)

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private SqlSessionTemplate sqlSession;

	public void taskTest() {

		// 스케쥴러.schedule(Runnable객체,java.util.Date 객체)
		// date 객체는 시간을 담아 얼마만큼의 시간이 흐른 뒤 처리할지 설정

		taskScheduler.schedule(new Runnable() {

			@Override
			public void run() {

				System.out.println("taskTest 동작!");

			}

		}, new Date(System.currentTimeMillis() + 5000));// 현재시간 + 5초
	}

	// 게시글 상세보기를 하면 해당 게시글이 10초뒤에 status가 N으로 변경되는 로직을 작성하기
	// updateStatusN() 메소드명을 이용해보세요

	public void updateStatusN(int bno) {

		taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {

				int result = sqlSession.update("boardMapper.updateStatusN", bno);

				if (result > 0) {
					System.out.println("변경 성공");
				} else {
					System.out.println("변경 실패");
				}

			}
		}, new Date(System.currentTimeMillis() + 10000)); // 현재시간 + 10초

	}

	// 트리거에 대해 알아보기(동적으로 스케쥴러 동작시점 설정하기)
	public void scTrigger() {

		taskScheduler.schedule(new Runnable() {

			@Override
			public void run() {

				System.out.println("트리거를 이용하여 주기설정하기");

			}
		}, new CronTrigger("* * * * * *"));// 매초마다 수행시키는 크론표현식 트리거 설정
	}

	// 사용자 정의 트리거 설정해보기
	public void scCustomTrigger() {

		taskScheduler.schedule(new Runnable() {

			@Override
			public void run() {

				System.out.println("사용자 정의 트리거 확인");
				

			}
		}, new CustomTrigger());// 사용자 정의 트리거 객체전달
	}

}

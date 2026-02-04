package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Lombok(롬복)
 * -자동 코드 생성 라이브러리
 * -반복되는 getter,setter,toString 등의 메소드 작성 코드를 줄여주는 코드 라이브러리
 * Lombok 사용시 주의사항
 * -uName,bTitle과 같이 앞글자가 외자인 필드명은 지양할것
 * -필드명 작성시 적어도 소문자 두글자 이상으로 시작해야한다.
 * -EL표기법을 이용하여 내부 getter메소드 호출시 getuName과 같이 잘못된 메소드를 호출할 가능성있음
 *  
 * 어노테이션을 이용하여 필요한 메소드들을 사용한다. 
 * */
@NoArgsConstructor //기본생성자
@AllArgsConstructor //모든 필드를 매개변수로 갖는 매개변수 생성자
//@Setter //setter메소드들 
//@Getter //getter메소드들
//@ToString// toString 메소드
//@EqualsAndHashCode //equals와 hashcode 메소드
@Data //getter/setter/toString/eq&hc 를 다 포함한 어노테이션
@Builder //Builder (객체 생성 구문을 별도의 build 형태로 작성 가능하다)
public class Member {
	
	private String userId;		//	USER_ID	VARCHAR2(30 BYTE)
	private String userPwd;//	USER_PWD	VARCHAR2(100 BYTE)
	private String userName;//	USER_NAME	VARCHAR2(15 BYTE)
	private String email;//	EMAIL	VARCHAR2(100 BYTE)
	private String gender;//	GENDER	VARCHAR2(1 BYTE)
	private String age;//	AGE	NUMBER
	private String phone;//	PHONE  VARCHAR2(13 BYTE)
	private String address;//	ADDRESS	VARCHAR2(100 BYTE)
	private Date enrollDate;//	ENROLL_DATE	DATE
	private Date modifyDate;//	MODIFY_DATE	DATE
	private String status;//	STATUS	VARCHAR2(1 BYTE)

}

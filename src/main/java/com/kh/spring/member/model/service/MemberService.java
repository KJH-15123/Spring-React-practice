package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;

//설계단계에서 메소드 형식 정의 
public interface MemberService {
	
	//로그인 기능 (SELECT)
	Member loginMember(Member m);
	//회원가입 기능 (INSERT)
	int insertMember(Member m);
	//회원 정보 수정 (UPDATE)
	int updateMember(Member m);
	//회원 탈퇴 기능 (UPDATE / DELETE)
	int deleteMember(String userId);
	
	//아이디 중복 확인 기능 (SELECT)
	int idCheck(String inputId);
	
}

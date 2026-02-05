package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

//@Repository : 저장소 (주로 DB저장소와 관련된 처리를 하는 영역)
@Repository
public class MemberDao {

	//로그인
	public Member loginMember(SqlSessionTemplate sqlSession, Member m) {
		
		//한줄처리
		return sqlSession.selectOne("memberMapper.loginMember",m);
	}

	//회원가입
	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.insert("memberMapper.insertMember",m);
	}

	public int deleteMember(SqlSessionTemplate sqlSession, String userId) {
		
		return sqlSession.update("memberMapper.deleteMember",userId);
	}

	public int updateMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.update("memberMapper.updateMember",m);
	}

	public int idCheck(SqlSessionTemplate sqlSession, String inputId) {
		
		return sqlSession.selectOne("memberMapper.idCheck",inputId);
	}
	
	
	
	

}

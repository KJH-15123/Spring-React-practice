package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public int listCount() {
		
		return dao.listCount(sqlSession);
	}
	
	@Override
	public ArrayList<Board> boardList(PageInfo pi) {
		
		return dao.boardList(sqlSession,pi);
	}
	
	
	@Override
	public int increaseCount(int bno) {
		
		return dao.increaseCount(sqlSession,bno);
	}

	@Override
	public Board boardDetail(int bno) {

		return dao.boardDetail(sqlSession,bno);
	}
	
	
	@Override
	public int boardInsert(Board b) {
	
		return dao.boardInsert(sqlSession,b);
	}
	
	@Override
	public int updateBoard(Board b) {
	
		return dao.updateBoard(sqlSession,b);
	}
	
	@Override
	public int deleteBoard(int bno) {
		
		return dao.deleteBoard(sqlSession,bno);
	}
	
	@Override
	public ArrayList<Board> searchList(HashMap<String, String> map,PageInfo pi) {
	
		return dao.searchList(sqlSession,map,pi);
	}
	
	@Override
	public int searchListCount(HashMap<String, String> map) {
	
		return dao.searchListCount(sqlSession,map);
	}
	
	@Override
	public List<Reply> replyList(int refBno) {
		
		return dao.replyList(sqlSession,refBno);
	}
	
	@Override
	public int insertReply(Reply r) {
		
		return dao.insertReply(sqlSession,r);
	}
	
	@Override
	public List<Board> topList() {
		
		return dao.topList(sqlSession);
	}
	
	/*
	@Override
	public int insertPhoto(Board b, ArrayList<Attachment> atList) {
		//게시글 정보와 첨부파일 정보를 다른 테이블에 넣어야한다.
		//이때 둘중 하나라도 잘못된다면 트랜잭션이 확정되어서는 안되기 때문에 
		//트랜잭션을 묶어서 처리할 수 있도록 해야함.
		
		//게시글 번호를 미리 추출하여 게시글 등록과 첨부파일 등록에 사용하기 
		
		int boardNo = dao.selectBoardNo(sqlSession);
		
		//게시글 번호가 잘 추출되었다면 이후 처리하기 
		
		if(boardNo > 0) {
			b.setBoardNo(boardNo);//미리 추출한 게시글 번호 넣어주기 
		}else {
			return boardNo; //0보다 크지 않으면 실패 처리 될수있도록 반환
		}
		
		
		//사진 게시글 등록용 메소드 호출
		int result = dao.insertPhoto(sqlSession,b);
		
		//첨부파일 등록 결과 변수 
		int result2 = 1;
		
		if(result>0) {//게시글정보가 잘 등록됐다면 (성공)
			
			//첨부파일 정보들을 모두 등록하고 제대로 처리가 되었다면 
			//성공값 리턴하기 
			//첨부파일 목록을 모두 등록하기 
			for(Attachment at : atList) {
				//목록중 하나라도 등록처리가 되지 않는다면 모두가 되돌아갈수있도록(rollback)
				//처리하기
				at.setRefBno(boardNo);//참조 게시글 번호 추가 
				result2 *= dao.insertAttachment(sqlSession,at);//하나의 데이터씩 처리 
				//만약 첨부파일 목록중 하나라도 잘못된다면 결과값이 0이 되도록 곱셈처리
			}
			
			//최종 리턴값
			return result * result2; //둘중 하나라도 0이면 0이 리턴되도록 처리 
			
			
		}else {//실패
			return result;//0보다 크지 않은 수 반환(실패판별)
		}
	}
	*/
	
	
	//위에 작업한 게시글번호 추출 + 게시글정보등록 +  다중파일등록 처리를 
	//마이바티스 동적 sql 기능을 이용하여 효율적으로 작성해보기 
	
	//첨부파일정보를 별도의 테이블에 등록하는 작업에서 만약 첨부파일 정보 등록에 오류가 발생하면
	//기존에 들어가려던 게시글 정보도 등록되어서는 안된다. 스프링이 트랜잭션처리를 관리하지만
	//메소드를 묶어 처리하지 않고 각 sqlSession 메소드 별로 처리하기 때문에 
	//메소드 내에 있는 구문을 함께 트랜잭션 하고 싶다면 @Transactional 어노테이션을 부여해야한다.
	//pom에 tx dependency 추가 후 root-context에 driven 및 manager 등록
	@Transactional //해당 메소드에서 동작하는 메소드들의 트랜잭션을 한번에 처리하게 해주는 어노테이션
	@Override
	public int insertPhoto(Board b, ArrayList<Attachment> atList) {
		
		//게시글 정보 추가 
		int result = dao.insertPhoto(sqlSession, b);
		
		if(result>0) {
			
			//목록 그대로 보내보기 
			int result2 = dao.insertAttachment(sqlSession, atList);
			
			return result*result2; //하나라도 0이면 0으로 반환되도록 처리 
		}else {
			//실패시 0처리 
			return 0;
		}
	}
	
	
	//사진 게시글 목록
	@Override
	public List<Board> photoList() {
		
		return dao.photoList(sqlSession);
	}
	
	
	//사진게시글 상세보기시 첨부파일 목록
	@Override
	public List<Attachment> AttachmentList(int bno) {
		
		return dao.attachmentList(sqlSession,bno);
	}
	
	
	
	
	
	
	
}

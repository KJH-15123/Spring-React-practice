package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {

	
	//게시글 총 개수 
	int listCount();
	
	//게시글 목록 조회
	ArrayList<Board> boardList(PageInfo pi);

	//조회수증가
	int increaseCount(int bno);
	//상세보기
	Board boardDetail(int bno);
	
	//게시글 등록
	int boardInsert(Board b);

	//게시글 수정
	int updateBoard(Board b);

	//게시글 삭제
	int deleteBoard(int bno);
	
	//게시글 검색
	ArrayList<Board> searchList(HashMap<String, String> map, PageInfo pi);
	
	//검색 게시글 개수
	int searchListCount(HashMap<String, String> map);
	
	//댓글 목록 조회
	List<Reply> replyList(int refBno);

	int insertReply(Reply r);

	List<Board> topList();

	//사진게시글 등록
	int insertPhoto(Board b, ArrayList<Attachment> atList);
	
	
	//사진게시글 목록
	List<Board> photoList();

	List<Attachment> AttachmentList(int bno);
	
	
	
	
	
	
	

}

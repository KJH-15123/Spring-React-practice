package com.kh.spring.board.model.vo;

import java.util.List;

import com.kh.spring.common.model.vo.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//게시글 목록과 페이징 정보를 함께 담아 반환할 수 있도록 하는 클래스 
//게시글 목록 DTO (Data Transfer Object) - 데이터를 주고받을 때 사용하는 클래스(객체) 요청 or 응답시 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListDTO {
	
	private List<Board> list; //게시글 목록
	private int listCount; //총 게시글 수
	private int currentPage; //현재 페이지
	private int maxPage; //마지막 페이지
	private int startPage; //페이징바 시작 페이지
	private int endPage; //페이징바 끝 페이지 
	
	
	//게시글 목록과 페이징 정보로 응답 객체 생성 메소드
	public static BoardListDTO of(List<Board> list,PageInfo pageInfo) {
		return BoardListDTO.builder()
						   .list(list)
						   .currentPage(pageInfo.getCurrentPage())
						   .listCount(pageInfo.getListCount())
						   .maxPage(pageInfo.getMaxPage())
						   .startPage(pageInfo.getStartPage())
						   .endPage(pageInfo.getEndPage())
						   .build();
}
	
	
	
}

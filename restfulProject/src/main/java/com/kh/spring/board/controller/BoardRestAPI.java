package com.kh.spring.board.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@RestController //@Controller와 @ResponseBody 가 합쳐진 형태
@RequestMapping("/api")
public class BoardRestAPI {
	
	
	@Autowired
	private MemberService service;
	
	//GET 요청받기
	@GetMapping("/list")
	public ArrayList<Board> selectList(){
		
		ArrayList<Board> list = new ArrayList<>();
		list.add(Board.builder()
					  .boardNo(1)
					  .boardTitle("글제목1")
					  .boardContent("글내용1")
					  .build());
		list.add(Board.builder()
				  .boardNo(2)
				  .boardTitle("글제목2")
				  .boardContent("글내용2")
				  .build());
		list.add(Board.builder()
				  .boardNo(3)
				  .boardTitle("글제목3")
				  .boardContent("글내용3")
				  .build());
		
		return list;
	}
	
	//POST요청 받기 (글작성)
	@PostMapping("/insert")
	public int insertBoard(@RequestBody Board b) {
		//비동기 POST 요청시 body영역에 데이터를 전달했다면
		//꺼내줄때도 @RequestBody 어노테이션을 이용하여 꺼내야한다.
		
		System.out.println(b);
		
		int result = 0;
		
		if(b!=null) {
			result = 1;
		}
		return result;
	}
	
	
	//Get요청 받기 (사용자 정보 조회)
	@GetMapping("/selectMember")
	public Member selectMember(String userId) {
		
		Member m = Member.builder().userId(userId).build();
		
		Member selectMember = service.loginMember(m);
		
		
		return selectMember; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

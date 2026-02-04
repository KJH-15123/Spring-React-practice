package com.kh.spring.member.controller;


import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;
import com.kh.spring.util.JwtUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


//Controller 타입의 어노테이션을 부여하면 spring이 bean scan을 통해 Controller bean으로 등록한다.
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {

	
	@Autowired
	private MemberService service;
	
	//BcryptPasswordEncoder 사용하기 위해서 스프링에게 주입 처리 하기 
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginMember(@RequestBody Member m) {
		//post 요청시 json객체 형태로 데이터 전달하면 requestBody로 받아주어야함
		
		HashMap<String, Object> map = new HashMap<>();
		
		//사용자가 입력한 id로 회원 정보 조회 
		Member loginMember = service.loginMember(m);
		
		
		if(loginMember==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								 .body("존재하지 않는 회원입니다.");//아이디 잘못입력한경우
		}
		
		//위에 조회된 loginMember가 null 이 아닌 경우에만 비밀번호 검증 처리하기
		
		if(bcrypt.matches(m.getUserPwd(), loginMember.getUserPwd())) {
			
			//JWT 토큰 생성하여 응답데이터에 로그인 정보와 토큰정보 담아서 반환하기
			String token = jwtUtil.generateToken(loginMember.getUserId());
			
			loginMember.setUserPwd(null); //비밀번호 안보내기
			map.put("token", token);
			map.put("user", loginMember);
			
			//데이터 담은 응답 처리 
			return ResponseEntity.ok(map);
		}else {//비밀번호 오류 
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					 .body("아이디 또는 비밀번호가 일치하지 않습니다.");//아이디 잘못입력한경우
		}
		
		
	}
	
	
	//로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		//JWT는 서버에서 세션을 관리하지 않기 때문에 
		//클라이언트에서 토큰 삭제만으로 로그아웃처리 된다.
		
		return ResponseEntity.ok("로그아웃 되었습니다.");
	}
	
	
	/*
	 	응답뷰로 포워딩(위임)시 전달데이터 담을 수 있는 객체
	 * 1.model
	 * requestScope를 담당하는 객체로 key value세트로 데이터를 담아서 전달할 수 있다.
	 * 데이터 담는 메소드는 기존에 사용하던 setAttribute가 아닌 addAttribute 이다.	 
	 * */
	//마이페이지 이동 
	@RequestMapping("/mypage.me")
	public String mypage(Model model) {
		
		//마이페이지로 이동시키기 
		//model.addAttribute("alertMsg","모델로 데이터 넘기기");
		
		return "member/mypage";
	}
	
	
	//회원가입 페이지로 이동 메소드
	@RequestMapping("/enrollForm.me")
	public String enrollForm() {
		
		return "member/memberEnrollForm";
	}
	
	
	//회원가입 요청시 처리 메소드 
	@PostMapping("/register")
	public ResponseEntity<?> insertMember(@RequestBody Member m) {

		m.setUserPwd(bcrypt.encode(m.getUserPwd()));
		
		int result = service.insertMember(m);
		
		if(result>0) {//회원가입 성공
			return ResponseEntity.status(HttpStatus.CREATED) //201
								 .body("회원가입이 완료되었습니다.");
		}else { //회원가입 실패 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) //500
					 .body("회원가입에 실패했습니다.");
		
		}
	}
	
	
	//회원탈퇴
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteMember(@RequestParam String userId,
										  @RequestParam String userPwd) {		
		
		Member m = Member.builder().userId(userId).userPwd(userPwd).build();
		
		//기존에 만들어둔 메소드 활용(로그인 데이터 조회해오기)
		Member loginMember = service.loginMember(m);
//		실패시 실패메시지와 함께 마이페이지로
//		성공시 로그인 풀고 메인페이지로 메시지도 함께 (메시지 자유)
		if(loginMember!=null&&bcrypt.matches(m.getUserPwd(), loginMember.getUserPwd())) {
			
			//회원 탈퇴 처리 요청하기 
			int result = service.deleteMember(m.getUserId());
			
			if(result>0) {//탈퇴 성공
				
				return ResponseEntity.ok("탈퇴 성공");
				
			}else {//탈퇴 실패
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
									 .body("탈퇴 요청에 실패했습니다.");
				
			}
		}else { //로그인정보가 없거나 비밀번호가 일치하지 않는 경우 
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								 .body("인증 실패");
		}
		
	}
	
	
	//회원 정보 수정
	@PutMapping("/update")
	public ResponseEntity<?> updateMember(@RequestBody Member m) {
		
		int result = service.updateMember(m);
		
//		성공시 로그인정보 갱신,정보수정 성공 메시지 / 마이페이지로 이동
//		실패시 정보수정 실패 메시지 alert / 마이페이지로 이동  
		System.out.println("확인");
		if(result>0) {//성공
			Member loginMember = service.loginMember(m);
			//새로 조회한 정보 비밀번호 지우기 
			loginMember.setUserPwd(null);
			
			
			return ResponseEntity.ok(loginMember);
		
		}else {//실패
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								 .body("회원정보 수정에 실패했습니다.");
		}
		
	}
	
	
	@GetMapping("/checkId/{userId}")
	public ResponseEntity<?> idCheck(@PathVariable String userId) {
		
		int count = service.idCheck(userId);
		
		//있으면(중복) 1 없으면 0 
		if(count>0) {
			
			return ResponseEntity.ok("NNNNN");
		}else {
			
			return ResponseEntity.ok("NNNNY");
		}
	}
	
	
	
	
	
	
	
}

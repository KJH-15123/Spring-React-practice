package com.kh.spring.common.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor{
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//로그인 되어 있는지 확인 후 요청 처리 
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginMember") == null) {
			session.setAttribute("alertMsg", "로그인 후 이용 가능한 서비스 입니다.");
			response.sendRedirect(request.getContextPath());//메인페이지로 응답뷰 지정
			return false;
		}
		
		//위 조건이 아니라면 흐름 유지
		return true;
	}

}

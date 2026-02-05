package com.kh.spring.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 요청을 가로채서 특정 권한을 체크하거나,로그를 기록하거나, 공통적으로 사용해야하는 코드를 처리한다
 * 
 * 필터와의 차이점 - 간섭시점이 다르다.
 * 필터 : 디스패처 서블릿 전 시점(인코딩 작업)
 * 인터셉터 : 디스패처 서블릿 후 컨트롤러 전 (권한 및 추가 작업)
 * 
 * 인터셉터를 사용하기 위해 HandlerInterceptor를 구현하기 
 * */
public class TestInterceptor implements HandlerInterceptor{
	
	//간섭 시점 1 : 요청 처리 전 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//request : 사용자는 무엇을 요청했는가
		//response : 사용자에게 보낼 정보가 있는가
		//handler : 이 요청은 누가 처리할 것인가.
			
		
		System.out.println("====테스트 인터셉터 요청 처리 전====");
		System.out.println(request.getSession().getAttribute("loginMember"));//로그인 정보확인
		System.out.println(response);
		System.out.println(handler);
		
//		response.sendRedirect(request.getContextPath());//메인페이지로 보내기 (응답뷰 지정)
//		return false;
		//반환값이 true여야 기존 흐름을 유지한다. false면 기존흐름 방지 처리
		return true;
	}
	
	
	//간섭 시점 2 : 요청 처리 후 (view가 만들어지기 전)
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		//request : 사용자는 무엇을 요청했는가
		//response : 사용자에게 보낼 정보가 있는가 
		//handler :  이 요청은 누가 처리할것인가
		//modelandview : model(전달데이터) / view(이동할 뷰페이지 정보)
		
		System.out.println("======테스트 인터셉터 요청 처리 후=======");
		System.out.println(request);
		System.out.println(response);
		System.out.println(handler);
		System.out.println(modelAndView);
		
		//model에 담긴 값을 조작할 수 있지만 권장하지 않음 
		//ArrayList<Board> list = new ArrayList<>();//빈 리스트
		//modelAndView.addObject("list", list);//빈 리스트 담아주기
		
		//뷰 정보 조작하기 (조건에 따라서 다른 페이지 보여주기 작업 가능)
		//modelAndView.setViewName("common/errorPage");//에러페이지로 보내기
	}
	
	//간섭시점 3 : 사용자에게 출력되기 전
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//request  : 사용자는 무엇을 요청했는가
		//response : 사용자에게 보낼 정보가 있는가
		//handler : 이 요청은 누가 처리할것인가
		//exception : 처리과정중 예외가 발생하였는가
		
		System.out.println(ex);
	}
	
	
	
	

}

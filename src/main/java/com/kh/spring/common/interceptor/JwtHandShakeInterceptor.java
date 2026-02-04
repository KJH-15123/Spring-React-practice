package com.kh.spring.common.interceptor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.kh.spring.util.JwtUtil;

//jwt토큰으로 사용자 정보를 가져와 처리할 수 있게 하는 인터셉터 
//웹소켓 요청시 토큰정보를 추출하여 사용할 수 있도록 처리한다
@Component
public class JwtHandShakeInterceptor implements HandshakeInterceptor{

	
	@Autowired
	private JwtUtil jwtUtil;
	
	//요청전 
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		System.out.println("인터셉터");
		
		if(request instanceof ServletServerHttpRequest) {
			//해당 request를 servletServerHttpRequest로 다운캐스팅 하여 토큰정보 추출하기 
			//websocket 요청시 요청주소에 token을 쿼리스트링으로 전달하고 해당 토큰정보 추출하기 
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			
			//쿼리스트링으로 전달된 토큰값 파라미터 영역에서 추출하기
			String token = servletRequest.getServletRequest().getParameter("token");
			
			if(token!=null) {
				//토큰 검증 및 사용자 id 추출하기 (기존에 만들어둔 사용자 인증 및 아이디반환 메소드 사용)
				
				String userId = jwtUtil.getUserIdFromToken(token);
				attributes.put("userId", userId);//웹소켓 서버에서 추출할 수 있도록 아이디 넣어주기
				
				return true; //기존흐름 유지 
			}
		}
		
		return false;//토큰 없으면 연결거부
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}

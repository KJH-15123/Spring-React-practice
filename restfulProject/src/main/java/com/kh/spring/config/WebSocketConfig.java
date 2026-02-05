package com.kh.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.kh.spring.common.interceptor.JwtHandShakeInterceptor;
import com.kh.spring.websocket.server.BasicServer;
import com.kh.spring.websocket.server.ChatServer;
import com.kh.spring.websocket.server.GroupServer;
import com.kh.spring.websocket.server.MemberServer;
import com.kh.spring.websocket.server.PrivateServer;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
	
	
	@Autowired
	private JwtHandShakeInterceptor jwtInterceptor;
	
	
	//각 웹소켓 서버를 Bean 등록하기 
	@Bean
	public WebSocketHandler basicServer() {
		return new BasicServer();
	}
	
	@Bean
	public WebSocketHandler GroupServer() {
		return new GroupServer();
	}
	
	@Bean
	public WebSocketHandler MemberServer() {
		return new MemberServer();
	}
	
	@Bean
	public WebSocketHandler ChatServer() {
		return new ChatServer();
	}
	
	@Bean
	public WebSocketHandler PrivateServer() {
		return new PrivateServer();
	}
	
	//요청 매핑주소와 웹소켓 서버를 연결하는 핸들러 처리 
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		registry.addHandler(basicServer(), "/basic").setAllowedOrigins("*");//모든 요청 허용 (ex)localhost:5173
		registry.addHandler(GroupServer(), "/group").setAllowedOrigins("*");
		registry.addHandler(MemberServer(), "/member")
				.addInterceptors(jwtInterceptor).setAllowedOrigins("*"); // jwt 정보 가로채기
		
		registry.addHandler(ChatServer(), "/chat")
		.addInterceptors(new HttpSessionHandshakeInterceptor()); 
		
		registry.addHandler(PrivateServer(), "/private")
		.addInterceptors(jwtInterceptor).setAllowedOrigins("*"); 
				
		
	}
	
	
	
	
	
	

}

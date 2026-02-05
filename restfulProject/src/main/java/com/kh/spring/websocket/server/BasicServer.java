package com.kh.spring.websocket.server;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/*
 * 상속 또는 구현을 통해 websocket 관련 메소드 구현하기
 * websocket 서버를 만들기 위해서는 WebSocketHandler 인터페이스를 구현하거나
 * TextWebSocketHandler 클래스를 상속받는다.
 * 
 * 
 * 웹소켓 ( Websocket )
 * -WEB에서 수행하는 socket통신으로 연결형 통신이다(양방향)
 * -web의 기본 통신은 비연결형 통신을 사용한다.
 * -기본통신은 HTTP로 진행하고 연결형 통신이 필요한 상황에선 Websocket을 이용한다.
 * -실시간 작업을 할때 유용하다 (채팅,시세변동,실시간 알림 등등...)
 * */

public class BasicServer extends TextWebSocketHandler{
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//연결이 되었을때 동작하는 메소드 
		
		System.out.println("접속!");
		System.out.println(session);
	
	}
	
	//payload : 전달된 데이터(메시지)
	//byteCount : 바이트 수 
	//last : 메시지가 전부 전달되어 마지막 부분이라면 true 아니면 false(메시지가 나눠서 왔다면)
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		//session은 메시지를 전송한 대상의 websocketSession 정보 
		//textMessage는 전달된 메시지 정보 
		System.out.println(message);
		
		//전달받은 메시지를 해당 세션에 다시 보내기 
		session.sendMessage(message);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//연결이 종료되었을때 동작하는 메소드 
		
		System.out.println("연결 종료");
	}
	
	
	
	
}

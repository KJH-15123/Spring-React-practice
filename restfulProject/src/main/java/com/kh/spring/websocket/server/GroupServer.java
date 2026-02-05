package com.kh.spring.websocket.server;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//그룹 채팅이 가능한 그룹 서버 
public class GroupServer extends TextWebSocketHandler{
	
	/*
	 * 접속한 사용자 정보를 기록해놔야 해당 접속자에게 메시지를 공유할 수 있다.
	 * 클라이언트가 서버 접속 요청을 하면 접속된 사용자의 websocketSession 정보가 전달되고 이 정보를 기록해놓고
	 * 다른 클라이언트가 메시지를 보냈을때 접속했었던 사용자들의 websocketsession에 메시지를 전달한다.
	 * 
	 * 각 세션정보는 고유해야하기 때문에 저장하는 저장소는 중복을 허용하지 않는 저장소 (SET)을 이용해야한다.
	 * SET저장소 중 동기화 처리까지 되어있는 CopyOnWriteArraySet 저장소를 사용
	 * */
	
	//접속한 사용자 정보를 담을 저장소 준비 
	private Set<WebSocketSession> users = new CopyOnWriteArraySet<>();
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//연결이 되었을때 동작하는 메소드 
		//WebSocketSession 정보를 저장소에 담아두기 
		System.out.println("접속 : "+session);
		users.add(session);//접속자 정보 담기 
		System.out.println("현재 접속자 수는 "+users.size()+"명 입니다.");
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		//사용자가 메시지를 전달했을때 동작하는 메소드 
		//전달자 정보와 메시지 정보가 전달됨.
		
		//접속되어있는 사용자들에게 메시지를 모두 전달하면 된다.
		for(WebSocketSession user : users) {//반복문을 이용하여 모든 세션에 접근 (접속자들)
			user.sendMessage(message);//메시지 전달
		}
	}
	
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 접속 종료시 동작하는 메소드
		System.out.println("접속종료 : " + session);
		//접속 종료시 접속 종료한 세션정보를 저장소에서 지워주기 
		users.remove(session);
		System.out.println("남은 접속자 수는 "+users.size()+"명 입니다.");
	}
	

}

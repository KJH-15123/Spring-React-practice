package com.kh.spring.websocket.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kh.spring.member.model.vo.Member;

public class MemberServer extends TextWebSocketHandler{
	
	private Set<WebSocketSession> users = new CopyOnWriteArraySet<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("로그인 서버 접속!"+session.getAttributes().get("userId"));
		//session.getAttributes().get("세션에담긴 키값") - 해당 데이터 추출 가능
		users.add(session); //접속한 인원 세션 정보 기록하기(저장소에 담아주기)
		System.out.println("현재 접속자 수 : "+users.size()+"명");
	
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		//메시지 전송 형식 : [아이디] 메시지내용 (00:01:01)
		String time = new SimpleDateFormat("(HH:mm:ss)").format(new Date());
		String userId = (String)session.getAttributes().get("userId");
		String msg = message.getPayload();
		
		//전달 메시지 형식 바꾸기
		String responseMsg = "["+userId+"]"+msg+time;
		
		
		//반복문을 이용하여 모든 사용자에게 메시지 전달하기 
		
		message = new TextMessage(responseMsg);
		
		for(WebSocketSession user : users) {
			user.sendMessage(message);
		}
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	
		System.out.println("접속 종료!");
		//접속 종료시 저장소에서 세션정보 삭제하기 
		users.remove(session);
		System.out.println("현재 접속자 수 : "+users.size());
		
		
		
	}
}

package com.kh.spring.websocket.server;


import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.kh.spring.member.model.vo.Member;
import com.kh.spring.websocket.model.vo.MessageVO;

public class ChatServer extends TextWebSocketHandler{
	
	/*
	 * 일반 채팅과 귓속말을 나눠 처리 할 수 있는 서버를 구현해보기 
	 * chat.jsp 페이지를 만들어 해당 페이지에는 
	 * 기존 채팅 화면 + 접속한 사용자 목록을 채팅란 옆에 작성해보기 (태그는 자유롭게)
	 * 접속한 사용자 목록은 사용자의 아이디를 띄우고 해당 아이디를 클릭하면 그 아이디에게 
	 * 귓속말을 보낼 수 있도록 작성해보기 
	 * 만약 해당 사용자가 접속되어있지 않다면 접속을 종료한 대상입니다 라는 메시지를 
	 * 본인에게 보내주기 
	 * 상대방의 아이디를 클릭하지 않은 상태로 채팅메시지를 입력시 일반 메시지 형태로 전달되어
	 * 모두에게 보일 수 있도록 처리해보세요.
	 * 
	 * */
	
	//사용자 아이디와 세션정보 담을 저장소
	private Map<String,WebSocketSession> users = Collections.synchronizedMap(new HashMap<>());
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//사용자 접속 이후 
		Member loginMember = (Member)session.getAttributes().get("loginMember");
		
		users.put(loginMember.getUserId(), session);
		
		
		Set<String> userList = users.keySet();
		//admin,user01,qwe,user02
		
		//사용자 목록을 보낸다라는 의미로 사용할 키값과 함께 전송하기 
		Map<String,Set<String>> userListMap = new HashMap<>();
		
		userListMap.put("userList", userList);
		
		//map json문자열 형태로 전달
		TextMessage tm = new TextMessage(new Gson().toJson(userListMap));

		//사용자가 추가 접속했으니 기존 사용자들에게 모두 목록 보내주기 
		for(String user : userList) {
			//접근된 키값으로 session 접근해서 메시지 보내기 
			users.get(user).sendMessage(tm);//모두에게 목록 데이터 전달
		}
		
		
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		//System.out.println(message.getPayload());
		//JSONObject msgJson = (JSONObject) new JSONParser().parse(message.getPayload());
		//gson을 이용하여 json 문자열 데이터를 사용자 정의 VO로 파싱시키기 
		//gson.fromJson(json문자열,파싱할VO.class); 
		//주의사항 : 키값과 필드명 일치시켜야 파싱됨
		
		Gson gson = new Gson();
		
		MessageVO mvo = gson.fromJson(message.getPayload(), MessageVO.class);
		
		//시간 추출
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		mvo.setTime(time);
		//메시지 VO json문자열화 시켜서 전달
		message = new TextMessage(gson.toJson(mvo));
		
		//일반채팅인지 귓속말인지 판별 
		if(mvo.getType()==1) {//일반채팅 (모든 사용자에게 메시지 보내기)
			
			for(Entry<String, WebSocketSession> e : users.entrySet()) {
				
				e.getValue().sendMessage(message);
			}
			
		}else {//귓속말 (otherId로 session 접근해서 귓속말하기)
			
			//해당 접속자가 존재한다면 보내기 
			if(users.containsKey(mvo.getOtherId())) { //접속자가 있다면 
				
				users.get(mvo.getOtherId()).sendMessage(message);
				
			}else {//귓속말 대상이 접속을 종료한 경우
				mvo.setMessage("상대방이 접속을 종료하였습니다.");
				message = new TextMessage(gson.toJson(mvo));
				session.sendMessage(message);
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		Member loginMember = (Member)session.getAttributes().get("loginMember");
		
		users.remove(loginMember.getUserId());
		
		//사용자가 줄어들었으니 목록 갱신시키기 
		Set<String> userList = users.keySet();
		
		//사용자 목록을 보낸다라는 의미로 사용할 키값과 함께 전송하기 
		Map<String,Set<String>> userListMap = new HashMap<>();
		
		userListMap.put("userList", userList);
		
		//map json문자열 형태로 전달
		TextMessage tm = new TextMessage(new Gson().toJson(userListMap));

		//사용자가 추가 접속했으니 기존 사용자들에게 모두 목록 보내주기 
		for(String user : userList) {
			//접근된 키값으로 session 접근해서 메시지 보내기 
			users.get(user).sendMessage(tm);//모두에게 목록 데이터 전달
		}
	}
	
	
	
	
	

}

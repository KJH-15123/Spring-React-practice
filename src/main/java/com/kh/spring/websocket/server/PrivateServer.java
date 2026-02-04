package com.kh.spring.websocket.server;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.kh.spring.member.model.vo.Member;
import com.kh.spring.websocket.model.vo.MessageVO;


//특정 대상에게만 메시지를 보낼 수 있는 서버
public class PrivateServer extends TextWebSocketHandler{
	
	//기존 접속자들을 저장하는 저장소는 session 정보만을 기록할 수 있었기 때문에 
	//사용자가 특정 대상을 지정할 수 없다
	//특정 대상에게만 채팅을 보내려고 한다면 대상이 가지는 식별자가 필요하고 
	//현재 사이트에서 사용되는 식별자는 사용자의 아이디값이다.
	//때문에 로그인된 사용자를 각각 식별하려면 사용자의 아이디와 session 정보를 연결지어 저장해야한다.
	//저장소는 key-value를 같이 저장할 수 있는 map을 사용
	
	//사용할 Map을 동기화 처리해주는 Collections.syncronizedMap() 메소드 이용하여 동기화 처리
	private Map<String,WebSocketSession> users = Collections.synchronizedMap(new HashMap<>());
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		//로그인 정보 추출
		String userId = (String)session.getAttributes().get("userId");
		
		
		//키값은 접속자 아이디 - value는 해당 접속자의 websocketsession 객체정보 
		users.put(userId, session); 
		
		//접속메시지 보내보기
		//TextMessage tm = new TextMessage(userId+"님이 접속하셨습니다.");
		
		//반복문 이용하여 users에 있는 모든 사용자에게 메시지 보내기 
		//session.sendMessage(tm); 
		
		//keyset 이용
		Set<String> userIds = users.keySet();
	
		//접속자 목록 전송하기 
		HashMap<String,Set<String>> userListMap = new HashMap<>();
		
		userListMap.put("userList",userIds);
		
		//JSON 문자열로 변환시켜 보내기 
		TextMessage tm = new TextMessage(new Gson().toJson(userListMap));
		
		//모든 접속자에게 전달하기 
		for(String user : userIds) {
			
			users.get(user).sendMessage(tm);
		}
		
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		//전달받은 메시지 확인
		System.out.println(message.getPayload());
		//json문자열을 json 객체로 파싱해주는 메소드
		JSONObject msgJson = (JSONObject) new JSONParser().parse(message.getPayload());
		
//		System.out.println(msgJson.get("userId"));
//		System.out.println(msgJson.get("otherId"));
//		System.out.println(msgJson.get("msg"));
//		System.out.println(msgJson.get("type"));
		
		//상대방 아이디를 추출했으니 해당 사용자에게만 메시지 보내보기 
		String otherId = (String)msgJson.get("otherId");
		
		//상대방 아이디를 이용해서 접속자중에 해당 아이디가 있다면 메시지 전송하기 
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		
		if(users.containsKey(otherId)) {//맵에 해당 키가 존재하는지 확인하는 메소드 containsKey("키값")
			
			//해당 아이디로 session 추출해서 메시지 전송하기 
			WebSocketSession otherSession = users.get(otherId);
			
			MessageVO mv = MessageVO.builder()
							.type(2)
							.myId((String)session.getAttributes().get("userId"))
							.otherId(otherId)
							.message((String)msgJson.get("message")) 
							.time(time)
							.build();
			TextMessage tm = new TextMessage(new Gson().toJson(mv));
			
			
			otherSession.sendMessage(tm);//특정 대상에게만 메시지 전송
			session.sendMessage(tm);//나한테도 보내기
			
		}else {//해당 유저가 접속되어있지 않으면 전챗
			//entryset 이용
			Set<Entry<String,WebSocketSession>> entrySet = users.entrySet();
			//메시지 데모데이터 처리해보기 
			MessageVO mv = MessageVO.builder()
									.type(1)
									.myId((String)session.getAttributes().get("userId"))
									.otherId(null)
									.time(time)
									.message((String)msgJson.get("message"))
									.build();
			
			String mvJson = new Gson().toJson(mv);//gson이용해서 객체 json문자열로 뽑아주기 
			
			//전달할 메시지 객체 TextMessage에 위에서 만든 VO 담아주기
			TextMessage tm = new TextMessage(mvJson);//json문자열 넣어주기
			
			for(Entry<String,WebSocketSession> entry : entrySet) {
				entry.getValue().sendMessage(tm);
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		//사용자가 접속을 종료하면 저장소에서 지워야한다. map을 사용하고 있으니 key를 이용해서 지워주기 
		String userId = (String)session.getAttributes().get("userId");
		
		users.remove(userId);//지워주기 
		
	}

}

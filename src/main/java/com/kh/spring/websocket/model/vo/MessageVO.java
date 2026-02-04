package com.kh.spring.websocket.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageVO {
	private int type;//1 이면 일반채팅 / 2면 귓속말 
	private String myId;
	private String otherId;
	private String time;
	private String message;
}

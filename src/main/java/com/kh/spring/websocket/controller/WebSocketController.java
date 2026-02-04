package com.kh.spring.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/websocket")//공통 매핑 레벨 처리 
public class WebSocketController {
	
	@GetMapping("/basic")
	public String basic() {
	
		return "websocket/basic";
	}
	
	
	@GetMapping("/group")
	public String group() {
	
		return "websocket/group";
	}
	
	@GetMapping("/member")
	public String member() {
	
		return "websocket/member";
	}
	
	@GetMapping("/private")
	public String privateChat() {
	
		return "websocket/privateChat";
	}

	@GetMapping("/chat")
	public String Chat() {
	
		return "websocket/chatPage";
	}
	
	
}

package com.kh.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kh.spring.common.interceptor.LoginInterceptor;
import com.kh.spring.common.interceptor.TestInterceptor;


/*
 * WebMvcConfigurer 
 * spring에서 웹설정을 사용자 정의하기 위해 제공하는 인터페이스
 * 기존 레거시 프로젝트에서 설정하던 xml파일에 대한 처리를 대체한다.
 * 
 * 
 * */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/*.me","/*.bo","/websocket/chat")
				.excludePathPatterns("/enrollForm.me"
									,"/insert.me"
									,"/login.me"
									,"/list.bo"
									,"/search.bo"
									,"/topList.bo"
									,"/detail.bo");
		
//		추가로 넣을 인터셉터가 있다면 아래와 같이 추가하면 된다.
//		registry.addInterceptor(new TestInterceptor())
//				.addPathPatterns("/*");
	}

}

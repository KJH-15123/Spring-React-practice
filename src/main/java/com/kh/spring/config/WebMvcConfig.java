package com.kh.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Web MVC 설정 클래스
 * 정적 리소스 매핑(파일 업로드 경로)
 * 
 * WebMvcConfigurer 인터페이스 
 * spring mvc설정을 커스터마이징 할 수 있는 콜백 메소드를 제공한다.
 * 기본 설정을 유지하며 필요한 부분만 오버라이드 할 수 있음
 * 
 * CORS설정도 가능하다.
 * 
 * 
 * */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	//application.properties에서 작성한 파일 경로 가져오기 
	@Value("${file.upload.path}")
	private String savePath;
	
	/*
	 * 정적 리소스 핸들러 설정 
	 * -파일 시스템의 특정경로를 url로 매핑시켜준다.
	 * -업로드된 파일을 웹에서 접근 가능하도록 요청해줌 
	 * 
	 * ex)
	 * -파일 저장 경로 - c:/uploadFiles/121515132893.jpg
	 * -URL 접근 : http://localhost:8080/upload/1231242521.jpg
	 * 
	 * */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		registry.addResourceHandler("/upload/**") //upload 라는 매핑요청이 들어왔다면 
				.addResourceLocations("file:///"+savePath) //file:접두사로 파일 시스템 경로 지정 
				//window는 file:///  경로 사용 linux 환경에선 file: 형태 사용 
				.setCachePeriod(3600); //캐시설정 3600초
		
	}
	
	
	
	

}

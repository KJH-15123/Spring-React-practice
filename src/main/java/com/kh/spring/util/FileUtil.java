package com.kh.spring.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

//파일 업로드 처리용 컴포넌트 
@Component
public class FileUtil {
	
	//파일 저장 경로 (application.properties에 작성 후 불러오기)
	//C:/uploadFiles
	@Value("${file.upload.path}")
	private String savePath;

	
	public String saveFile(MultipartFile uploadFile) throws Exception{
		
		//1.원본 파일명 추출
		String originName = uploadFile.getOriginalFilename();
		
		//2.시간형식 문자열로 뽑아주기 
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//3.랜덤값 5자리 추출
		int ranNum = (int)(Math.random()*90000+10000);
		
		
		//4.원본파일에서 확장자 추출 (마지막 . 기준으로 잘라내기)
		String ext = originName.substring(originName.lastIndexOf("."));
		
		//5.합쳐주기 
		String changeName = currentTime+ranNum+ext;
		
		//6.서버에 업로드 처리경로 
		//application.properties에 지정한 실제 저장 경로 불러와서 사용 
		
		
		//7.서버에 업로드 처리 
		//MultipartFile의 transferto 메소드 사용
		uploadFile.transferTo(new File(savePath+changeName));
		
		//8.변경된 이름 반환하기
		return changeName;
	}

	//파일 삭제 메소드 
	public boolean deleteFile(String deleteFile) {
		
		//저장경로 + 저장파일명 
		String filePath = savePath+deleteFile;
		
		File file = new File(filePath);
		
		if(file.exists()) {//파일이 있다면 
			
			return file.delete();//파일 삭제 
		}
		
		return false;
	}
	

}

package com.kh.spring.board.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Attachment {
	private int fileNo;//	FILE_NO	NUMBER
	private int refBno;//	REF_BNO	NUMBER
	private String originName;//	ORIGIN_NAME	VARCHAR2(255 BYTE)
	private String changeName;//	CHANGE_NAME	VARCHAR2(255 BYTE)
	private String filePath;//	FILE_PATH	VARCHAR2(1000 BYTE)
	private Date uploadDate; //	UPLOAD_DATE	DATE
	private int fileLevel;//	FILE_LEVEL	NUMBER
	private String status;//	STATUS	VARCHAR2(1 BYTE)
}

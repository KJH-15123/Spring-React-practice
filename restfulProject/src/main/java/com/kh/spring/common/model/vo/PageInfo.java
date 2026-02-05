package com.kh.spring.common.model.vo;

import com.kh.spring.common.template.Pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageInfo {
	private int currentPage;
	private int listCount;
	private int boardLimit;
	private int pageLimit;
	
	private int maxPage;
	private int startPage;
	private int endPage;
}

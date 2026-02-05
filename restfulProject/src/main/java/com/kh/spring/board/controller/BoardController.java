package com.kh.spring.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardListDTO;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.scheduler.template.TaskTest;
import com.kh.spring.common.template.Pagination;
import com.kh.spring.util.FileUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {
	
	
	@Autowired
	private BoardService service;
	
	//TaskTest 사용자 정의 스케쥴러가 등록된 클래스 준비
	@Autowired
	private TaskTest taskTest;
	
	//파일 유틸 
	@Autowired
	private FileUtil fileUtil;
	
	
	
	
	/*
	 * ResponseEntity 응답 상태 정리 
	 * 200번대 
	 * ResponseEntity.ok(data) : 조회 성공 200 - OK
	 * ResponseEntity.created(location).body(data); - 생성 성공 201번 (location-생성된 데이터 uri) - CREATED
	 * ResponseEntity.noContent().build(); - 성공했지만 반환 데이터 없음(delete)  - NO_CONTENT
	 * 
	 * 400번대
	 * ResponseEntity.badRequest().body('오류메시지') - 잘못된 요청 400번 -HttpStatus.BAD_REQUEST
	 * ResponseEntity.status(401).body('로그인이 필요합니다'); - 인증 오류 401번 - HttpStatus.UNAUTHORIZED
	 * ResponseEntity.status(403).body('접근권한 불충분'); - 접근 권한 오류 403번 - HttpStatus.FORBIDDEN
	 * ResponseEntity.status(404).body('사용자를 찾을 수 없습니다.') - 리소스 찾지 못함 404번 -HttpStatus.NOT_FOUND
	 * 
	 * 500번대
	 * ResponseEntity.status(500).body('서버오류') - INTERNAL_SERVER_ERROR
	 * 
	 * **body가 없으면 build() 필수 **
	 * 
	 * */
	@GetMapping("/list")
	public ResponseEntity<BoardListDTO> boardList(@RequestParam(value="page",defaultValue = "1")
						 	int currentPage,
						 	int size,
						 	String condition,
						 	String keyword
						   ) {
		//게시글 목록 
		//페이징처리에 필요한 요소들 
		//현재페이지값 : currentPage
		//게시글 총 개수 조회해오기 
		int listCount = service.listCount();
		int boardLimit = 5; //게시글 보여줄 개수
		int pageLimit = size; //페이징바 개수
		
		//PageInfo 받아오기 
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, boardLimit, pageLimit);
		
		ArrayList<Board> list = service.boardList(pi);
		
		
		//RepsonseEntity - spring에서 http 응답(상태코드+헤더+바디)을 제어할 수 있는 클래스 
		//상태코드를 이용해서 성공,실패 및 에러 처리까지도 응답할 수 있다.
		
		//기존 데이터만 응답시 항상 200 ok 로 처리됨 
		//ResponseEntity.ok(응답객체) : ok 는 200번 상태코드 (응답성공)
		return ResponseEntity.ok(BoardListDTO.of(list, pi));
	}
	
	
	@GetMapping("/list2")
	public ResponseEntity<BoardListDTO> boardList2(@RequestParam(value="page",defaultValue = "1")
													 	int currentPage,
													 	int size,
													 	String condition,
													 	String keyword
	   ) {
		//게시글 목록 || 검색목록
		//페이징처리에 필요한 요소들 
		//현재페이지값 : currentPage
		//게시글 총 개수 조회해오기 
		int listCount;
		HashMap<String,String> map = new HashMap<>();
		
		if(keyword!=null && !keyword.isEmpty()) {//키워드값이 있을때 
			
			
			map.put("keyword", keyword);
			map.put("condition", condition);
			
			listCount = service.searchListCount(map);
		}else {//키워드값이 없을때 
			
			listCount = service.listCount(); //일반 목록 조회
		}
		
		
		int boardLimit = 5; //게시글 보여줄 개수
		int pageLimit = size; //페이징바 개수
		
		//PageInfo 받아오기 
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, boardLimit, pageLimit);
		
		ArrayList<Board> list;
		
		if(keyword!=null && !keyword.isEmpty()) {//키워드값이 있을때 
			list = service.searchList(map, pi);
		}else {
			list = service.boardList(pi);
		}
		
		//RepsonseEntity - spring에서 http 응답(상태코드+헤더+바디)을 제어할 수 있는 클래스 
		//상태코드를 이용해서 성공,실패 및 에러 처리까지도 응답할 수 있다.
		
		//기존 데이터만 응답시 항상 200 ok 로 처리됨 
		//ResponseEntity.ok(응답객체) : ok 는 200번 상태코드 (응답성공)
		return ResponseEntity.ok(BoardListDTO.of(list, pi));
	}
	
	
	
	
	
	//url 경로 변수 받는 법 매핑주소에 {변수명} 으로 작성한다
	//매개변수로 받을때 @PathVariable 어노테이션과 함께 처리한다
	@GetMapping("/detail/{boardNo}")
	public ResponseEntity<?> boardDetail(@PathVariable int boardNo) {
		
		log.debug("게시글 번호 : {}",boardNo);
		
		//조회수 증가처리 
		int result = service.increaseCount(boardNo);
		
		if(result>0) { //조회수 증가 성공
			//게시글 조회
			Board b = service.boardDetail(boardNo);
			
			return ResponseEntity.ok(b); //조회 성공
		
		}else{ //조회수 증가 실패 

			return ResponseEntity.status(404)
								 .body("게시글을 찾을 수 없습니다.");
		}
	}
	
	
	//글작성 요청 
	@PostMapping("/insert")
	public ResponseEntity<?> boardInsert(Board b
							 ,MultipartFile uploadFile) {
		
		//첨부파일이 있다면 
		if(!uploadFile.getOriginalFilename().equals("")) {
			
			try {
				
				//저장경로 설정 및 저장,파일명 변경 작업 처리하기 
				String changeName = fileUtil.saveFile(uploadFile);
				String originName = uploadFile.getOriginalFilename();
				
				b.setChangeName(changeName);
				b.setOriginName(originName);
				
				//log.debug("업로드 파일명 : {}",changeName);
				
			}catch(Exception e) {
				//log.debug("파일 업로드 실패 : {}",e.getMessage());
				
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								     .body("파일 업로드에 실패했습니다.");
			}
			
		}
		
		//게시글 등록처리
		int result = service.boardInsert(b);
		
		if(result>0) {
			return ResponseEntity.status(HttpStatus.CREATED)
								 .body("게시글 등록 성공");
	
		}else {
		
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								 .body("게시글 등록 실패");
		}
	}
	
	
	
	
	/*
	 * 정보수정 성공시 성공메시지와 함께 새로운 첨부파일이 있고 기존에 첨부파일이 있었다면 해당 파일 삭제 후 디테일뷰로 재요청
	 * 정보수정 실패시 실패 메시지와 함께 디테일뷰로 재요청
	 * 
	 * 새로운 첨부파일이 있는 경우 - 데이터베이스에 변경된 데이터 적용 + 서버에 업로드된 기존 첨부파일 삭제 
	 * 새로운 첨부파일이 없는 경우 - 데이터베이스에 변경된 데이터 적용 
	 * 게시글 등록 기능에서 사용했던 첨부파일 처리 참고해서 진행할것
	 * 
	 * 첨부파일 삭제하는 방법 - 실제 파일저장경로찾아서 해당 파일을 파일객체에 연결하여 .delete() 메소드 사용
	 * */
	
	@PutMapping("/update")
	public ResponseEntity<?> updateBoard(Board b
							 			,MultipartFile uploadFile
							 ) {
		String deleteFile = null;
		//새로운 첨부파일이 전달되었는지 확인하기 
		if(uploadFile != null && !uploadFile.getOriginalFilename().equals("")) {
			
			//기존 첨부파일이 있는지 없는지 확인 (만약 새 첨부파일이 있고 기존 첨부파일도 있었으면 기존첨부파일을 지워야한다.)
			if(b.getOriginName()!=null) { 
				deleteFile = b.getChangeName();//서버에 저장된 경로(변경된 파일명) 추후 삭제처리용
			}
			
			//새로 업로드된 파일 서버에 업로드 및 변경이름 받아오기 
			String changeName;
			try {
				changeName = fileUtil.saveFile(uploadFile);
				//업로드 되었다면 데이터베이스에 등록할 이름들 board객체에 넣어주기 
				b.setOriginName(uploadFile.getOriginalFilename()); //원본파일명
				b.setChangeName(changeName);//저장경로 담기
			
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						 .body("파일 업로드 중 오류 발생");
			}
		}
		
		
		int result = service.updateBoard(b);
		
		
		if(result>0) {//정보수정 성공 
			
			
			//기존 첨부파일이 있었다면 삭제하기 
			if(deleteFile!=null) {//기존 파일경로가 저장되어있다면
				//파일 객체로 해당 경로 잡아서 삭제처리하기 
					
				boolean flag =  fileUtil.deleteFile(deleteFile);
			
				if(!flag) { //파일 삭제 중 문제가 생겼을 경우 
					log.warn("게시글 정보는 수정되었지만 파일 삭제 중 문제 발생!");
				}
			
				
			}
			return ResponseEntity.ok("게시글 수정 성공");
			
		}else {//정보수정 실패
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					 .body("게시글 정보 수정 실패!");
		}
	}

	
	
	@DeleteMapping("/delete/{boardNo}")
	public ResponseEntity<?> deleteBoard(@PathVariable int boardNo) {
	
		//게시글 번호로 게시글 조회
		Board b = service.boardDetail(boardNo);
		
		if(b==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
		}
		
		
		//게시글 삭제 요청 
		int result = service.deleteBoard(boardNo);
		
		if(result>0) {//데이터베이스에서 삭제처리 성공
				
			if(b.getOriginName()!=null) {
				
				boolean flag = fileUtil.deleteFile(b.getChangeName());
				
				if(!flag) {
					log.warn("정보 삭제는 되었지만 파일 삭제 오류 발생");
				}
			}
			return ResponseEntity.ok("게시글이 삭제되었습니다.");

		}else {//실패 
			return ResponseEntity
				   .status(HttpStatus.INTERNAL_SERVER_ERROR)
				   .body("게시글이 삭제 중 오류가 발생했습니다.");
		}
	}
	
	//검색 메소드
	@GetMapping("/search")
	public ResponseEntity<?> searchList(@RequestParam(value="page",defaultValue = "1")
							 int currentPage
							,String condition
							,String keyword
							) {
		
		HashMap<String,String> map = new HashMap<>();
		map.put("condition", condition);
		map.put("keyword", keyword);
		
		int listCount = service.searchListCount(map);
		int boardLimit = 5;
		int pageLimit = 10;
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, boardLimit, pageLimit);
		
		
		ArrayList<Board> searchList = service.searchList(map,pi);
		
		HashMap<String,Object> response = new HashMap<>();
		response.put("list", searchList);
		response.put("pi", pi);
		response.put("map", map);
		
		
		return ResponseEntity.ok(response);
	}
	
	
	//댓글 목록 조회
	@GetMapping("/replyList")
	public ResponseEntity<?> replyList(int refBno) {
		
		//참조게시글 번호를 이용하여 해당 게시글에 작성된 댓글 목록 조회해오기
		List<Reply> list = service.replyList(refBno);
		
		//목록 반환 
		return ResponseEntity.ok(list);
	}
	
	
	@ResponseBody
	@RequestMapping("/insertReply.re")
	public int insertReply(Reply r) {
		
		int result = service.insertReply(r);
		
		//반환값 보내기(프론트에서 성공실패 처리)
		return result;
	}
	
	
	@GetMapping("/topList")
	public ResponseEntity<?> topList(){
		
		List<Board> list = service.topList();
		
		
		return ResponseEntity.ok(list);
	}
	
	
	//사진게시글 목록 
	@RequestMapping("/photoList.ph")
	public String photoList(Model model) {
		
		//사진게시글 목록 조회하기 
		//조회하는 첨부파일은 대표이미지만 조회하기
		List<Board> list = service.photoList();
		
		model.addAttribute("list",list);
		
		return "photo/photoList";
		
	}
	
	
	//사진게시글 작성 영역으로
	@RequestMapping("/insert.ph")
	public String photoEnrollForm() {
		
		return "photo/photoEnrollForm";
	}
	
	/*
	//사진게시글 작성 
	@PostMapping("/insert.ph")
	//public String photoInsert(Board b,MultipartFile[] uploadFiles) { //첨부파일 여러개 배열로 받기 
	public String photoInsert(Board b
							 ,ArrayList<MultipartFile> uploadFiles
							 ,HttpSession session) {//list로 받아주기
		//첨부파일이 여러개일땐 해당 name값을 이용해서 MultipartFile의 배열 또는 리스트(제네릭)형태로 전달 받는다.
		//첨부파일 정보들이 여러개인 경우 각 첨부파일 정보를 구분지어 데이터베이스에 등록하여야한다.
		//해당 데이터들 추출하여 VO에 담아주기 
		ArrayList<Attachment> atList = new ArrayList<>();
		
		int count = 1; //첨부파일 대표사진 확인용 
		
		//반복문을 이용하여 전달받은 첨부파일들 처리하기 
		for(MultipartFile file : uploadFiles) {
			String changeName = saveFile(session, file);//파일 업로드 및 서버업로드파일명 받기
			String originName = file.getOriginalFilename();//원본파일명 추출 
			
			//파일 정보 객체 생성해서 리스트에 추가해주기 
			Attachment at = new Attachment();
			at.setChangeName(changeName);
			at.setOriginName(originName);
			at.setFilePath("/resources/uploadFiles/"); //저장경로작성
			//참조게시글 번호 추가는 아직 게시글이 등록되지 않았기 때문에 
			//게시글이 먼저 등록된 이후에 번호를 가져와야한다.
			
			
			if(count==1) {
				at.setFileLevel(count++); //1번 대표사진 판별용 데이터 추가 
			}else {
				at.setFileLevel(2); //대표사진 제외 나머지 사진들 2번 
			}
			
			//리스트에 추가
			atList.add(at);
		}
		
		//게시글 등록처리 
		//서비스에 게시글정보 Board와 첨부파일 목록 정보 List를 전달하기 
		int result = service.insertPhoto(b,atList);
		
		//등록 성공
		if(result>0) {
			session.setAttribute("alertMsg", "사진게시글 등록 성공!");
		}else {
			session.setAttribute("alertMsg", "사진게시글 등록 실패!");
		}
		
		
		return "redirect:/photoList.ph";
	}
	*/
	//사진게시글 상세보기
	@RequestMapping("/detail.ph")
	public String photoDetail(int bno
						     ,Model model) {
		
		//게시글 정보와 첨부파일 목록 조회하기
		Board b = service.boardDetail(bno);
		
		//첨부파일 목록 조회해오기 (참조 게시글 번호 전달하여 첨부파일 목록 조회하기
		List<Attachment> atList = service.AttachmentList(bno);
		
		
		model.addAttribute("b",b);
		model.addAttribute("atList",atList);
		
		
		return "photo/photoDetail";
	}
	

	
	
	
	
	
}

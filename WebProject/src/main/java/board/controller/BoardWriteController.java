package board.controller;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import common.FileUploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 게시글 작성 처리
@WebServlet("/board/write")
@MultipartConfig
public class BoardWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
	// 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;
    
    // Service 객체 생성
    public BoardWriteController() {
        boardService = new BoardServiceImpl();
    }
    
    // 게시글 작성 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String writerId = (String) session.getAttribute("loginId");

        	// 로그인하지 않은 사용자는 글 작성 불가
        if (writerId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        	// 관리자 화면에서 작성한 경우 다시 관리자 상세로 보내기 위한 값
        String returnUrl = request.getParameter("returnUrl");

        // boardForm.jsp에서 입력한 게시글 정보 가져오기
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");
        
        // 비밀글 체크하지 않았으면, 기본값 공개글
        if (isSecret == null) {
            isSecret = "N";
        }
        
        	// 게시글 정보를 DTO에 담기
        BoardDTO board = new BoardDTO();
        board.setTitle(title);
        board.setContent(content);
        board.setWriterId(writerId);
        board.setIsSecret(isSecret);

        List<BoardFileDTO> fileList = FileUploadUtil.uploadBoardFiles(request);	 // 첨부파일 업로드 처리

        int boardId = boardService.insertBoard(board, fileList);	// 게시글 + 첨부파일 DB 저장

        	// 등록 성공 시 상세 페이지로 이동
        if (boardId > 0) {
            if ("admin".equals(returnUrl)) {
                response.sendRedirect(request.getContextPath() + "/admin/board/detail?boardId=" + boardId);
            } else {
                response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
            }
         // 등록 실패 시 목록 페이지로 이동
        } else {
            if ("admin".equals(returnUrl)) {
                response.sendRedirect(request.getContextPath() + "/admin/board/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/board/list");
            }
        }
    }
}
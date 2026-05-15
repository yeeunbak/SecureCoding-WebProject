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

// 게시글 수정 처리
@WebServlet("/board/update")
@MultipartConfig
public class BoardUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    	// 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardUpdateController() {
        boardService = new BoardServiceImpl();
    }
    
    // 게시글 수정 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        int boardId = 0;

        // 수정할 게시글 번호 가져오기
        // 숫자가 아니거나 없으면 목록으로 이동
        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        // boardForm.jsp에서 입력한 수정 정보 가져오기
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String isSecret = request.getParameter("isSecret");

        // 비밀글 체크하지 않았으면, 기본값 공개글
        if (isSecret == null) {
            isSecret = "N";
        }
        
        	// 수정할 게시글 정보를 DTO에 담기
        BoardDTO board = new BoardDTO();
        board.setBoardId(boardId);
        board.setTitle(title);
        board.setContent(content);
        board.setIsSecret(isSecret);
        board.setWriterId(loginId);

        	// 새로 업로드한 첨부파일 처리
        List<BoardFileDTO> fileList = FileUploadUtil.uploadBoardFiles(request);

        	// 업로드한 파일에 게시글 번호 설정
        for (BoardFileDTO file : fileList) {
            file.setBoardId(boardId);
        }
        
        	// 게시글 + 첨부파일 수정 처리
        int result = boardService.updateBoard(board, fileList);

        	// 수정 성공 시 상세 페이지 이동
        if (result > 0) {
            response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
         // 수정 실패 시 목록 페이지 이동
        } else {
            response.sendRedirect(request.getContextPath() + "/board/list");
        }
    }
}
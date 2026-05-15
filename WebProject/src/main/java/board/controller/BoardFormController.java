package board.controller;

import java.io.IOException;
import java.util.List;

import board.dto.BoardDTO;
import board.dto.BoardFileDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 게시글 작성, 수정 화면
@WebServlet("/board/form")
public class BoardFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    	// 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;
    
    // Service 객체 생성
    public BoardFormController() {
        boardService = new BoardServiceImpl();
    }

    // 게시글 작성, 수정 화면 -> 요청 처리
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        	// 작성 -> null값 전달
        	// 수정 -> 게시글 번호 전달
        String boardIdParam = request.getParameter("boardId");

        	// 수정모드 (boardId 존재 시)
        if (boardIdParam != null && !boardIdParam.trim().equals("")) {
            int boardId = Integer.parseInt(boardIdParam);	// 문자열 → int 변환
            
            BoardDTO board = boardService.selectBoardDetail(boardId);	// 게시글 상세 조회

            	// 게시글이 존재하지 않으면 목록으로 이동
            if (board == null) {
                response.sendRedirect(request.getContextPath() + "/board/list");
                return;
            }
            
            	// 작성자가 아니면 수정 불가
            if (!loginId.equals(board.getWriterId())) {
                response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
                return;
            }
            
            	// 첨부파일 목록 조회
            List<BoardFileDTO> fileList = boardService.selectFileList(boardId);
            	
            	// 수정 화면에 출력할 데이터 저장
            request.setAttribute("board", board);
            request.setAttribute("fileList", fileList);
        }
        
        	// 게시글 작성,수정 화면 이동
        request.getRequestDispatcher("/board/boardForm.jsp").forward(request, response);
    }
}
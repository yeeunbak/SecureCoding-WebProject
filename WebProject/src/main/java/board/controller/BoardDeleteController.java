package board.controller;

import java.io.IOException;

import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 게시글 삭제 처리
@WebServlet("/board/delete")
public class BoardDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    	// 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public BoardDeleteController() {
        boardService = new BoardServiceImpl();
    }
    
    // 게시글 삭제 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        	// 현재 로그인 사용자 정보 가져오기
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        	// 삭제할 게시글 번호 가져오기
        // boardId가 숫자가 아니거나 없으면 목록으로 이동
        int boardId = 0;
        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        
        	// 게시글 삭제 처리
        	// 실제 작성자 검증 및 삭제 로직은 Service에서 처리
        boardService.deleteBoard(boardId, loginId);
        	
        	// 삭제 후 게시글 목록으로 이동
        response.sendRedirect(request.getContextPath() + "/board/list");
    }
}
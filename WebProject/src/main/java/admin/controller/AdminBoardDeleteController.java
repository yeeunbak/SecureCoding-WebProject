package admin.controller;

import java.io.IOException;

import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//관리자 게시글 삭제 처리
@WebServlet("/admin/board/delete")
public class AdminBoardDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    	// 게시글 관련 비즈니스 로직 처리용 Service
    private BoardService boardService;

    // Service 객체 생성
    public AdminBoardDeleteController() {
        boardService = new BoardServiceImpl();
    }

    	// 관리자 게시글 삭제 요청 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int boardId = 0;

        // 삭제할 게시글 번호 가져오기
        // 숫자가 아니거나 없으면 게시글 목록으로 이동
        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }

        	// 관리자 권한으로 게시글 삭제 처리
        boardService.adminDeleteBoard(boardId);

        // 삭제 완료 후, 관리자 게시글 목록으로 이동
        response.sendRedirect(request.getContextPath() + "/admin/board/list");
    }
}
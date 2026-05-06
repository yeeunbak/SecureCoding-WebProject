package admin.controller;

import java.io.IOException;

import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/board/delete")
public class AdminBoardDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public AdminBoardDeleteController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginRole = (String) session.getAttribute("loginRole");

        // 관리자 체크
        if (!"ADMIN".equals(loginRole)) {
            response.sendRedirect(request.getContextPath() + "/main.jsp");
            return;
        }

        int boardId = Integer.parseInt(request.getParameter("boardId"));

        boardService.adminDeleteBoard(boardId);

        response.sendRedirect(request.getContextPath() + "/admin/board/list");
    }
}
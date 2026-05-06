package board.controller;

import java.io.IOException;

import board.dto.CommentDTO;
import board.service.BoardService;
import board.service.BoardServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/comment/delete")
public class BoardCommentDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private BoardService boardService;

    public BoardCommentDeleteController() {
        boardService = new BoardServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        if (loginId == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        int boardId = 0;

        try {
            boardId = Integer.parseInt(request.getParameter("boardId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/board/list");
            return;
        }
        int commentId = Integer.parseInt(request.getParameter("commentId"));

        CommentDTO comment = new CommentDTO();
        comment.setBoardId(boardId);
        comment.setCommentId(commentId);
        comment.setWriterId(loginId);

        boardService.deleteComment(comment);

        response.sendRedirect(request.getContextPath() + "/board/detail?boardId=" + boardId);
    }
}